package com.example.demo.levels;

import java.util.*;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.view.LevelView;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public abstract class LevelParent {

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
	private LevelView levelView;
	private final StringProperty nextLevelProperty = new SimpleStringProperty();
	private final Set<KeyCode> pressedKeys = new HashSet<>();
//	private final PauseMenu pauseMenu;
	private Map<ActiveActor, Rectangle> actorHitboxes = new HashMap<>();
	private boolean isPaused = false;


	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
//		pauseMenu = new PauseMenu(this);
//		root.getChildren().add(pauseMenu);
//		pauseMenu.setVisible(false);
		initializePauseHandler();
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showKillCountDisplay();
		return scene;
	}

	private void initializePauseHandler(){
		scene.setOnKeyPressed(e-> {
			if(e.getCode() == KeyCode.ESCAPE) {
				if (isPaused) {
					resumeGame();
				} else {
					pauseGame();
				}
			}
		});

	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	protected void goToNextLevel(String nextLevelClassName, String nextLevelName) {
		nextLevelProperty.set(nextLevelClassName + "," + nextLevelName);
	}

	public StringProperty nextLevelProperty() {
		return nextLevelProperty;
	}

	//refactor this!
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
		updateUserKillCount();
		updateLevelView();
		checkIfGameOver();
		updateUserPlaneMovement();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				pressedKeys.add(e.getCode());
				if (e.getCode() == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				pressedKeys.remove(e.getCode());
			}
		});
		root.getChildren().add(background);
	}

	public void updateUserPlaneMovement(){
		if (pressedKeys.contains(KeyCode.UP)) user.moveUp();
		if (pressedKeys.contains(KeyCode.DOWN)) user.moveDown();
		if (pressedKeys.contains(KeyCode.LEFT)) user.moveLeft();
		if (pressedKeys.contains(KeyCode.RIGHT)) user.moveRight();
		if (!pressedKeys.contains(KeyCode.UP) && !pressedKeys.contains(KeyCode.DOWN)) user.stopVertical();
		if (!pressedKeys.contains(KeyCode.LEFT) && !pressedKeys.contains(KeyCode.RIGHT)) user.stopHorizontal();
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

	private void updateActors() {
		friendlyUnits.forEach(plane -> {
			plane.updateActor();
			addHitboxToScene(plane);
		});
		enemyUnits.forEach(enemy -> {
			enemy.updateActor();
			addHitboxToScene(enemy);
		});
		userProjectiles.forEach(projectile -> {
			projectile.updateActor();
			addHitboxToScene(projectile);
		});
		enemyProjectiles.forEach(projectile -> {
			projectile.updateActor();
			addHitboxToScene(projectile);
		});
	}

	private void addHitboxToScene(ActiveActorDestructible actor) {
		// Remove the old hitbox if it exists
		if (actorHitboxes.containsKey(actor)) {
			root.getChildren().remove(actorHitboxes.get(actor));
		}

		// Add the new hitbox
		Rectangle hitbox = actor.getHitboxRectangle();
		actorHitboxes.put(actor, hitbox);
		root.getChildren().add(hitbox);
	}



	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		destroyedActors.forEach(actor -> root.getChildren().remove(actorHitboxes.remove(actor)));
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1,
								  List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
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
		levelView.updateKillCount(user.getKillCount());
	}

	private void updateUserKillCount() {
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

	public void pauseGame(){
		if (!isPaused){
			timeline.pause();
			isPaused = true;
//			showPauseMenu();
		}
	}

	public void resumeGame(){
		if (isPaused){
			timeline.play();
			isPaused = false;
//			hidePauseMenu();
		}
	}

//	public void showPauseMenu(){
//		pauseMenu.setVisible(true);
//	}
//
//	public void hidePauseMenu(){
//		pauseMenu.setVisible(false);
//	}
}