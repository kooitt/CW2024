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
		import java.util.stream.Collectors;

/**
 * Represents a parent class for all levels in the game.
 */
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

//	private final List<ActiveActorDestructible> friendlyUnits;
//	private final List<ActiveActorDestructible> enemyUnits;
//	private final List<ActiveActorDestructible> userProjectiles;
//	private final List<ActiveActorDestructible> enemyProjectiles;

//	private int currentNumberOfEnemies;
	private LevelView levelView;
	private final StringProperty nextLevelProperty = new SimpleStringProperty();
	private final Set<KeyCode> pressedKeys = new HashSet<>();
	private Map<ActiveActorDestructible, Rectangle> actorHitboxes = new HashMap<>();

	private final CollisionHandler collisionHandler;
	private final InputHandler inputHandler;
	private final PauseHandler pauseHandler;
	private final GameInitializer gameInitializer;

	/**
	 * Constructs a LevelParent with the specified parameters.
	 *
	 * @param backgroundImageName the name of the background image.
	 * @param screenHeight the height of the screen.
	 * @param screenWidth the width of the screen.
	 * @param playerInitialHealth the initial health of the player.
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
//		this.friendlyUnits = new ArrayList<>();
//		this.enemyUnits = new ArrayList<>();
//		this.userProjectiles = new ArrayList<>();
//		this.enemyProjectiles = new ArrayList<>();
		this.entityManager = new EntityManager(root);

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
//		this.currentNumberOfEnemies = 0;
		this.collisionHandler = new CollisionHandler();
		this.inputHandler = new InputHandler(pressedKeys, user, background, root, entityManager.getUserProjectiles());
		this.pauseHandler = new PauseHandler(scene, this::pauseGame, this::resumeGame);
		this.gameInitializer = new GameInitializer(root, scene, background, user, levelView, pauseHandler);
		initializeTimeline();
//		entityManager.addFriendlyUnit(user);
		entityManager.addEnemyDestroyedListener(enemy -> user.incrementKillCount());
	}

	/**
	 * Initializes the friendly units in the level.
	 */
	private void initializeFriendlyUnits(){
		System.out.println("Checking if user plane exists in root");
		if (!root.getChildren().contains(user)) {
			System.out.println("Adding user plane to root in initializeFriendlyUnits");
			root.getChildren().add(user);
		}else{
			System.out.println("User plane already exists in root");
		}
	};

	/**
	 * Checks if the game is over.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns enemy units in the level.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the view for the level.
	 *
	 * @return the view for the level.
	 */
	protected abstract LevelView instantiateLevelView();

	/**
	 * Initializes the scene for the level.
	 *
	 * @return the initialized scene.
	 */
	public Scene initializeScene() {
		gameInitializer.initializeGame();
		inputHandler.initializeFireProjectileHandler();
//		initializeFriendlyUnits();
		return scene;
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Advances to the next level.
	 *
	 * @param nextLevelClassName the class name of the next level.
	 * @param nextLevelName the name of the next level.
	 */
	protected void goToNextLevel(String nextLevelClassName, String nextLevelName) {
		nextLevelProperty.set(nextLevelClassName + "," + nextLevelName);
	}

	/**
	 * Returns the property for the next level.
	 *
	 * @return the property for the next level.
	 */
	public StringProperty nextLevelProperty() {
		return nextLevelProperty;
	}

	/**
	 * Updates the scene.
	 */
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
//		updateUserKillCount();
		updateLevelView();
		checkIfGameOver();
		inputHandler.updateUserPlaneMovement();
	}

	/**
	 * Initializes the timeline for the game loop.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Generates enemy fire.
	 */
	private void generateEnemyFire() {
		entityManager.getEnemyUnits().forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	/**
	 * Spawns an enemy projectile.
	 *
	 * @param projectile the enemy projectile to spawn.
	 */
	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			entityManager.addEnemyProjectile(projectile);
		}
	}




	/**
	 * Handles enemy penetration.
	 */
	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : entityManager.getEnemyUnits()) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	/**
	 * Updates the level view.
	 */
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateKillCount(user.getKillCount());
	}

	/**
	 * Updates the user's kill count.
	 */
//	private void updateUserKillCount() {
//		int previousNumberOfEnemies = entityManager.getCurrentNumberOfEnemies();
//		System.out.println("previousNumberOfEnemies: " + previousNumberOfEnemies);
//
//		// Ensure enemy units are properly removed
//		entityManager.removeDestroyedActors();
//
//		int currentNumberOfEnemies = entityManager.getEnemyUnits().size();
//		System.out.println("currentNumberOfEnemies: " + currentNumberOfEnemies);
//
//		for (int i = 0; i < previousNumberOfEnemies - currentNumberOfEnemies; i++) {
//			user.incrementKillCount();
//		}
//	}

	/**
	 * Checks if an enemy has penetrated the defenses.
	 *
	 * @param enemy the enemy to check.
	 * @return true if the enemy has penetrated the defenses, false otherwise.
	 */
	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	/**
	 * Returns the user plane.
	 *
	 * @return the user plane.
	 */
	protected UserPlane getUser() {
		return user;
	}

	/**
	 * Returns the root group of the scene.
	 *
	 * @return the root group of the scene.
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Returns the current number of enemies.
	 *
	 * @return the current number of enemies.
	 */
	protected int getCurrentNumberOfEnemies() {
		return entityManager.getCurrentNumberOfEnemies();
	}

	/**
	 * Adds an enemy unit to the level.
	 *
	 * @param enemy the enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		entityManager.addEnemyUnit(enemy);
	}

	/**
	 * Returns the maximum Y position for enemies.
	 *
	 * @return the maximum Y position for enemies.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Returns the width of the screen.
	 *
	 * @return the width of the screen.
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user is destroyed.
	 *
	 * @return true if the user is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}



	/**
	 * Pauses the game.
	 */
	public void pauseGame() {
		if (!pauseHandler.isPaused()) {
			timeline.pause();
		}
	}

	/**
	 * Resumes the game.
	 */
	public void resumeGame() {
		if (pauseHandler.isPaused()) {
			timeline.play();
		}
	}

	/**
	 * Handles winning the game.
	 */
	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	/**
	 * Handles losing the game.
	 */
	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}
}