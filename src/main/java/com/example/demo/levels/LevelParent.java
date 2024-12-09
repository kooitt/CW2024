package com.example.demo.levels;

import com.example.demo.actors.Actor.*;
import com.example.demo.actors.Projectile.BulletFactory;
import com.example.demo.actors.Projectile.Projectile;
import com.example.demo.components.*;
import com.example.demo.controller.Controller;
import com.example.demo.ui.GameOverImage;
import com.example.demo.ui.HeartDisplay;
import com.example.demo.ui.WinImage;
import com.example.demo.utils.*;
import com.example.demo.ui.SettingsPage;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import com.example.demo.interfaces.LevelChangeListener;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;

/**
 * Abstract class representing a generic game level in the application.
 * It manages game entities, user input, collision detection, and game state transitions.
 */
public abstract class LevelParent {

    /**
     * Adjustment value for screen height to determine enemy spawn positions.
     */
    private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;

    /**
     * Delay in milliseconds between each game loop iteration.
     */
    private static final int MILLISECOND_DELAY = 30;

    /**
     * Height of the game screen.
     */
    private final double screenHeight;

    /**
     * Width of the game screen.
     */
    private final double screenWidth;

    /**
     * Maximum Y position where enemies can spawn.
     */
    private final double enemyMaximumYPosition;

    /**
     * Root node of the JavaFX scene graph for this level.
     */
    private final Group root;

    /**
     * Timeline object handling the game loop and animations.
     */
    private final Timeline timeline;

    /**
     * The user's plane in the game.
     */
    protected final UserPlane user;

    /**
     * JavaFX Scene associated with this level.
     */
    private Scene scene;

    /**
     * Background image of the level.
     */
    private ImageView background;

    /**
     * List of friendly units (e.g., user-controlled entities) present in the level.
     */
    protected final List<Actor> friendlyUnits = new ArrayList<>();

    /**
     * List of enemy units present in the level.
     */
    protected final List<Actor> enemyUnits = new ArrayList<>();

    /**
     * List of projectiles fired by the user.
     */
    protected final List<Actor> userProjectiles = new ArrayList<>();

    /**
     * List of projectiles fired by enemies.
     */
    protected final List<Actor> enemyProjectiles = new ArrayList<>();

    /**
     * List of shields present in the level.
     */
    protected final List<Actor> shields = new ArrayList<>();

    /**
     * List of power-ups available in the level.
     */
    protected final List<Actor> powerUps = new ArrayList<>();

    /**
     * Listeners that respond to level change events.
     */
    private final List<LevelChangeListener> levelChangeListeners = new ArrayList<>();

    /**
     * Additional timelines for handling various animations or game events.
     */
    private final List<Timeline> additionalTimelines = new ArrayList<>();

    /**
     * Controller managing the overall game state and transitions.
     */
    protected Controller controller;

    /**
     * Flag indicating whether user input is currently enabled.
     */
    protected boolean isInputEnabled = true;

    /**
     * Current number of enemies in the level.
     */
    private int currentNumberOfEnemies;

    /**
     * Set of currently active keys pressed by the user.
     */
    private final Set<KeyCode> activeKeys = new HashSet<>();

    /**
     * Object pool for managing user projectiles efficiently.
     */
    private ObjectPool<Projectile> userProjectilePool;

    /**
     * Object pool for managing enemy projectiles efficiently.
     */
    private ObjectPool<Projectile> enemyProjectilePool;

    /**
     * Object pool for managing boss projectiles efficiently.
     */
    private ObjectPool<Projectile> bossProjectilePool;

    /**
     * Object pool for managing boss two projectiles efficiently.
     */
    private ObjectPool<Projectile> bossTwoProjectilePool;

    /**
     * Component handling animations within the level.
     */
    private AnimationComponent animationComponent;

    /**
     * Timestamp of the last update cycle.
     */
    private long lastUpdateTime;

    /**
     * Pause button displayed during gameplay to access the pause menu.
     */
    public Button pauseButton;

    /**
     * Flag indicating whether the game is currently paused.
     */
    private boolean isGamePaused = false;

    /**
     * Reference to the settings page displayed when the game is paused.
     */
    private SettingsPage settingsPageForPause;

    /**
     * Display managing the hearts (health) of the user.
     */
    protected final HeartDisplay heartDisplay;

    /**
     * Image displayed upon winning the level.
     */
    protected final WinImage winImage;

    /**
     * Image displayed upon losing the level.
     */
    protected final GameOverImage gameOverImage;

    /**
     * Label displaying the objective of the level.
     */
    protected Label objectiveLabel;

    /**
     * Constructs a new LevelParent instance with the specified parameters.
     *
     * @param backgroundImageName The path to the background image for the level.
     * @param screenHeight        The height of the game screen.
     * @param screenWidth         The width of the game screen.
     * @param controller          The controller managing the game state.
     */
    public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, Controller controller) {
        this.controller = controller;
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.timeline = new Timeline();
        this.animationComponent = new AnimationComponent(root);
        int heartsToDisplay = 5;
        this.user = new UserPlane(heartsToDisplay);
        // Initialize display components
        this.heartDisplay = NewHeartDisplay(root, 5, 25, heartsToDisplay); // x=5, y=25
        this.winImage = new WinImage(root);
        this.gameOverImage = new GameOverImage(root);

        this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;

        initializeTimeline();
        friendlyUnits.add(user);

        userProjectilePool = new ObjectPool<>(new BulletFactory("user"));
        enemyProjectilePool = new ObjectPool<>(new BulletFactory("enemy"));
        bossProjectilePool = new ObjectPool<>(new BulletFactory("boss"));
        bossTwoProjectilePool = new ObjectPool<>(new BulletFactory("bossTwo"));

        lastUpdateTime = System.nanoTime();
    }

    /**
     * Initializes the friendly units for the level.
     * Must be implemented by subclasses to add specific friendly entities.
     */
    protected abstract void initializeFriendlyUnits();

    /**
     * Retrieves the objective text for the level.
     *
     * @return The objective text.
     */
    protected abstract String getObjectiveText();

    /**
     * Checks the game state to determine if the game is over.
     * Must be implemented by subclasses to define specific game over conditions.
     */
    protected abstract void checkIfGameOver();

    /**
     * Spawns enemy units within the level.
     * Must be implemented by subclasses to define specific enemy spawning logic.
     */
    protected abstract void spawnEnemyUnits();

    /**
     * Creates a new HeartDisplay instance.
     *
     * @param root            The root group to which the HeartDisplay will be added.
     * @param xPosition       The X position of the HeartDisplay.
     * @param yPosition       The Y position of the HeartDisplay.
     * @param heartsToDisplay The initial number of hearts to display.
     * @return A new HeartDisplay instance.
     */
    protected HeartDisplay NewHeartDisplay(Group root, double xPosition, double yPosition, int heartsToDisplay) {
        return new HeartDisplay(root, xPosition, yPosition, heartsToDisplay);
    }

    /**
     * Initializes the scene by setting up the background, friendly units, and UI elements.
     *
     * @return The initialized Scene object.
     */
    public Scene initializeScene() {
        initializeBackground();
        initializeFriendlyUnits();
        heartDisplay.showHeartDisplay(); // Display the heart display
        user.setAnimationComponent(animationComponent);
        pauseButton = new Button("Pause");
        pauseButton.setStyle("-fx-font-size: 18px; -fx-background-color: transparent; -fx-text-fill: white;");
        pauseButton.setOnAction(e -> showPauseMenu());
        initializeObjectiveLabel(); // Initialize the objective display
        return scene;
    }

    /**
     * Updates the objective label with new text.
     *
     * @param newText The new objective text to display.
     */
    protected void updateObjectiveLabel(String newText) {
        if (objectiveLabel != null) {
            objectiveLabel.setText(newText);
        }
    }

    /**
     * Initializes the objective label for the level.
     * Sets up the label's appearance and position within the scene.
     */
    protected void initializeObjectiveLabel() {
        StackPane container = new StackPane();
        container.setPrefSize(screenWidth, screenHeight); // Set container size to match screen
        container.setAlignment(Pos.BOTTOM_LEFT); // Align to bottom-left corner

        objectiveLabel = new Label(getObjectiveText());
        objectiveLabel.setFont(new Font("Arial", 20));
        objectiveLabel.setTextFill(Color.WHITE);
        objectiveLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10; -fx-border-color: white; -fx-border-width: 1;");

        // Add margin to position the label slightly above the bottom
        StackPane.setMargin(objectiveLabel, new Insets(0, 0, 50, 0)); // 50px space from bottom

        container.getChildren().add(objectiveLabel); // Add label to container
        root.getChildren().add(container); // Add container to root
    }

    /**
     * Displays the win image when the user wins the level.
     */
    protected void showWinImage() {
        winImage.showWinImage();
    }

    /**
     * Displays the game over image when the user loses the level.
     */
    protected void showGameOverImage() {
        gameOverImage.showGameOverImage();
    }

    /**
     * Removes a specified number of hearts from the heart display.
     *
     * @param heartsRemaining The number of hearts remaining after removal.
     */
    protected void removeHearts(int heartsRemaining) {
        heartDisplay.removeHearts(heartsRemaining);
    }

    /**
     * Adds a specified number of hearts to the heart display.
     *
     * @param heartsToAdd The number of hearts to add.
     */
    public void addHearts(int heartsToAdd) {
        heartDisplay.addHearts(heartsToAdd);
    }

    /**
     * Adds an additional timeline to manage animations or events.
     *
     * @param timeline The Timeline to be added.
     */
    public void addTimeline(Timeline timeline) {
        additionalTimelines.add(timeline);
    }

    /**
     * Pauses all game timelines and sounds.
     */
    private void pauseAllGameTimelines() {
        timeline.pause();
        additionalTimelines.forEach(Timeline::pause);
        SoundComponent.pauseCurrentLevelSound();
    }

    /**
     * Resumes all paused game timelines and sounds.
     */
    private void resumeAllGameTimelines() {
        timeline.play();
        additionalTimelines.forEach(Timeline::play);
        SoundComponent.resumeCurrentLevelSound();
    }

    /**
     * Adds a LevelChangeListener to respond to level change events.
     *
     * @param listener The LevelChangeListener to be added.
     */
    public void addLevelChangeListener(LevelChangeListener listener) {
        if (listener != null && !levelChangeListeners.contains(listener)) {
            levelChangeListeners.add(listener);
        }
    }

    /**
     * Notifies all registered LevelChangeListeners about a level change.
     *
     * @param nextLevelName The name of the next level to transition to.
     */
    protected void notifyLevelChange(String nextLevelName) {
        for (LevelChangeListener listener : new ArrayList<>(levelChangeListeners)) {
            listener.onLevelChange(nextLevelName);
        }
    }

    /**
     * Sets the SettingsPage to be displayed when the game is paused.
     *
     * @param sp The SettingsPage instance.
     */
    public void setSettingsPageForPause(SettingsPage sp) {
        this.settingsPageForPause = sp;
    }

    /**
     * Displays the pause menu, halting game input and animations.
     */
    private void showPauseMenu() {
        if (isGamePaused) return;

        pauseAllGameTimelines();
        isInputEnabled = false;
        isGamePaused = true;

        controller.getSettingsPage().refresh();
        controller.getSettingsPage().setBackAction(this::hidePauseMenu);

        if (controller.getSettingsPage().getRoot().getParent() != null) {
            ((Pane) controller.getSettingsPage().getRoot().getParent()).getChildren().remove(controller.getSettingsPage().getRoot());
        }

        root.getChildren().add(controller.getSettingsPage().getRoot());
        controller.getSettingsPage().getRoot().setVisible(true);
        controller.getSettingsPage().getRoot().toFront();
    }

    /**
     * Hides the pause menu and resumes game input and animations.
     */
    private void hidePauseMenu() {
        if (!isGamePaused) return;

        if (background != null) {
            background.requestFocus();
        }
        root.getChildren().remove(settingsPageForPause.getRoot());

        isGamePaused = false;
        isInputEnabled = true;
        resumeAllGameTimelines();

        background.requestFocus();
        activeKeys.clear();
        user.stopVerticalMovement();
        user.stopHorizontalMovement();
    }

    /**
     * Checks if the user's plane has been destroyed.
     *
     * @return True if the user is destroyed, false otherwise.
     */
    protected boolean userIsDestroyed() {
        return user.isDestroyed();
    }

    /**
     * Retrieves the maximum Y position where enemies can spawn.
     *
     * @return The maximum Y position.
     */
    protected double getEnemyMaximumYPosition() {
        return enemyMaximumYPosition;
    }

    /**
     * Retrieves the width of the game screen.
     *
     * @return The screen width.
     */
    protected double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Retrieves the height of the game screen.
     *
     * @return The screen height.
     */
    public double getScreenHeight() {
        return screenHeight;
    }

    /**
     * Adds an enemy unit to the level, managing its inclusion in relevant lists and the scene graph.
     *
     * @param enemy The enemy Actor to be added.
     */
    public void addEnemyUnit(Actor enemy) {
        if (enemy instanceof Shield && !shields.contains(enemy)) {
            shields.add(enemy);
        } else if (!(enemy instanceof Shield) && !enemyUnits.contains(enemy)) {
            enemyUnits.add(enemy);
        }
        root.getChildren().add(enemy);
    }

    /**
     * Clears all projectiles, power-ups, and enemy units from the level.
     */
    protected void clearAllProjectiles() {
        destroyActors(userProjectiles);
        destroyActors(enemyProjectiles);
        destroyActors(powerUps);
        destroyActors(enemyUnits);

        removeDestroyedActors(userProjectiles, userProjectilePool);
        removeDestroyedActors(enemyProjectiles, enemyProjectilePool, bossProjectilePool);
        removeDestroyedActors(powerUps);
        removeDestroyedActors(enemyUnits);
    }

    /**
     * Destroys all actors in the provided list by invoking their destroy method.
     *
     * @param actors The list of actors to be destroyed.
     */
    private void destroyActors(List<Actor> actors) {
        for (Actor actor : actors) {
            actor.destroy();
        }
    }

    /**
     * Starts the game by initiating user plane movement animations and enabling game input.
     */
    public void startGame() {
        isInputEnabled = false;

        double startX = -200.0;
        double overshootX = 160.0;
        double finalX = 130.0;

        user.setLayoutX(startX);

        KeyValue kv1 = new KeyValue(user.layoutXProperty(), overshootX, Interpolator.EASE_IN);
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000), kv1);

        KeyValue kv2 = new KeyValue(user.layoutXProperty(), finalX, Interpolator.EASE_BOTH);
        KeyFrame kf2 = new KeyFrame(Duration.millis(1500), event -> {
            isInputEnabled = true;
            background.requestFocus();
            timeline.play();

            pauseButton.setLayoutX(screenWidth - 100);
            pauseButton.setLayoutY(20);
            if (!userIsDestroyed()) {
                root.getChildren().add(pauseButton);
            }

        }, kv2);

        Timeline introTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(user.layoutXProperty(), startX)),
                kf1,
                kf2
        );

        introTimeline.play();
    }

    /**
     * Initiates the transition to the next level.
     *
     * @param levelName The fully qualified class name of the next level.
     */
    public void goToNextLevel(String levelName) {
        notifyLevelChange(levelName);
    }

    /**
     * Updates the game scene by processing input, spawning enemies, updating actors,
     * handling collisions, and checking game over conditions.
     */
    private void updateScene() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastUpdateTime) / 1e9;
        lastUpdateTime = currentTime;

        processInput();
        spawnEnemyUnits();
        updateActors(deltaTime);
        updateNumberOfEnemies();
        handleEnemyPenetration();
        handleCollisions();
        removeProjectilesOutOfBounds();
        removeAllDestroyedActors();
        updateKillCount();
        updateHeartDisplay();
        checkIfGameOver();
    }

    /**
     * Initializes the main game timeline and sets up the game loop.
     */
    private void initializeTimeline() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
        timeline.getKeyFrames().add(gameLoop);
    }

    /**
     * Initializes the background image and sets up key event handlers.
     */
    private void initializeBackground() {
        background.setFocusTraversable(true);
        background.setFitWidth(screenWidth);
        background.setFitHeight(screenHeight);
        background.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        background.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
        root.getChildren().add(background);
    }

    /**
     * Processes user input by checking active keys and moving the user plane accordingly.
     */
    private void processInput() {
        if (!isInputEnabled) {
            user.stopVerticalMovement();
            user.stopHorizontalMovement();
            return;
        }

        KeyBindings keys = KeyBindings.getInstance();
        boolean up = activeKeys.contains(keys.getUpKey());
        boolean down = activeKeys.contains(keys.getDownKey());
        boolean left = activeKeys.contains(keys.getLeftKey());
        boolean right = activeKeys.contains(keys.getRightKey());

        if (up && !down) user.moveUp();
        else if (down && !up) user.moveDown();
        else user.stopVerticalMovement();

        if (left && !right) user.moveLeft();
        else if (right && !left) user.moveRight();
        else user.stopHorizontalMovement();
    }

    /**
     * Updates all actors (friendly units, enemy units, projectiles, shields, power-ups) based on deltaTime.
     *
     * @param deltaTime The time elapsed since the last update in seconds.
     */
    private void updateActors(double deltaTime) {
        friendlyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
        if (!isInputEnabled) return;
        enemyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
        shields.forEach(shield -> shield.updateActor(deltaTime, this));
        userProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
        enemyProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
        powerUps.forEach(powerUp -> powerUp.updateActor(deltaTime, this));
    }

    /**
     * Removes all destroyed actors from their respective lists and the scene graph.
     */
    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits);
        removeDestroyedActors(enemyUnits, enemyProjectilePool, bossProjectilePool);
        removeDestroyedActors(shields);
        removeDestroyedActors(userProjectiles, userProjectilePool);
        removeDestroyedActors(enemyProjectiles, enemyProjectilePool, bossProjectilePool);
        removeDestroyedActors(powerUps);
    }

    /**
     * Removes destroyed actors from the provided list and releases projectiles back to their pools.
     *
     * @param actors The list of actors to check and remove.
     * @param pools  Optional object pools to release projectiles back to.
     */
    @SafeVarargs
    private void removeDestroyedActors(List<Actor> actors, ObjectPool<Projectile>... pools) {
        Iterator<Actor> iterator = actors.iterator();
        while (iterator.hasNext()) {
            Actor actor = iterator.next();
            if (actor.isDestroyed()) {
                if (actor instanceof Boss && !((Boss) actor).isReadyToRemove) continue;
                root.getChildren().remove(actor);
                iterator.remove();
                if (actor instanceof Projectile) {
                    for (ObjectPool<Projectile> pool : pools) {
                        pool.release((Projectile) actor);
                    }
                }
            }
        }
    }

    /**
     * Removes destroyed actors from the provided list without interacting with object pools.
     *
     * @param actors The list of actors to check and remove.
     */
    private void removeDestroyedActors(List<Actor> actors) {
        actors.removeIf(actor -> {
            if (actor.isDestroyed()) {
                root.getChildren().remove(actor);
                return true;
            }
            return false;
        });
    }

    /**
     * Handles collisions between various actors such as projectiles, shields, and power-ups.
     */
    private void handleCollisions() {
        handleCollisions(userProjectiles, shields);
        handleCollisions(enemyProjectiles, friendlyUnits);
        handleCollisions(enemyUnits, Collections.singletonList(user));
        handlePickupCollisions(Collections.singletonList(user), powerUps);
        handleCollisions(userProjectiles, enemyUnits);
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    /**
     * Handles collisions between actors1 and power-ups, triggering pickup actions.
     *
     * @param actors1  The first list of actors involved in collisions.
     * @param powerUps The list of power-up actors involved in collisions.
     */
    private void handlePickupCollisions(List<Actor> actors1, List<Actor> powerUps) {
        for (Actor actor1 : actors1) {
            for (Actor powerUp : powerUps) {
                if (actor1.isDestroyed() || powerUp.isDestroyed()) continue;
                if (actor1.getCollisionComponent().checkCollision(powerUp.getCollisionComponent())) {
                    if (powerUp instanceof ActorLevelUp) {
                        ((ActorLevelUp) powerUp).onPickup(this);
                    } else if (powerUp instanceof HeartItem) {
                        ((HeartItem) powerUp).onPickup(this);
                    }
                }
            }
        }
    }

    /**
     * Handles collisions between actors1 and actors2, applying damage as necessary.
     *
     * @param actors1 The first list of actors involved in collisions.
     * @param actors2 The second list of actors involved in collisions.
     */
    private void handleCollisions(List<Actor> actors1, List<Actor> actors2) {
        for (Actor actor1 : actors1) {
            for (Actor actor2 : actors2) {
                if (actor1.isDestroyed() || actor2.isDestroyed()) continue;
                if (actor1.getCollisionComponent().checkCollision(actor2.getCollisionComponent())) {
                    actor1.takeDamage(1);
                    actor2.takeDamage(1);
                }
            }
        }
    }

    /**
     * Handles scenarios where enemies penetrate past the screen boundaries,
     * resulting in damage to the user.
     */
    private void handleEnemyPenetration() {
        for (Actor enemy : enemyUnits) {
            if (Math.abs(enemy.getTranslateX()) > screenWidth) {
                user.takeDamage(1);
                enemy.destroy();
            }
        }
    }

    /**
     * Updates the kill count based on the number of enemies eliminated.
     */
    protected void updateKillCount() {
        for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
            user.incrementKillCount();
        }
    }

    /**
     * Wins the game by displaying the win image, stopping animations, and handling cleanup.
     */
    protected void winGame() {
        isInputEnabled = false;
        timeline.stop();
        showWinImage(); // Calls WinImage's showWinImage method
        SoundComponent.stopAllSound();
        root.getChildren().remove(pauseButton);
        Button returnButton = createStyledButton(controller::returnToMainMenu);
        returnButton.setLayoutX(screenWidth / 2 - 150);
        returnButton.setLayoutY(screenHeight / 2 + 100);
        root.getChildren().add(returnButton);
    }

    /**
     * Loses the game by displaying the game over image, stopping animations, and handling cleanup.
     */
    protected void loseGame() {
        isInputEnabled = false;
        timeline.stop();
        showGameOverImage(); // Calls GameOverImage's showGameOverImage method
        SoundComponent.stopAllSound();
        SoundComponent.playGameoverSound();

        root.getChildren().remove(pauseButton);

        Button returnButton = createStyledButton(controller::returnToMainMenu);
        returnButton.setLayoutX(screenWidth / 2 - 150);
        returnButton.setLayoutY(screenHeight / 2 + 100);
        root.getChildren().add(returnButton);
    }

    /**
     * Updates the heart display based on the user's current health.
     */
    protected void updateHeartDisplay() {
        removeHearts(user.getCurrentHealth());
    }

    /**
     * Creates a styled button with hover effects.
     *
     * @param action The action to be performed when the button is clicked.
     * @return A styled Button instance.
     */
    private Button createStyledButton(Runnable action) {
        Button button = new Button("Return to Main Menu");
        String style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2;";
        String hoverStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2px;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Retrieves the controller managing the game.
     *
     * @return The Controller instance.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Cleans up resources by stopping timelines, clearing lists, and removing nodes from the scene graph.
     * It also nullifies references to aid garbage collection.
     */
    public void cleanUp() {
        if (timeline != null) {
            timeline.stop();
            timeline.getKeyFrames().clear();
        }
        for (Timeline t : additionalTimelines) {
            t.stop();
            t.getKeyFrames().clear();
        }

        additionalTimelines.clear();
        root.getChildren().clear();
        friendlyUnits.clear();
        enemyUnits.clear();
        userProjectiles.clear();
        enemyProjectiles.clear();
        shields.clear();
        powerUps.clear();
        if (user != null) {
            user.stopShooting();
            user.setHealthComponent(null);
        }
        SoundComponent.stopAllSound();
        userProjectilePool = null;
        enemyProjectilePool = null;
        bossProjectilePool = null;
        bossTwoProjectilePool = null;
        animationComponent = null;
        controller = null;
        scene = null;
        background = null;
        System.gc();
    }

    /**
     * Retrieves the user's plane.
     *
     * @return The UserPlane instance.
     */
    public UserPlane getUser() {
        return user;
    }

    /**
     * Retrieves the root group of the scene graph.
     *
     * @return The root Group.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Gets the current number of enemies (including shields) in the level.
     *
     * @return The total number of enemies.
     */
    protected int getCurrentNumberOfEnemies() {
        return enemyUnits.size() + shields.size();
    }

    /**
     * Updates the count of current enemies based on the size of enemy and shield lists.
     */
    private void updateNumberOfEnemies() {
        currentNumberOfEnemies = enemyUnits.size() + shields.size();
    }

    /**
     * Removes projectiles that have moved out of the game boundaries.
     *
     * @param projectiles The list of projectiles to check.
     * @param width       The width boundary of the game screen.
     * @param height      The height boundary of the game screen.
     */
    private void removeOutOfBounds(List<Actor> projectiles, double width, double height) {
        projectiles.removeIf(proj -> {
            CollisionComponent cc = proj.getCollisionComponent();
            double x = cc.getHitboxX();
            double y = cc.getHitboxY();
            if (x > width || x + cc.getHitboxWidth() < 0 || y > height || y + cc.getHitboxHeight() < 0) {
                proj.destroy();
                return true;
            }
            return false;
        });
    }

    /**
     * Removes projectiles that are out of bounds from both user and enemy projectile lists.
     */
    private void removeProjectilesOutOfBounds() {
        removeOutOfBounds(userProjectiles, screenWidth, screenHeight);
        removeOutOfBounds(enemyProjectiles, screenWidth, screenHeight);
    }

    /**
     * Adds a projectile to the appropriate projectile list based on the owner.
     *
     * @param projectile The Projectile to be added.
     * @param owner      The Actor who owns the projectile.
     */
    public void addProjectile(Projectile projectile, Actor owner) {
        if (owner instanceof UserPlane) {
            userProjectiles.add(projectile);
        } else {
            enemyProjectiles.add(projectile);
        }
    }

    /**
     * Retrieves the object pool for user projectiles.
     *
     * @return The ObjectPool managing user projectiles.
     */
    public ObjectPool<Projectile> getUserProjectilePool() {
        return userProjectilePool;
    }

    /**
     * Retrieves the object pool for enemy projectiles.
     *
     * @return The ObjectPool managing enemy projectiles.
     */
    public ObjectPool<Projectile> getEnemyProjectilePool() {
        return enemyProjectilePool;
    }

    /**
     * Retrieves the object pool for boss projectiles.
     *
     * @return The ObjectPool managing boss projectiles.
     */
    public ObjectPool<Projectile> getBossProjectilePool() {
        return bossProjectilePool;
    }

    /**
     * Retrieves the object pool for boss two projectiles.
     *
     * @return The ObjectPool managing boss two projectiles.
     */
    public ObjectPool<Projectile> getBossTwoProjectilePool() {
        return bossTwoProjectilePool;
    }
}
