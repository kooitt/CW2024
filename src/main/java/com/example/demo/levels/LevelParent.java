package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
import com.example.demo.handlers.CollisionHandler;
import com.example.demo.handlers.InputHandler;
import com.example.demo.handlers.PauseHandler;
import com.example.demo.handlers.GameInitializer;
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

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private int currentNumberOfEnemies;
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
		this.collisionHandler = new CollisionHandler();
		this.inputHandler = new InputHandler(pressedKeys, user, background, root, userProjectiles);
		this.pauseHandler = new PauseHandler(scene, this::pauseGame, this::resumeGame);
		this.gameInitializer = new GameInitializer(root, scene, background, user, levelView, pauseHandler);
		initializeTimeline();
		friendlyUnits.add(user);
	}

	/**
	 * Initializes the friendly units in the level.
	 */
	protected abstract void initializeFriendlyUnits();

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
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		collisionHandler.handleUserProjectileCollisions(userProjectiles, enemyUnits);
		collisionHandler.handleEnemyProjectileCollisions(enemyProjectiles, friendlyUnits);
		collisionHandler.handlePlaneCollisions(friendlyUnits, enemyUnits);
		removeAllDestroyedActors();
		updateUserKillCount();
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
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	/**
	 * Spawns an enemy projectile.
	 *
	 * @param projectile the enemy projectile to spawn.
	 */
	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	/**
	 * Updates the actors in the level.
	 */
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

	/**
	 * Adds a hitbox to the scene for the specified actor.
	 *
	 * @param actor the actor to add a hitbox for.
	 */
	private void addHitboxToScene(ActiveActorDestructible actor) {
		if (actorHitboxes.containsKey(actor)) {
			root.getChildren().remove(actorHitboxes.get(actor));
		}
		Rectangle hitbox = actor.getHitboxRectangle();
		actorHitboxes.put(actor, hitbox);
		root.getChildren().add(hitbox);
	}

	/**
	 * Removes all destroyed actors from the scene.
	 */
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	/**
	 * Removes destroyed actors from the specified list.
	 *
	 * @param actors the list of actors to remove destroyed actors from.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		destroyedActors.forEach(actor -> root.getChildren().remove(actorHitboxes.remove(actor)));
		actors.removeAll(destroyedActors);
	}

	/**
	 * Handles enemy penetration.
	 */
	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	/**
	 * Updates the level view.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateKillCount(user.getKillCount());
	}

	/**
	 * Updates the user's kill count.
	 */
	private void updateUserKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

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
		return enemyUnits.size();
	}

	/**
	 * Adds an enemy unit to the level.
	 *
	 * @param enemy the enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
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
	 * Updates the number of enemies.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
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