package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.DestructionType;
import com.example.demo.actors.planes.FighterPlane;
import com.example.demo.actors.planes.UserPlane;
import com.example.demo.config.GameConfig;
import com.example.demo.controller.GameController;
import com.example.demo.controller.Main;
import com.example.demo.controller.MainMenuController;
import com.example.demo.handlers.*;
import com.example.demo.view.LevelView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



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

	public LevelParent(String backgroundImageName, int playerInitialHealth) {
		this.screenHeight = GameConfig.SCREEN_HEIGHT;
		this.screenWidth = GameConfig.SCREEN_WIDTH;
		this.root = new Group();
		this.scene = new Scene(root, this.screenWidth, this.screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.entityManager = new EntityManager(root);

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();

		this.collisionHandler = new CollisionHandler();
		this.inputHandler = new InputHandler(pressedKeys, user, background, root, entityManager.getUserProjectiles());
		this.pauseHandler = new PauseHandler(
				scene,
				root,
				this::pauseGame,
				this::resumeGame,
				this::goToMainMenu,    // Add main menu action
				this::restartLevel     // Add restart action
		);
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
//		entityManager.removeDestroyedActors();

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
//				user.decrementKillCount();
				user.takeDamage();
				enemy.destroy(DestructionType.PENETRATED_DEFENSE);
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


	protected void goToMainMenu() {
		timeline.stop();
		try {
			URL fxmlLocation = getClass().getClassLoader().getResource("MenuScreen.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlLocation);
			Parent menuRoot = loader.load();

			// Create new scene with static dimensions
			Scene menuScene = new Scene(menuRoot, screenWidth, screenHeight);

			// Get the stage and set the new scene
			Stage stage = (Stage) scene.getWindow();
			MainMenuController controller = loader.getController();
			controller.setStage(stage);
			stage.setScene(menuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void restartLevel() {
		timeline.stop();
		try {
			// Create new instance using the no-args constructor
			Class<?> currentLevelClass = this.getClass();
			Constructor<?> constructor = currentLevelClass.getConstructor();
			LevelParent newLevel = (LevelParent) constructor.newInstance();

			Stage stage = (Stage) scene.getWindow();

			newLevel.nextLevelProperty().addListener((observable, oldValue, newValue) -> {
				try {
					String[] levelInfo = newValue.split(",");
					GameController gameController = new GameController(stage);
					gameController.goToLevel(levelInfo[0], levelInfo[1]);
				} catch (Exception e) {
					e.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(e.getClass().toString());
					alert.show();
				}
			});

			Scene newScene = newLevel.initializeScene();
			stage.setScene(newScene);
			newLevel.startGame();
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}

	// Update existing pause/resume methods
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