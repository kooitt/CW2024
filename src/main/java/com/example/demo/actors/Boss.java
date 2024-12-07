package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.levels.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Boss extends ActiveActor {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400.0;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FIRE_RATE = 1.0;
    private static final int IMAGE_HEIGHT = 200;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int MAX_HEALTH = 800;
    private static final int HEALTH_BAR_WIDTH = 150;
    private static final int HEALTH_BAR_HEIGHT = 15;

    private final List<Integer> movePattern = new ArrayList<>();
    private int consecutiveMovesInSameDirection = 0;
    private int indexOfCurrentMove = 0;

    private ProgressBar healthBar;
    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;
    private Shield shield;
    private Timeline shieldCheckTimeline;
    private LevelParent level;
    private boolean isSummoningShield = false;
    public boolean isReadyToRemove = false;
    private Runnable onExplosionFinished;

    public Boss(Group root, LevelParent level) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
        this.level = level;
        initializeMovePattern();
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        initializeHealthBar();
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_POSITION_OFFSET, PROJECTILE_Y_POSITION_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
        initializeShieldCheck();
    }

    private void initializeHealthBar() {
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setStyle("-fx-accent: red; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 2;");
        this.getChildren().add(healthBar);
        healthBar.toFront();
        double imageWidth = getCollisionComponent().getHitboxWidth();
        healthBar.setLayoutX((imageWidth - HEALTH_BAR_WIDTH) / 2);
        healthBar.setLayoutY(-HEALTH_BAR_HEIGHT - 10);
        healthBar.setVisible(getCurrentHealth() > 0);
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getBossProjectilePool());
        }
        if (shield != null && !shield.isDestroyed()) {
            shield.setLayoutX(getCollisionComponent().getHitboxX() - 50);
            shield.setLayoutY(getCollisionComponent().getHitboxY());
        }
        shootingComponent.update(deltaTime, level);
    }

    @Override
    public void takeDamage(int damage) {
        if (shield == null || shield.isDestroyed()) {
            super.takeDamage(damage);
            updateHealthBar();
        }
    }

    public void setOnExplosionFinished(Runnable onExplosionFinished) {
        this.onExplosionFinished = onExplosionFinished;
    }

    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    private void initializeShieldCheck() {
        shieldCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(10.0), e -> {
            if (shield == null || shield.isDestroyed()) {
                summonShield();
            }
        }));
        shieldCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        shieldCheckTimeline.play();

        // 将boss的shieldCheckTimeline添加到level中，以便统一暂停/恢复
        level.addTimeline(shieldCheckTimeline);
    }


    private void summonShield() {
        if (isSummoningShield) return;
        isSummoningShield = true;
        Platform.runLater(() -> {
            shield = new Shield(getCollisionComponent().getHitboxX() - 50, getCollisionComponent().getHitboxY());
            level.addEnemyUnit(shield);
            isSummoningShield = false;
        });
    }

    private void initializeMovePattern() {
        for (int i = 0; i < 5; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(0);
        }
        Collections.shuffle(movePattern);
    }

    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection == 10) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) indexOfCurrentMove = 0;
        return currentMove;
    }

    @Override
    public void destroy() {
        if (!isDestroyed) {
            SoundComponent.stopLevel2Sound();
            super.destroy();
            if (shieldCheckTimeline != null) shieldCheckTimeline.stop();
            if (shield != null && !shield.isDestroyed()) shield.destroy();
            animationComponent.playExplosion(
                    getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth() / 2,
                    getCollisionComponent().getHitboxY() + getCollisionComponent().getHitboxHeight() / 2,
                    2.5,
                    () -> {
                        isReadyToRemove = true;
                        if (onExplosionFinished != null) {
                            onExplosionFinished.run();
                        }
                    }
            );
        }
    }

    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        int nextMove = getNextMove();
        getMovementComponent().setVelocity(0, nextMove);
        super.updatePosition();
        double currentPosition = getLayoutY() + getTranslateY();
        if (currentPosition < 10 || currentPosition > 475) {
            setTranslateY(initialTranslateY);
            getMovementComponent().setVelocity(0, 0);
        }
    }
}