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

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
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
	 * Initializes the background image for the level and adds it to the root group.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		root.getChildren().add(background);
	}

	/**
	 * Initializes the timeline to create a game loop, setting its cycle count to indefinite
	 * and defining the delay between each frame.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Adds a projectile to the level, adding it to the user projectiles list and the root group.
	 *
	 * @param projectile The projectile to add.
	 */
	public void addProjectile(ActiveActorDestructible projectile) {
		userProjectiles.add(projectile);
		root.getChildren().add(projectile);
	}

	/**
	 * Adds an enemy unit to the level by adding it to the enemy units list and the root group.
	 *
	 * @param enemyUnit The enemy unit to add.
	 */
	public void addEnemyUnit(ActiveActorDestructible enemyUnit) {
		enemyUnits.add(enemyUnit);
		root.getChildren().add(enemyUnit);
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
		handleUserProjectileCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	/**
	 * Starts the game by focusing on the background and playing the timeline animation.
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
	 * Handles game loss by displaying the game-over image and stopping the game loop.
	 */
	protected void loseGame() {
		levelView.showGameOverImage();
		timeline.stop();
	}

	/**
	 * Handles game win by displaying the win image and stopping the game loop.
	 */
	protected void winGame() {
		levelView.showWinImage();
		timeline.stop();
	}

	/**
	 * Returns the current number of enemies that are not destroyed.
	 *
	 * @return The number of enemies on screen.
	 */
	protected int getCurrentNumberOfEnemies() {
		return (int) enemyUnits.stream().filter(enemy -> !enemy.isDestroyed()).count();
	}

	/**
	 * Notifies the controller to move to the next level.
	 *
	 * @param nextLevel The class name of the next level.
	 */
	protected void goToNextLevel(String nextLevel) {
		setChanged();
		notifyObservers(nextLevel);
	}

	/**
	 * Updates all actors in the game by moving them and updating their positions.
	 */
	private void updateActors() {
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
	}

	/**
	 * Makes enemy planes fire projectiles at certain intervals and adds them to the game.
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
	 * Handles collisions between user projectiles and enemy planes.
	 * If a collision is detected, both the projectile and the enemy plane are destroyed.
	 */
	private void handleUserProjectileCollisions() {
		List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();
		List<ActiveActorDestructible> enemiesToRemove = new ArrayList<>();

		for (ActiveActorDestructible projectile : userProjectiles) {
			for (ActiveActorDestructible enemy : enemyUnits) {
				if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					projectile.destroy();
					enemy.destroy();
					projectilesToRemove.add(projectile);
					enemiesToRemove.add(enemy);
					break;
				}
			}
		}

		userProjectiles.removeAll(projectilesToRemove);
		enemyUnits.removeAll(enemiesToRemove);
		root.getChildren().removeAll(projectilesToRemove);
		root.getChildren().removeAll(enemiesToRemove);
	}

	/**
	 * Handles collisions between the user plane and enemy planes.
	 * If a collision is detected, both the user and the enemy take damage.
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
	 * Removes destroyed actors from both the game screen and relevant lists.
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
	 * If a collision is detected, the user takes damage, and the projectile is destroyed.
	 */
	private void handleEnemyProjectileCollisions() {
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				user.takeDamage();
				projectile.destroy();
			}
		}
	}

	/**
	 * Updates the level view to reflect the player's current health and kill count.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	// Abstract methods for subclasses to implement specific level functionality
	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();
	protected abstract LevelView instantiateLevelView();

	// Getter methods for access to user plane and other game details

	/**
	 * Returns the user plane instance in the game.
	 *
	 * @return The user plane.
	 */
	public UserPlane getUser() {
		return user;
	}

	/**
	 * Returns the root group of the level, which contains all game objects.
	 *
	 * @return The root group.
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Returns the maximum y-position for enemy spawning within the level.
	 *
	 * @return The maximum y-position for enemies.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Returns the screen width of the level.
	 *
	 * @return The screen width.
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user plane is destroyed.
	 *
	 * @return True if the user is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Updates the current number of enemies.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}
