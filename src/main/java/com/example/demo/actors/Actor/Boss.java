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
 * The Boss has unique behavior such as moving in a predefined pattern,
 * summoning shields, firing projectiles, and playing an explosion animation when destroyed.
 */
public class Boss extends Actor {

    // Constants for Boss properties

    /** The name of the image representing the Boss. */
    private static final String IMAGE_NAME = "bossplane.png";

    /** The initial X-coordinate position of the Boss. */
    private static final double INITIAL_X_POSITION = 1000.0;

    /** The initial Y-coordinate position of the Boss. */
    private static final double INITIAL_Y_POSITION = 400.0;

    /** The offset for the X-coordinate of the Boss's projectiles. */
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;

    /** The offset for the Y-coordinate of the Boss's projectiles. */
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;

    /** The fire rate of the Boss, determining how often it fires projectiles. */
    private static final double FIRE_RATE = 1.0;

    /** The height of the Boss's image in pixels. */
    private static final int IMAGE_HEIGHT = 200;

    /** The vertical velocity of the Boss during movement. */
    private static final int VERTICAL_VELOCITY = 8;

    /** The maximum health of the Boss. */
    private static final int MAX_HEALTH = 800;

    /** The width of the health bar displayed above the Boss. */
    private static final int HEALTH_BAR_WIDTH = 150;

    /** The height of the health bar displayed above the Boss. */
    private static final int HEALTH_BAR_HEIGHT = 15;

    // Fields for movement and health tracking

    /** The movement pattern that dictates the Boss's vertical movement. */
    private final List<Integer> movePattern = new ArrayList<>();

    /** Counter to track consecutive moves in the same direction. */
    private int consecutiveMovesInSameDirection = 0;

    /** Index of the current move in the movement pattern. */
    private int indexOfCurrentMove = 0;

    /** The progress bar representing the Boss's current health. */
    private ProgressBar healthBar;

    // Components and related fields

    /** Component handling the Boss's projectile shooting behavior. */
    private final ShootingComponent shootingComponent;

    /** Component responsible for playing explosion animations. */
    private final AnimationComponent animationComponent;

    /** Shield object used to protect the Boss from damage. */
    private Shield shield;

    /** Timeline for periodically checking and managing the Boss's shield status. */
    private Timeline shieldCheckTimeline;

    /** Reference to the parent level containing the Boss. */
    private final LevelParent level;

    /** Indicates whether the Boss is currently summoning a shield. */
    private boolean isSummoningShield = false;

    /** Flag indicating if the Boss is ready to be removed from the game. */
    public boolean isReadyToRemove = false;

    /** Callback to execute after the Boss's explosion animation finishes. */
    private Runnable onExplosionFinished;

    /**
     * Constructs a Boss instance with a specific root group and parent level.
     *
     * @param root  The root group of the scene graph.
     * @param level The parent level containing the Boss.
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
     * Initializes the health bar for the Boss and configures its appearance and position.
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
     * Updates the Boss's state during each frame, including position, shield, and shooting behavior.
     *
     * @param deltaTime The time since the last update, in seconds.
     * @param level     The parent level containing the Boss.
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
     * Inflicts damage on the Boss if its shield is not active.
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
     * Sets a callback to execute when the Boss's explosion animation finishes.
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
     * Stops the timeline responsible for checking the Boss's shield status.
     */
    public void stopShieldTimeline() {
        if (shieldCheckTimeline != null) {
            shieldCheckTimeline.stop();
        }
    }

    /**
     * Initializes a timeline to periodically check and summon a shield for the Boss.
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
     * Summons a shield to protect the Boss if it is not already summoning one.
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
     * Initializes the movement pattern of the Boss, defining how it moves vertically.
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
     * Retrieves the next move from the Boss's movement pattern.
     * The pattern is reshuffled after consecutive moves in the same direction.
     *
     * @return The next vertical movement value.
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
     * Handles the destruction of the Boss, including playing explosion animations
     * and executing the destruction callback.
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
     * Updates the Boss's position based on its movement component and prevents it from
     * moving outside the allowable bounds.
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
