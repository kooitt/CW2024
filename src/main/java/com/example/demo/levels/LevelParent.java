package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterPlane;
import com.example.demo.actors.UserPlane;
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
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

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
	private Map<ActiveActorDestructible, javafx.scene.shape.Rectangle> actorHitboxes = new HashMap<>();
	private boolean isPaused = false;

	private final CollisionHandler collisionHandler;
	private final InputHandler inputHandler;

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
		initializeTimeline();
		friendlyUnits.add(user);
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

	private void initializePauseHandler() {
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
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

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		inputHandler.initializeFireProjectileHandler();
		root.getChildren().add(background);
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
		if (actorHitboxes.containsKey(actor)) {
			root.getChildren().remove(actorHitboxes.get(actor));
		}
		javafx.scene.shape.Rectangle hitbox = actor.getHitboxRectangle();
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

	public void pauseGame() {
		if (!isPaused) {
			timeline.pause();
			isPaused = true;
		}
	}

	public void resumeGame() {
		if (isPaused) {
			timeline.play();
			isPaused = false;
		}
	}
}