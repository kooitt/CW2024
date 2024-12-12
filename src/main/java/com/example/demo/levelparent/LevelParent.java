package com.example.demo.levelparent;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.actors.*;
import com.example.demo.actors.player.*;
import com.example.demo.controller.Main;
import com.example.demo.controller.SoundManager;
import com.example.demo.levels.LevelView;
import com.example.demo.controller.MainMenuController;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

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
	private final LevelView levelView;
	private boolean isGameActive;
	private boolean didGameEnd;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<ActiveActorDestructible> obstacles;
	private int currentNumberOfEnemies;
	private int currentNumberOfObstacles;

	private final Set<KeyCode> activeKeys = new HashSet<>(); //Creating a new hash-set which is basically a box that keeps track of all active keys being pressed by the player
	private long lastFiredProjectile = 0;
	private static final long PROJECTILE_COOLDOWN = 120; // Cooldown in milliseconds

	private Stage stage;
	private static final String MAIN_MENU_FXML = "/fxml/mainmenu.fxml";
	private Button popupButton;

	//sounds
	private SoundManager soundManager;
	private static final String BG_MUSIC = "/com/example/demo/sfx/level_music/mainMenuMusic.mp3";
	private static final String BUTTON_CLICK_SFX = "/com/example/demo/sfx/ui_sfx/buttonclick.mp3";
	private static final String SHOOT_SFX = "/com/example/demo/sfx/level_sfx/userShootalt.mp3";

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Stage stage) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.obstacles = new ArrayList<>();


        this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		this.currentNumberOfObstacles = 0;

		this.stage = stage;
		initializeTimeline();
		friendlyUnits.add(user);

		//Sound-related
		this.soundManager = SoundManager.getInstance();

		soundManager.loadSFX("button_click", BUTTON_CLICK_SFX);
		soundManager.loadSFX("shoot", SHOOT_SFX);
	}

	protected void playShootSound() {
		soundManager.playSFX("shoot");
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected ActiveActorDestructible createEnemy() {
		// Default implementation returns null (no enemy for this level)
		return null;
	}

	protected ActiveActorDestructible createObstacle() {
		// Default implementation returns null (no obstacle for this level)
		return null;
	}

	protected double getEnemySpawnProbability() {
		return 0.0; // Default: No enemy spawn probability
	}

	protected double getObstacleSpawnProbability() {
		return 0.0; // Default: No obstacle spawn probability
	}

	protected int getTotalEnemies() {
		return 0; // Default: No enemies allowed
	}

	protected int getTotalObstacles() {
		return 0; // Default: No obstacles allowed
	}


	protected void spawnEnemyUnits() {
		currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < getTotalEnemies() - currentNumberOfEnemies; i++) {
			if (Math.random() < getEnemySpawnProbability()) {
				ActiveActorDestructible newEnemy = createEnemy();
				if (newEnemy != null) {
					addEnemyUnit(newEnemy);
				}
			}
		}
	}

	protected void spawnObstacles() {
		currentNumberOfObstacles = getCurrentNumberOfObstacles();
		for (int i = 0; i < getTotalObstacles() - currentNumberOfObstacles; i++) {
			if (Math.random() < getObstacleSpawnProbability()) {
				ActiveActorDestructible newObstacle = createObstacle();
				if (newObstacle != null) {
					addObstacle(newObstacle);
				}
			}
		}
	}

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		isGameActive = true; //Add a value to help the game decide if the game is running at the moment. Useful
		didGameEnd = false;
		timeline.play();
	}

	public void pauseGame() {
		if (isGameActive && !didGameEnd) {
			isGameActive = false;
			timeline.pause();
			levelView.showPauseImage();
			showMainMenuButton(stage);
		}
		else if (!isGameActive && !didGameEnd) {
			isGameActive = true;
			timeline.play();
			levelView.hidePauseImage();

			if (popupButton != null) {
				Group root = (Group) scene.getRoot();
				root.getChildren().remove(popupButton);
				popupButton = null; // Clear the reference
			}
		}
	} //Pauses the game if the game is active, and starts the game again if it is already paused.

	private void cleanAssets() {
		//Do a proper cleaning of all assets on the screen before proceeding to the next level.
		user.destroy();
		userProjectiles.clear();
		friendlyUnits.clear();
		enemyUnits.clear();
		enemyProjectiles.clear();
		obstacles.clear();
	}

	public void goToNextLevel(String levelName) {
		timeline.stop(); //Fixes the memory leaks produced from not having the user plane cleared when level is cleared
		cleanAssets(); //Clean all assets on current screen
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		spawnEnemyUnits();
		spawnObstacles();
		updateActors();
		generateEnemyFire();
		handlePlayerActions();
		updateNumberOfEnemies();
		updateNumberOfObstacles();
		handleEnemyPenetration();
		handleObstaclePenetration();
		handleObstacleCollisions();
		handleProjectileObstacleCollisions();
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
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				activeKeys.add(kc); //On key press, add that key to the hash set
				if (kc == KeyCode.ESCAPE) pauseGame();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				activeKeys.remove(kc); //On key release, remove that key from the hash set
				if (kc == KeyCode.UP || kc == KeyCode.DOWN || kc == KeyCode.W || kc == KeyCode.S) {
					user.stopY();
				}
				else if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT || kc == KeyCode.A || kc == KeyCode.D) {
					user.stopX();
				}
			}
		});
		root.getChildren().add(background);
	}

	private void handlePlayerActions() {
		if (!isGameActive) return; //Void all inputs if the game is currently not active.
		if (activeKeys.contains(KeyCode.UP) || (activeKeys.contains(KeyCode.W))) user.moveUp();
		if (activeKeys.contains(KeyCode.DOWN) || (activeKeys.contains(KeyCode.S))) user.moveDown();
		if (activeKeys.contains(KeyCode.LEFT) || (activeKeys.contains(KeyCode.A))) user.moveLeft();
		if (activeKeys.contains(KeyCode.RIGHT) || (activeKeys.contains(KeyCode.D))) user.moveRight();
		if (activeKeys.contains(KeyCode.SPACE) || (activeKeys.contains(KeyCode.K))) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastFiredProjectile> PROJECTILE_COOLDOWN) {
				fireProjectile();
				playShootSound();
				lastFiredProjectile = currentTime;
			}
		} //Makes sure that the active keys don't make any weird combinations when the user is inputting as it can effectively separate the processing of keys
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
		friendlyUnits.forEach(ActiveActorDestructible::updateActor);
		enemyUnits.forEach(ActiveActorDestructible::updateActor);
		userProjectiles.forEach(ActiveActorDestructible::updateActor);
		enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
		obstacles.forEach(ActiveActorDestructible::updateActor);
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(obstacles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleObstacleCollisions() {
		handleCollisions(friendlyUnits, obstacles);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleProjectileObstacleCollisions() {
		handleCollisions(userProjectiles, obstacles);
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
				user.decrementKillCount();
			}
		}
	}

	private void handleObstaclePenetration() {
		for (ActiveActorDestructible obstacle : obstacles) {
			if (obstacleHasPenetratedDefenses(obstacle)) {
				obstacle.destroy();
				System.out.println("Obstacle cleared!");
			}
		}
	} //Obstacles do not count as enemies. Therefore, they should not make user take damage

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

	private boolean obstacleHasPenetratedDefenses(ActiveActorDestructible obstacles) {
		return Math.abs(obstacles.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		isGameActive = false;
		levelView.showWinImage();
		cleanAssets();
		showMainMenuButton(stage);
	}

	protected void loseGame() {
		timeline.stop();
		isGameActive = false;
		didGameEnd = true;
		levelView.showGameOverImage();
		showMainMenuButton(stage);
	}

	private void showMainMenuButton(Stage stage) {
		// Create a StackPane as a parent container to center the button
		Group root = (Group) scene.getRoot();

		if (popupButton == null) {
			popupButton = new Button("Go Back To Main Menu");
			popupButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");

			// Position the button in the center
			popupButton.setLayoutX(555); // X-coordinate
			popupButton.setLayoutY(475); // Y-coordinate

			popupButton.setFocusTraversable(false);

			popupButton.setOnAction(event -> {
				cleanAssets(); //To ensure all assets will always be cleaned.
				// Close the popup button (remove it) when clicked
				root.getChildren().remove(popupButton);
				soundManager.stopBackgroundMusic();
				soundManager.playBackgroundMusic(BG_MUSIC);
				MainMenuController mainMenuController = new MainMenuController();
				try {
					mainMenuController.showMainMenu(stage);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		// Add the popup button to the root layout (overlay)
		root.getChildren().add(popupButton);
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

	protected int getCurrentNumberOfObstacles() {
		return obstacles.size();
	}

	protected void addObstacle(ActiveActorDestructible obstacle) {
		obstacles.add(obstacle);
		root.getChildren().add(obstacle);
	} // Add obstacles to the scene

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

	private void updateNumberOfObstacles() {
		currentNumberOfObstacles = obstacles.size();
	}
}
