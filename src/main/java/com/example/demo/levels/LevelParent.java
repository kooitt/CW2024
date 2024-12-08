package com.example.demo.levels;

import com.example.demo.actors.Actor.*;
import com.example.demo.actors.Projectile.BulletFactory;
import com.example.demo.actors.Projectile.Projectile;
import com.example.demo.components.*;
import com.example.demo.controller.Controller;
import com.example.demo.utils.*;
import com.example.demo.views.LevelView;
import com.example.demo.ui.SettingsPage;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import com.example.demo.interfaces.LevelChangeListener;
import javafx.util.Duration;

import java.util.*;

public abstract class LevelParent {

    private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
    private static final int MILLISECOND_DELAY = 30;
    private final double screenHeight;
    private final double screenWidth;
    private final double enemyMaximumYPosition;

    private final Group root;
    private final Timeline timeline;
    protected final UserPlane user;
    private Scene scene;
    private ImageView background;

    protected final List<Actor> friendlyUnits = new ArrayList<>();
    protected final List<Actor> enemyUnits = new ArrayList<>();
    protected final List<Actor> userProjectiles = new ArrayList<>();
    protected final List<Actor> enemyProjectiles = new ArrayList<>();
    protected final List<Actor> shields = new ArrayList<>();
    protected final List<Actor> powerUps = new ArrayList<>();
    private List<LevelChangeListener> levelChangeListeners = new ArrayList<>();
    private List<Timeline> additionalTimelines = new ArrayList<>();
    protected Controller controller;
    protected boolean isInputEnabled = true;

    private int currentNumberOfEnemies;
    private LevelView levelView;

    private Set<KeyCode> activeKeys = new HashSet<>();

    private ObjectPool<Projectile> userProjectilePool;
    private ObjectPool<Projectile> enemyProjectilePool;
    private ObjectPool<Projectile> bossProjectilePool;
    private ObjectPool<Projectile> bossTwoProjectilePool;

    private AnimationComponent animationComponent;
    private long lastUpdateTime;
    public Button pauseButton;
    private StackPane pauseOverlay;
    private boolean isGamePaused = false;
    private SettingsPage settingsPageForPause;

    private int heartsToDisplay = 5;

    public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, Controller controller) {
        this.controller = controller;
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.timeline = new Timeline();
        this.animationComponent = new AnimationComponent(root);
        this.user = new UserPlane(heartsToDisplay);
        this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
        this.levelView = NewLevelView(heartsToDisplay);

        initializeTimeline();
        friendlyUnits.add(user);

        userProjectilePool = new ObjectPool<>(new BulletFactory("user"));
        enemyProjectilePool = new ObjectPool<>(new BulletFactory("enemy"));
        bossProjectilePool = new ObjectPool<>(new BulletFactory("boss"));
        bossTwoProjectilePool = new ObjectPool<>(new BulletFactory("bossTwo"));

        lastUpdateTime = System.nanoTime();
    }

    protected abstract void initializeFriendlyUnits();

    protected abstract void checkIfGameOver();

    protected abstract void spawnEnemyUnits();

    public Scene initializeScene() {
        initializeBackground();
        initializeFriendlyUnits();
        levelView.showHeartDisplay();
        user.setAnimationComponent(animationComponent);
        pauseButton = new Button("Pause");
        pauseButton.setStyle("-fx-font-size: 18px; -fx-background-color: transparent; -fx-text-fill: white;");
        pauseButton.setOnAction(e -> showPauseMenu());

        return scene;
    }

    protected LevelView NewLevelView(int hearts) {
        return new LevelView(root, hearts);
    }

    public void addTimeline(Timeline timeline) {
        additionalTimelines.add(timeline);
    }

    private void pauseAllGameTimelines() {
        timeline.pause();
        additionalTimelines.forEach(Timeline::pause);
        SoundComponent.pauseCurrentLevelSound();
    }

    private void resumeAllGameTimelines() {
        timeline.play();
        additionalTimelines.forEach(Timeline::play);
        SoundComponent.resumeCurrentLevelSound();
    }

    public void addLevelChangeListener(LevelChangeListener listener) {
        if (listener != null && !levelChangeListeners.contains(listener)) {
            levelChangeListeners.add(listener);
        }
    }

    public void removeLevelChangeListener(LevelChangeListener listener) {
        levelChangeListeners.remove(listener);
    }

    protected void notifyLevelChange(String nextLevelName) {
        for (LevelChangeListener listener : new ArrayList<>(levelChangeListeners)) {
            listener.onLevelChange(nextLevelName);
        }
    }

    public void setSettingsPageForPause(SettingsPage sp) {
        this.settingsPageForPause = sp;
    }

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

    protected boolean userIsDestroyed() {
        return user.isDestroyed();
    }

    protected double getEnemyMaximumYPosition() {
        return enemyMaximumYPosition;
    }

    protected double getScreenWidth() {
        return screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void addEnemyUnit(Actor enemy) {
        if (enemy instanceof Shield && !shields.contains(enemy)) {
            shields.add(enemy);
        } else if (!(enemy instanceof Shield) && !enemyUnits.contains(enemy)) {
            enemyUnits.add(enemy);
        }
        root.getChildren().add(enemy);
    }

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

    private void destroyActors(List<Actor> actors) {
        for (Actor actor : actors) {
            actor.destroy();
        }
    }

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

    public void goToNextLevel(String levelName) {
        notifyLevelChange(levelName);
    }

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
        background.setFitWidth(screenWidth);
        background.setFitHeight(screenHeight);
        background.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        background.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
        root.getChildren().add(background);
    }

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

    private void updateActors(double deltaTime) {
        friendlyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
        if (!isInputEnabled) return;
        enemyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
        shields.forEach(shield -> shield.updateActor(deltaTime, this));
        userProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
        enemyProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
        powerUps.forEach(powerUp -> powerUp.updateActor(deltaTime, this));
    }

    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits);
        removeDestroyedActors(enemyUnits, enemyProjectilePool, bossProjectilePool);
        removeDestroyedActors(shields);
        removeDestroyedActors(userProjectiles, userProjectilePool);
        removeDestroyedActors(enemyProjectiles, enemyProjectilePool, bossProjectilePool);
        removeDestroyedActors(powerUps);
    }

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

    private void removeDestroyedActors(List<Actor> actors) {
        actors.removeIf(actor -> {
            if (actor.isDestroyed()) {
                root.getChildren().remove(actor);
                return true;
            }
            return false;
        });
    }

    private void handleCollisions() {
        handleCollisions(userProjectiles, shields);
        handleCollisions(enemyProjectiles, friendlyUnits);
        handleCollisions(enemyUnits, Collections.singletonList(user));
        handlePickupCollisions(Collections.singletonList(user), powerUps);
        handleCollisions(userProjectiles, enemyUnits);
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

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

    private void handleEnemyPenetration() {
        for (Actor enemy : enemyUnits) {
            if (Math.abs(enemy.getTranslateX()) > screenWidth) {
                user.takeDamage(1);
                enemy.destroy();
            }
        }
    }

    private void updateLevelView() {
        levelView.removeHearts(user.getCurrentHealth());
    }

    private void updateKillCount() {
        for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
            user.incrementKillCount();
        }
    }

    protected void winGame() {
        isInputEnabled = false;
        timeline.stop();
        levelView.showWinImage();
        SoundComponent.stopAllSound();
        if (root.getChildren().contains(pauseButton)) {
            root.getChildren().remove(pauseButton);
        }
        Button returnButton = createStyledButton("Return to Main Menu", controller::returnToMainMenu);
        returnButton.setLayoutX(screenWidth / 2 - 150);
        returnButton.setLayoutY(screenHeight / 2 + 100);
        root.getChildren().add(returnButton);
    }

    protected void loseGame() {
        isInputEnabled = false;
        timeline.stop();
        levelView.showGameOverImage();
        SoundComponent.stopAllSound();
        SoundComponent.playGameoverSound();

        if (root.getChildren().contains(pauseButton)) {
            root.getChildren().remove(pauseButton);
        }

        Button returnButton = createStyledButton("Return to Main Menu", controller::returnToMainMenu);
        returnButton.setLayoutX(screenWidth / 2 - 150);
        returnButton.setLayoutY(screenHeight / 2 + 100);
        root.getChildren().add(returnButton);
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        String style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2;";
        String hoverStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> action.run());
        return button;
    }

    public Controller getController() {
        return controller;
    }

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
        levelView = null;
        controller = null;
        scene = null;
        background = null;
        System.gc();
    }

    public UserPlane getUser() {
        return user;
    }

    public Group getRoot() {
        return root;
    }

    protected int getCurrentNumberOfEnemies() {
        return enemyUnits.size() + shields.size();
    }

    private void updateNumberOfEnemies() {
        currentNumberOfEnemies = enemyUnits.size() + shields.size();
    }

    private void removeProjectilesOutOfBounds() {
        removeOutOfBounds(userProjectiles, screenWidth, screenHeight);
        removeOutOfBounds(enemyProjectiles, screenWidth, screenHeight);
    }

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

    public void addProjectile(Projectile projectile, Actor owner) {
        if (owner instanceof UserPlane) {
            userProjectiles.add(projectile);
        } else {
            enemyProjectiles.add(projectile);
        }
    }

    public ObjectPool<Projectile> getUserProjectilePool() {
        return userProjectilePool;
    }

    public ObjectPool<Projectile> getEnemyProjectilePool() {
        return enemyProjectilePool;
    }

    public ObjectPool<Projectile> getBossProjectilePool() {
        return bossProjectilePool;
    }

    public ObjectPool<Projectile> getBossTwoProjectilePool() {
        return bossTwoProjectilePool;
    }

    public LevelView getLevelView() {
        return levelView;
    }
}