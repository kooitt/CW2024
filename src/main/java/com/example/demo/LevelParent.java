package com.example.demo;

import java.net.URL;
import java.util.*;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.util.Duration;

public abstract class LevelParent extends ObservableHelper implements LevelBehaviour{

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private final LevelParentView levelView;
	private final LevelParentController controller;
	private final CollisionHandler collisionHandler;
	private final ActorManager actorManager;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
        this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.collisionHandler = new CollisionHandler();
		this.actorManager = new ActorManager(root);
		URL resource = getClass().getResource(backgroundImageName);
		if (resource != null){
			this.background = new ImageView(new Image(resource.toExternalForm()));
		} else {
			System.err.println("Background image not found: " + backgroundImageName);
			this.background = new ImageView();
		}
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.controller = createController();
        this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
    }

	// INITIALIZE
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	private void initializeBackground() {
		configureBackgroundProperties();
		//setUpKeyHandlers();
		addBackgroundToScene();
	}

	private void configureBackgroundProperties(){
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
	}

	private void addBackgroundToScene(){
		root.getChildren().add(background);
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	protected LevelParentController createController() {
		return new LevelParentController(this, this.levelView);
	}

	// GAME FLOW METHODS
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void endGame() {
		stopGameLoop();
		removeEventHandlers();
		clearSceneGraph();
		resetGameState();
		notifyControllerOfEndGame();
	}

	public void stopGameLoop(){
		timeline.stop();
		timeline.getKeyFrames().clear();
	}

	private void removeEventHandlers(){
		background.setOnKeyPressed(null);
		background.setOnKeyReleased(null);
	}

	private void clearSceneGraph(){
		root.getChildren().clear();
	}

	private void resetGameState(){
		friendlyUnits.clear();
		enemyUnits.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
	}

	private void notifyControllerOfEndGame() {
		stopGameLoop();
		if (user.isDestroyed()) {
			controller.winGame();
		} else {
			controller.loseGame();
		}
	}

	public void goToNextLevel(String levelName) {
		endGame();
		notifyObservers(levelName);
	}

	// SCENE UPDATE METHODS
	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		handleInteractions();
		updateNumberOfEnemies();
		removeAllDestroyedActors();
		updateKillCount();
		controller.updateLevelView();
		checkIfGameOver();
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void handleInteractions(){
		generateEnemyFire();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
	}

	// ACTOR MANAGEMENT
	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void removeAllDestroyedActors() {
		actorManager.removeDestroyedActors(friendlyUnits);
		actorManager.removeDestroyedActors(enemyUnits);
		actorManager.removeDestroyedActors(userProjectiles);
		actorManager.removeDestroyedActors(enemyProjectiles);
	}

	// COLLISION AND INTERACTION HANDLING
	private void handlePlaneCollisions() {
		collisionHandler.handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		collisionHandler.handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		collisionHandler.handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	// LEVEL STATE MANAGEMENT
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	// UTILITY AND HELPER METHODS
	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected List<ActiveActorDestructible> getUserProjectile() {
		return userProjectiles;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	public LevelParentView getLevelView(){
		return levelView;
	}

	public LevelParentController getController(){
		return controller;
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}
}
