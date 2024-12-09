package com.example.demo.actors.Actor;

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

/**
 * Represents the Boss enemy in the game.
 * The Boss has health, a shield, and a predefined move pattern.
 * It can shoot projectiles, summon shields, and play animations upon destruction.
 */
public class Boss extends Actor {

    // Constants for Boss properties
    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0; // Initial X position of the Boss
    private static final double INITIAL_Y_POSITION = 400.0; // Initial Y position of the Boss
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0; // X offset for projectiles
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0; // Y offset for projectiles
    private static final double FIRE_RATE = 1.0; // Fire rate of the Boss
    private static final int IMAGE_HEIGHT = 200; // Height of the Boss image
    private static final int VERTICAL_VELOCITY = 8; // Velocity for vertical movement
    private static final int MAX_HEALTH = 800; // Maximum health of the Boss
    private static final int HEALTH_BAR_WIDTH = 150; // Width of the health bar
    private static final int HEALTH_BAR_HEIGHT = 15; // Height of the health bar

    // Movement pattern for the Boss
    private final List<Integer> movePattern = new ArrayList<>();
    private int consecutiveMovesInSameDirection = 0; // Counter for consecutive moves
    private int indexOfCurrentMove = 0; // Index of the current move in the pattern

    private ProgressBar healthBar; // Health bar for visualizing Boss health
    private final ShootingComponent shootingComponent; // Shooting component for handling projectiles
    private final AnimationComponent animationComponent; // Animation component for explosion effects
    private Shield shield; // Shield object for protecting the Boss
    private Timeline shieldCheckTimeline; // Timeline for periodically checking the shield
    private final LevelParent level; // Reference to the parent level
    private boolean isSummoningShield = false; // Indicates if the Boss is currently summoning a shield
    public boolean isReadyToRemove = false; // Indicates if the Boss is ready to be removed from the game
    private Runnable onExplosionFinished; // Callback to execute after the Boss explosion animation is finished

    /**
     * Constructs a Boss instance.
     *
     * @param root  The root group to which the Boss belongs.
     * @param level The parent level of the Boss.
     */
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

    /**
     * Initializes the health bar for the Boss.
     */
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

    /**
     * Updates the Boss's state during each frame.
     *
     * @param deltaTime The time since the last update.
     * @param level     The parent level of the Boss.
     */
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

    /**
     * Inflicts damage on the Boss, bypassing the shield if it is active.
     *
     * @param damage The amount of damage to inflict.
     */
    @Override
    public void takeDamage(int damage) {
        if (shield == null || shield.isDestroyed()) {
            super.takeDamage(damage);
            updateHealthBar();
        }
    }

    /**
     * Sets a callback to be executed after the Boss explosion animation finishes.
     *
     * @param onExplosionFinished The callback to execute.
     */
    public void setOnExplosionFinished(Runnable onExplosionFinished) {
        this.onExplosionFinished = onExplosionFinished;
    }

    /**
     * Updates the health bar to reflect the Boss's current health.
     */
    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    /**
     * Stops the shield timeline.
     */
    public void stopShieldTimeline() {
        if (shieldCheckTimeline != null) {
            shieldCheckTimeline.stop();
        }
    }

    /**
     * Initializes the timeline for periodically checking the shield.
     */
    private void initializeShieldCheck() {
        shieldCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(10.0), e -> {
            if ((shield == null || shield.isDestroyed())) {
                summonShield();
            }
        }));
        shieldCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        shieldCheckTimeline.play();
        level.addTimeline(shieldCheckTimeline);
    }

    /**
     * Summons a shield to protect the Boss.
     */
    public void summonShield() {
        if (isSummoningShield) return;
        isSummoningShield = true;
        Platform.runLater(() -> {
            shield = new Shield(getCollisionComponent().getHitboxX() - 50, getCollisionComponent().getHitboxY());
            level.addEnemyUnit(shield);
            isSummoningShield = false;
        });
    }

    /**
     * Initializes the move pattern for the Boss.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < 5; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(0);
        }
        Collections.shuffle(movePattern);
    }

    /**
     * Retrieves the next move in the move pattern.
     *
     * @return The next move as an integer value.
     */
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

    /**
     * Destroys the Boss, playing an explosion animation and executing the callback.
     */
    @Override
    public void destroy() {
        if (!isDestroyed) {
            SoundComponent.stopLevel2Sound();
            super.destroy();
            if (shieldCheckTimeline != null) shieldCheckTimeline.stop();
            if (shield != null && !shield.isDestroyed()) shield.destroy();
            SoundComponent.playBossdownSound();
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

    /**
     * Updates the Boss's position based on its movement component and move pattern.
     */
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
