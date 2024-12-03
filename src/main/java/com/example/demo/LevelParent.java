package com.example.demo;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
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
	private final LevelView levelView;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
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
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
	}

		this.actorManager = new ActorManager(root);
    }

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		endGame();
		notifyObservers(levelName);
	}

	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		configureBackgroundProperties();
		setUpKeyHandlers();
		addBackgroundToScene();
	}

	private void configureBackgroundProperties(){
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
	}

	private void setUpKeyHandlers(){
		background.setOnKeyPressed(this::handleKeyPress);
		background.setOnKeyReleased(this::handleKeyRelease);
	}

	private void handleKeyPress(KeyEvent e){
		KeyCode kc = e.getCode();
		if (kc == KeyCode.UP) user.moveUp();
		if (kc == KeyCode.DOWN) user.moveDown();
		if (kc == KeyCode.SPACE) fireProjectile();
	}

	private void handleKeyRelease(KeyEvent e){
		KeyCode kc = e.getCode();
		if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
	}

	private void addBackgroundToScene(){
		root.getChildren().add(background);
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

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

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

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

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	public void endGame() {
		// stop the game loop
		timeline.stop();
		timeline.getKeyFrames().clear();

		// remove all event handlers
		background.setOnKeyPressed(null);
		background.setOnKeyReleased(null);

		// clear all actors from the scene graph
		root.getChildren().clear();

		// reset state variables
		friendlyUnits.clear();
		enemyUnits.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
	}
}
