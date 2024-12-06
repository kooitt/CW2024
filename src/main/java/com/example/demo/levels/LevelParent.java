package com.example.demo.levels;

import com.example.demo.actors.*;
import com.example.demo.components.AnimationComponent;
import com.example.demo.components.CollisionComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.projectiles.Projectile;
import com.example.demo.utils.ObjectPool;
import com.example.demo.projectiles.BulletFactory;
import com.example.demo.utils.KeyBindings;
import com.example.demo.views.LevelView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.*;

public abstract class LevelParent extends Observable {

    private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
    private static final int MILLISECOND_DELAY = 30;
    private final double screenHeight;
    private final double screenWidth;
    private final double enemyMaximumYPosition;

    private final Group root;
    private final Timeline timeline;
    protected final UserPlane user;
    private final Scene scene;
    private final ImageView background;

    protected final List<ActiveActor> friendlyUnits = new ArrayList<>();
    protected final List<ActiveActor> enemyUnits = new ArrayList<>();
    protected final List<ActiveActor> userProjectiles = new ArrayList<>();
    protected final List<ActiveActor> enemyProjectiles = new ArrayList<>();
    protected final List<ActiveActor> shields = new ArrayList<>();
    protected final List<ActiveActor> powerUps = new ArrayList<>();
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

    public LevelParent(String backgroundImageName, double screenHeight, double screenWidth) {
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.timeline = new Timeline();
        this.animationComponent = new AnimationComponent(root);
        this.user = new UserPlane(5);
        this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
        this.levelView = instantiateLevelView();

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

    protected abstract LevelView instantiateLevelView();

    public Scene initializeScene() {
        initializeBackground();
        initializeFriendlyUnits();
        levelView.showHeartDisplay();
        user.setAnimationComponent(animationComponent);
        return scene;
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

    public void addEnemyUnit(ActiveActor enemy) {
        if (enemy instanceof Shield && !shields.contains(enemy)) {
            shields.add(enemy);
        } else if (!(enemy instanceof Shield) && !enemyUnits.contains(enemy)) {
            enemyUnits.add(enemy);
        }
        root.getChildren().add(enemy);
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
        }, kv2);

        Timeline introTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(user.layoutXProperty(), startX)),
                kf1,
                kf2
        );

        introTimeline.play();
    }

    public void goToNextLevel(String levelName) {
        setChanged();
        notifyObservers(levelName);
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

    private void removeDestroyedActors(List<ActiveActor> actors, ObjectPool<Projectile>... pools) {
        Iterator<ActiveActor> iterator = actors.iterator();
        while (iterator.hasNext()) {
            ActiveActor actor = iterator.next();
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

    private void removeDestroyedActors(List<ActiveActor> actors) {
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
        handleCollisions(enemyUnits, Collections.singletonList(getUser()));
        handlePickupCollisions(Collections.singletonList(getUser()), powerUps);
        handleCollisions(userProjectiles, enemyUnits);
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handlePickupCollisions(List<ActiveActor> actors1, List<ActiveActor> powerUps) {
        for (ActiveActor actor1 : actors1) {
            for (ActiveActor powerUp : powerUps) {
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

    private void handleCollisions(List<ActiveActor> actors1, List<ActiveActor> actors2) {
        for (ActiveActor actor1 : actors1) {
            for (ActiveActor actor2 : actors2) {
                if (actor1.isDestroyed() || actor2.isDestroyed()) continue;
                if (actor1.getCollisionComponent().checkCollision(actor2.getCollisionComponent())) {
                    actor1.takeDamage(1);
                    actor2.takeDamage(1);
                }
            }
        }
    }

    private void handleEnemyPenetration() {
        for (ActiveActor enemy : enemyUnits) {
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
    }

    protected void loseGame() {
        isInputEnabled = false;
        timeline.stop();
        levelView.showGameOverImage();
        SoundComponent.stopAllSound();
        SoundComponent.playGameoverSound();
    }

    public void cleanUp() {
        timeline.stop();
        root.getChildren().clear();
        friendlyUnits.clear();
        enemyUnits.clear();
        shields.clear();
        userProjectiles.clear();
        enemyProjectiles.clear();
        powerUps.clear();
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

    private void removeOutOfBounds(List<ActiveActor> projectiles, double width, double height) {
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

    public void addProjectile(Projectile projectile, ActiveActor owner) {
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