package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * The LevelParent class is an abstract base class that manages core functionalities
 * for various game levels, including spawning enemies, updating the game state,
 * and handling collisions. It provides a framework that subclasses can extend
 * to create specific game levels.
 */
public abstract class LevelParent extends Observable {

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

	/**
	 * Constructs a LevelParent instance with the specified background, screen dimensions, and initial player health.
	 *
	 * @param backgroundImageName The name of the background image.
	 * @param screenHeight        The height of the game screen.
	 * @param screenWidth         The width of the game screen.
	 * @param playerInitialHealth The initial health of the player.
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;

		initializeBackground();
		initializeTimeline();
		friendlyUnits.add(user);
	}

	/**
	 * Initializes the background image for the level.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		root.getChildren().add(background);
	}

	/**
	 * Initializes the timeline, setting the game loop's cycle count to indefinite and
	 * defining the delay between each frame.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Updates the game state for each frame by calling methods to spawn enemies, update actors,
	 * handle collisions, and check for game-over conditions.
	 */
	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	/**
	 * Starts the game by playing the timeline animation.
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Initializes the scene by setting up the background, friendly units, and heart display.
	 *
	 * @return The initialized Scene object for the level.
	 */
	public Scene initializeScene() {
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	/**
	 * Handles game loss by displaying the game-over image.
	 */
	protected void loseGame() {
		levelView.showGameOverImage();
		timeline.stop();  // Stop the game loop
	}

	/**
	 * Handles game win by displaying the win image.
	 */
	protected void winGame() {
		levelView.showWinImage();
		timeline.stop();  // Stop the game loop
	}

	/**
	 * Returns the current number of enemies.
	 *
	 * @return the number of enemies on screen
	 */
	protected int getCurrentNumberOfEnemies() {
		return (int) enemyUnits.stream().filter(enemy -> !enemy.isDestroyed()).count();
	}

	/**
	 * Notifies the controller to move to the next level.
	 *
	 * @param nextLevel the class name of the next level
	 */
	protected void goToNextLevel(String nextLevel) {
		setChanged();
		notifyObservers(nextLevel);
	}

	/**
	 * Updates the actors in the game by moving them and updating their positions.
	 */
	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
	}

	/**
	 * Generates enemy fire by making enemy planes shoot projectiles at certain intervals.
	 */
	private void generateEnemyFire() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			ActiveActorDestructible projectile = ((FighterPlane) enemy).fireProjectile();
			if (projectile != null) {
				enemyProjectiles.add(projectile);
				root.getChildren().add(projectile);
			}
		}
	}

	/**
	 * Handles collisions between planes (friendly and enemy) in the game.
	 */
	private void handlePlaneCollisions() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemy.getBoundsInParent().intersects(user.getBoundsInParent())) {
				user.takeDamage();
				enemy.takeDamage();
			}
		}
	}

	/**
	 * Updates the kill count for the user if an enemy is destroyed.
	 */
	private void updateKillCount() {
		int destroyedEnemies = (int) enemyUnits.stream().filter(ActiveActorDestructible::isDestroyed).count();
		for (int i = 0; i < destroyedEnemies; i++) {
			user.incrementKillCount();
		}
	}

	/**
	 * Removes all actors marked as destroyed from the game.
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	/**
	 * Removes actors that are destroyed from both the game screen and relevant lists.
	 *
	 * @param actors The list of actors to remove if destroyed.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	/**
	 * Handles collisions between enemy projectiles and the user plane.
	 * Reduces user health only if a projectile directly hits the user plane.
	 */
	private void handleEnemyProjectileCollisions() {
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				user.takeDamage(); // Reduce heart life only if projectile hits the user plane
				projectile.destroy(); // Destroy the projectile upon collision
			}
		}
	}

	/**
	 * Updates the level view to reflect the player's current health and kill count.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	/**
	 * Abstract method to initialize friendly units in the level.
	 * Subclasses must define how friendly units are set up.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Abstract method to check if the game is over.
	 * Subclasses must define game-over conditions.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Abstract method to spawn enemy units in the level.
	 * Subclasses must define specific enemy spawning behavior.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Abstract method to instantiate the level view.
	 * Each subclass must provide its specific LevelView implementation.
	 *
	 * @return The LevelView instance specific to the level.
	 */
	protected abstract LevelView instantiateLevelView();

	/**
	 * Adds an enemy unit to the level.
	 *
	 * @param enemy The enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	/**
	 * @return The user's current plane instance.
	 */
	public UserPlane getUser() {
		return user;
	}

	/**
	 * @return The root Group that holds all game objects in the level.
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * @return The maximum y-position at which an enemy can spawn.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * @return The width of the game screen.
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @return true if the user plane has been destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Updates the number of enemies currently on the screen.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}
