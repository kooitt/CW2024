package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.planes.FighterPlane;
import com.example.demo.actors.planes.UserPlane;
import com.example.demo.handlers.*;
import com.example.demo.view.LevelView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;


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
	private final EntityManager entityManager;

	private LevelView levelView;
	private final StringProperty nextLevelProperty = new SimpleStringProperty();
	private final Set<KeyCode> pressedKeys = new HashSet<>();
	private Map<ActiveActorDestructible, Rectangle> actorHitboxes = new HashMap<>();

	private final CollisionHandler collisionHandler;
	private final InputHandler inputHandler;
	private final PauseHandler pauseHandler;
	private final GameInitializer gameInitializer;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.entityManager = new EntityManager(root);

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();

		this.collisionHandler = new CollisionHandler();
		this.inputHandler = new InputHandler(pressedKeys, user, background, root, entityManager.getUserProjectiles());
		this.pauseHandler = new PauseHandler(scene, this::pauseGame, this::resumeGame);
		this.gameInitializer = new GameInitializer(root, scene, background, user, levelView, pauseHandler);
		initializeTimeline();
		entityManager.addFriendlyUnit(user);
		entityManager.addEnemyDestroyedListener(enemy -> user.incrementKillCount());
	}

	private void initializeFriendlyUnits(){
		if (!root.getChildren().contains(user)) {
			root.getChildren().add(user);
		}
	};

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		gameInitializer.initializeGame();
		inputHandler.initializeFireProjectileHandler();
		return scene;
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

	private void updateScene() {
		initializeFriendlyUnits();
		spawnEnemyUnits();
		entityManager.updateActors();
		generateEnemyFire();
		handleEnemyPenetration();

		collisionHandler.handleUserProjectileCollisions(
				entityManager.getUserProjectiles(),
				entityManager.getEnemyUnits()
		);
		collisionHandler.handleEnemyProjectileCollisions(
				entityManager.getEnemyProjectiles(),
				entityManager.getFriendlyUnits()
		);
		collisionHandler.handlePlaneCollisions(
				entityManager.getFriendlyUnits(),
				entityManager.getEnemyUnits()
		);

		entityManager.removeDestroyedActors();
		updateLevelView();
		checkIfGameOver();
		inputHandler.updateUserPlaneMovement();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void generateEnemyFire() {
		entityManager.getEnemyUnits().forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}


	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			entityManager.addEnemyProjectile(projectile);
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : entityManager.getEnemyUnits()) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.decrementKillCount();
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateKillCount(user.getKillCount());
	}


	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return entityManager.getCurrentNumberOfEnemies();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		entityManager.addEnemyUnit(enemy);
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

	public void pauseGame() {
		if (!pauseHandler.isPaused()) {
			timeline.pause();
		}
	}

	public void resumeGame() {
		if (pauseHandler.isPaused()) {
			timeline.play();
		}
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}
}