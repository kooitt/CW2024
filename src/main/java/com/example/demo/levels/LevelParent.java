package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.DestructionType;
import com.example.demo.actors.planes.FighterPlane;
import com.example.demo.actors.planes.UserPlane;
import com.example.demo.config.GameConfig;
import com.example.demo.config.GameState;
import com.example.demo.managers.*;
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

    private final CollisionManager collisionManager;
    private final InputManager inputManager;
    private final PauseManager pauseHandler;
    private final GameInitializer gameInitializer;
    private final NavigationManager navigationManager;
    private final SoundManager soundManager;

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

        this.collisionManager = new CollisionManager();
        this.inputManager = new InputManager(pressedKeys, user, background, root, entityManager.getUserProjectiles());
        this.pauseHandler = new PauseManager(
                scene,
                root,
                this::pauseGame,
                this::resumeGame,
                this::goToMainMenu,    // Add main menu action
                this::restartLevel     // Add restart action
        );
        this.gameInitializer = new GameInitializer(root, scene, background, user, levelView, pauseHandler);
        this.navigationManager = new NavigationManager(scene, screenWidth, screenHeight);
        this.soundManager = new SoundManager();
        initializeTimelineAndMusic();
        entityManager.addFriendlyUnit(user);
        entityManager.addEnemyDestroyedListener(enemy -> user.incrementKillCount());
    }

    private void initializeFriendlyUnits() {
        if (!root.getChildren().contains(user)) {
            root.getChildren().add(user);
        }
    }

    ;

    protected abstract void checkIfGameOver();

    protected abstract void spawnEnemyUnits();

    protected abstract LevelView instantiateLevelView();

    public Scene initializeScene() {
        gameInitializer.initializeGame();
        inputManager.initializeFireProjectileHandler();
        return scene;
    }

    public void startGame() {
        background.requestFocus();
        timeline.play();
    }

    protected void goToNextLevel(String nextLevelClassName, String nextLevelName) {
        stopTimelineAndMusic();
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
        collisionManager.handleUserProjectileCollisions(
                entityManager.getUserProjectiles(),
                entityManager.getEnemyUnits()
        );
        collisionManager.handleEnemyProjectileCollisions(
                entityManager.getEnemyProjectiles(),
                entityManager.getFriendlyUnits()
        );
        collisionManager.handlePlaneCollisions(
                entityManager.getFriendlyUnits(),
                entityManager.getEnemyUnits()
        );
        entityManager.removeDestroyedActors();
        updateLevelView();
        checkIfGameOver();
        inputManager.updateUserPlaneMovement();
    }

    private void initializeTimelineAndMusic() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
        timeline.getKeyFrames().add(gameLoop);
		soundManager.playBackgroundMusic("level");
    }

	private void stopTimelineAndMusic() {
		timeline.stop();
		soundManager.stopAllBackgroundMusic();
	}

    private void generateEnemyFire() {
        entityManager.getEnemyUnits().forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
    }


    private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
			soundManager.playShootSound("enemy");
            entityManager.addEnemyProjectile(projectile);
        }
    }

    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : entityManager.getEnemyUnits()) {
            if (enemyHasPenetratedDefenses(enemy)) {
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
        stopTimelineAndMusic();
        navigationManager.goToMainMenu();
    }

    protected void restartLevel() {
        stopTimelineAndMusic();
        navigationManager.restartLevel(this.getClass());
    }

    // Update existing pause/resume methods
    public void pauseGame() {
        if (!pauseHandler.isPaused()) {
            timeline.pause();
            inputManager.setGameState(GameState.PAUSED);
        }
    }

    public void resumeGame() {
        if (pauseHandler.isPaused()) {
            timeline.play();
            inputManager.setGameState(GameState.ACTIVE);
        }
    }

    protected void winGame() {
        stopTimelineAndMusic();
        inputManager.setGameState(GameState.WIN);
//        levelView.showWinImage();
        navigationManager.showWinScreen();
    }

    protected void loseGame() {
        stopTimelineAndMusic();
        inputManager.setGameState(GameState.LOSE);
//        levelView.showGameOverImage();
        navigationManager.showGameOverScreen(this.getClass());
    }
}