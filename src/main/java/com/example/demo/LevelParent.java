package com.example.demo;

import com.example.demo.controller.MainMenu;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private int score; // Field to track the score
    private Text scoreDisplay; // UI element to show the score

    // Pause menu variables
    private PauseMenu pauseMenu;
    private boolean isPaused;
    private final Stage stage;

    public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, Stage stage) {
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
        this.stage = stage;

        this.score = 0; // Initialize score
        this.scoreDisplay = new Text(screenWidth - 150, 50, "Score: 0"); // Create the score display
        scoreDisplay.setStyle("-fx-font-size: 24px; -fx-fill: white;"); // Style the text
        getRoot().getChildren().add(scoreDisplay); // Add to the game root
        scoreDisplay.toFront(); // Bring to the front

        initializeTimeline();
        friendlyUnits.add(user);
    }

    // Abstract methods for subclasses to implement
    protected abstract void initializeFriendlyUnits();

    protected abstract void checkIfGameOver();

    protected abstract void spawnEnemyUnits();

    protected abstract LevelView instantiateLevelView();

    // Scene initialization
    public Scene initializeScene() {
        initializeBackground();
        initializeFriendlyUnits();
        levelView.showHeartDisplay();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P) { // Toggle pause with 'P'
                togglePause();
            }
        });

        return scene;
    }

    // Start game loop
    public void startGame() {
        background.requestFocus();
        timeline.play();
    }

    // Notify observers to transition to the next level
    public void goToNextLevel(String levelName) {
        timeline.stop(); // Stop the game loop before transitioning
        setChanged();
        notifyObservers(levelName);
    }

    protected void updateScene() {
        if (isPaused) return; // Skip updates if the game is paused

        spawnEnemyUnits();
        updateActors();
        generateEnemyFire();
        updateNumberOfEnemies();
        handleEnemyPenetration();
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        removeAllDestroyedActors();
        updateKillCount();
        updateLevelView();
        checkIfGameOver();
    }

    // Timeline setup
    private void initializeTimeline() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
        timeline.getKeyFrames().add(gameLoop);
    }

    // Initialize background and controls
    private void initializeBackground() {
        background.setFocusTraversable(true);
        background.setFitHeight(screenHeight);
        background.setFitWidth(screenWidth);

        background.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP) user.moveUp();
            if (kc == KeyCode.DOWN) user.moveDown();
            if (kc == KeyCode.SPACE) fireProjectile();
        });

        background.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
        });

        root.getChildren().add(background);
    }

    // Fire user projectile
    private void fireProjectile() {
        ActiveActorDestructible projectile = user.fireProjectile();
        if (projectile != null) {
            root.getChildren().add(projectile);
            userProjectiles.add(projectile);
        }
    }

    // Generate enemy fire
    private void generateEnemyFire() {
        enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
    }

    // Spawn enemy projectile
    private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
            root.getChildren().add(projectile);
            enemyProjectiles.add(projectile);
        }
    }

    // Update all actors
    private void updateActors() {
        friendlyUnits.forEach(ActiveActorDestructible::updateActor);
        enemyUnits.forEach(ActiveActorDestructible::updateActor);
        userProjectiles.forEach(ActiveActorDestructible::updateActor);
        enemyProjectiles.forEach(ActiveActorDestructible::updateActor);
    }

    // Remove destroyed actors
    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits);
        removeDestroyedActors(enemyUnits);
        removeDestroyedActors(userProjectiles);
        removeDestroyedActors(enemyProjectiles);
    }

    private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        root.getChildren().removeAll(destroyedActors);
        actors.removeAll(destroyedActors);
    }

    // Handle collisions
    private void handlePlaneCollisions() {
        handleCollisions(friendlyUnits, enemyUnits);
    }

    private void handleUserProjectileCollisions() {
        handleCollisions(userProjectiles, enemyUnits);
    }

    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors1) {
            for (ActiveActorDestructible otherActor : actors2) {
                if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                    actor.takeDamage();
                    otherActor.takeDamage();
                }
            }
        }
    }

    // Handle enemy penetration
    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage();
                enemy.destroy();
            }
        }
    }

    // Update level view
    private void updateLevelView() {
        levelView.removeHearts(user.getHealth());
    }

    private void updateScoreDisplay() {
        scoreDisplay.setText("Score: " + score); // Update the score text
        updateScoreDisplayPosition();
        scoreDisplay.toFront(); // Ensure it's on top
    }

    // Update kill count
    private void updateKillCount() {
        int kills = currentNumberOfEnemies - enemyUnits.size();
        if (kills > 0) {
            for (int i = 0; i < kills; i++) {
                score += 100; // Increment score for each kill
                getUser().incrementKillCount();
            }
            currentNumberOfEnemies = enemyUnits.size(); // Update enemy count
            updateScoreDisplay(); // Refresh score on UI
        }
    }

    private void updateScoreDisplayPosition() {
        scoreDisplay.setX(screenWidth - 150); // Dynamically adjust x-coordinate for top-right
    }

    public int getScore() {
        return score; // Getter for score
    }

    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth;
    }

    // Game outcomes
    protected void winGame() {
        timeline.stop();
        levelView.showWinImage();
    }

    protected void loseGame() {
        timeline.stop();
        levelView.showGameOverImage();
    }

    // Utility methods for subclasses
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

    // Pause functionality
    private void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        isPaused = true;
        timeline.stop();

        pauseMenu = new PauseMenu(stage, this::resumeGame, this::goToMainMenu, this::quitGame);
        getRoot().getChildren().add(pauseMenu.getRoot());
    }

    private void resumeGame() {
        isPaused = false;
        timeline.play();

        if (pauseMenu != null) {
            getRoot().getChildren().remove(pauseMenu.getRoot());
        }
    }

    private void goToMainMenu() {
        timeline.stop();
        MainMenu mainMenu = new MainMenu();
        try {
            mainMenu.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void quitGame() {
        System.exit(0); // Close the game
    }
}
