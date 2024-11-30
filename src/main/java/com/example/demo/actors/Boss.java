// Boss.java
package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
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

/**
 * Represents the boss character in the game.
 * The Boss can shoot projectiles and summon a shield.
 */
public class Boss extends ActiveActor {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FIRE_RATE = 1.0;
    private static final int IMAGE_HEIGHT = 100;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int MAX_HEALTH = 500;

    private final List<Integer> movePattern;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;

    private ProgressBar healthBar;
    private static final int HEALTH_BAR_WIDTH = 150; // 根据需要调整宽度
    private static final int HEALTH_BAR_HEIGHT = 15; // 根据需要调整高度

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;

    private Shield shield;
    private Timeline shieldCheckTimeline;
    private LevelParent level;

    private boolean isSummoningShield = false;

    /**
     * Constructs a Boss with specified root and level.
     *
     * @param root  the root group.
     * @param level the current level.
     */
    public Boss(Group root, LevelParent level) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
        this.level = level;
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        initializeMovePattern();

        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 3, IMAGE_HEIGHT);

        // 初始化血条
        initializeHealthBar();

        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, 0, PROJECTILE_Y_POSITION_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
        initializeShieldCheck();
    }

    private void initializeHealthBar() {
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setStyle("-fx-accent: red;");

        // 将血条添加为 Boss 的子节点
        this.getChildren().add(healthBar);
        healthBar.toFront();

        // 设置血条的位置，使其位于 Boss 的头顶上
        double imageWidth = getCollisionComponent().getHitboxWidth();
        healthBar.setLayoutX((imageWidth - HEALTH_BAR_WIDTH) / 2);
        healthBar.setLayoutY(-HEALTH_BAR_HEIGHT); // 调整偏移量以控制血条与 Boss 的距离

        healthBar.setVisible(getCurrentHealth() > 0);
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getBossProjectilePool());
        }

        if (shield != null && !shield.isDestroyed()) {
            double shieldX = getCollisionComponent().getHitboxX();
            double shieldY = getCollisionComponent().getHitboxY() + (getCollisionComponent().getHitboxHeight() / 2) - (Shield.SHIELD_SIZE / 2);
            shield.setLayoutX(shieldX);
            shield.setLayoutY(shieldY);
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

    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    // 其余代码保持不变

    private void initializeShieldCheck() {
        shieldCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(10.0), e -> {
            if (shield == null || shield.isDestroyed()) {
                summonShield();
            }
        }));
        shieldCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        shieldCheckTimeline.play();
    }

    private void summonShield() {
        if (isSummoningShield) return;
        isSummoningShield = true;

        Platform.runLater(() -> {
            double shieldX = getCollisionComponent().getHitboxX();
            double shieldY = getCollisionComponent().getHitboxY() + (getCollisionComponent().getHitboxHeight() / 2) - (Shield.SHIELD_SIZE / 2);
            shield = new Shield(shieldX, shieldY);
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
            super.destroy();
            if (shieldCheckTimeline != null) shieldCheckTimeline.stop();
            if (shield != null && !shield.isDestroyed()) shield.destroy();
            double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth();
            double y = getCollisionComponent().getHitboxY();
            animationComponent.playExplosion(x, y, 2.5);
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
