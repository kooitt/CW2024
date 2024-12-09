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

    /**
     * The filename of the Boss's image.
     */
    private static final String IMAGE_NAME = "bossplane.png";

    /**
     * The initial X position of the Boss on the screen.
     */
    private static final double INITIAL_X_POSITION = 1000.0;

    /**
     * The initial Y position of the Boss on the screen.
     */
    private static final double INITIAL_Y_POSITION = 400.0;

    /**
     * The X-axis offset for positioning projectiles relative to the Boss.
     */
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;

    /**
     * The Y-axis offset for positioning projectiles relative to the Boss.
     */
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;

    /**
     * The rate at which the Boss fires projectiles (shots per second).
     */
    private static final double FIRE_RATE = 1.0;

    /**
     * The height of the Boss's image.
     */
    private static final int IMAGE_HEIGHT = 200;

    /**
     * The vertical velocity multiplier for the Boss's movement.
     */
    private static final int VERTICAL_VELOCITY = 8;

    /**
     * The maximum health points of the Boss.
     */
    private static final int MAX_HEALTH = 800;

    /**
     * The width of the health bar displayed above the Boss.
     */
    private static final int HEALTH_BAR_WIDTH = 150;

    /**
     * The height of the health bar displayed above the Boss.
     */
    private static final int HEALTH_BAR_HEIGHT = 15;

    /**
     * The pattern of vertical movements the Boss will follow.
     */
    private final List<Integer> movePattern = new ArrayList<>();

    /**
     * Counter for tracking consecutive moves in the same direction.
     */
    private int consecutiveMovesInSameDirection = 0;

    /**
     * Index of the current move in the move pattern.
     */
    private int indexOfCurrentMove = 0;

    /**
     * Progress bar representing the Boss's current health.
     */
    private ProgressBar healthBar;

    /**
     * Component responsible for handling the Boss's shooting mechanics.
     */
    private final ShootingComponent shootingComponent;

    /**
     * Component responsible for handling animations such as explosions.
     */
    private final AnimationComponent animationComponent;

    /**
     * The shield object that can protect the Boss from damage.
     */
    private Shield shield;

    /**
     * Timeline that periodically checks if the Boss needs to summon a shield.
     */
    private Timeline shieldCheckTimeline;

    /**
     * Reference to the parent level in which the Boss exists.
     */
    private final LevelParent level;

    /**
     * Indicates whether the Boss is currently summoning a shield.
     */
    private boolean isSummoningShield = false;

    /**
     * Flag indicating whether the Boss is ready to be removed from the game.
     */
    public boolean isReadyToRemove = false;

    /**
     * Callback to execute after the Boss's explosion animation finishes.
     */
    private Runnable onExplosionFinished;

    /**
     * Constructs a Boss instance with specified root group and parent level.
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
     * Sets up the ProgressBar and positions it above the Boss.
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
     * Handles movement, shooting, and shield positioning.
     *
     * @param deltaTime The time elapsed since the last update (in seconds).
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
     * Inflicts damage on the Boss. If a shield is active and not destroyed, damage is bypassed.
     * Otherwise, the Boss takes damage and the health bar is updated.
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
     * Sets a callback to be executed after the Boss's explosion animation finishes.
     *
     * @param onExplosionFinished The callback to execute.
     */
    public void setOnExplosionFinished(Runnable onExplosionFinished) {
        this.onExplosionFinished = onExplosionFinished;
    }

    /**
     * Updates the health bar to reflect the Boss's current health.
     * Ensures that UI updates occur on the JavaFX Application Thread.
     */
    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    /**
     * Stops the timeline responsible for periodically checking and summoning shields.
     */
    public void stopShieldTimeline() {
        if (shieldCheckTimeline != null) {
            shieldCheckTimeline.stop();
        }
    }

    /**
     * Initializes the timeline that periodically checks if the Boss needs to summon a shield.
     * The Boss attempts to summon a shield every 10 seconds if no active shield exists.
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
     * Ensures that only one shield is being summoned at a time.
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
     * Initializes the movement pattern for the Boss.
     * The pattern consists of alternating vertical velocities to create a back-and-forth motion.
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
     * Shuffles the pattern after a certain number of consecutive moves to vary the Boss's movement.
     *
     * @return The next vertical velocity for the Boss.
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
     * Destroys the Boss, triggering an explosion animation and executing the onExplosionFinished callback.
     * Ensures that the Boss is only destroyed once.
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
     * Prevents the Boss from moving outside the visible screen bounds.
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
