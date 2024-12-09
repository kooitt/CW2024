package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;
import com.example.demo.actors.Projectile.UserProjectile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Represents the player's aircraft in the game, including movement, shooting, and power-up functionality.
 */
public class UserPlane extends Actor {

    /** Y-coordinate upper boundary for movement. */
    private static final double Y_UPPER_BOUND = -40;

    /** Y-coordinate lower boundary for movement. */
    private static final double Y_LOWER_BOUND = 600.0;

    /** X-coordinate left boundary for movement. */
    private static final double X_LEFT_BOUND = 0.0;

    /** X-coordinate right boundary for movement. */
    private static final double X_RIGHT_BOUND = 800.0;

    /** Initial X position of the plane. */
    private static final double INITIAL_X_POSITION = 5.0;

    /** Initial Y position of the plane. */
    private static final double INITIAL_Y_POSITION = 300.0;

    /** Height of the plane's image. */
    private static final int IMAGE_HEIGHT = 100;

    /** Movement velocity multiplier for the plane. */
    private static final int VELOCITY = 8;

    /** Horizontal offset for projectile spawning. */
    private static final double PROJECTILE_X_OFFSET = 110;

    /** Vertical offset for projectile spawning. */
    private static final double PROJECTILE_Y_OFFSET = 45;

    /** Default firing rate of the plane. */
    private static final double FIRE_RATE = 500.0;

    /** Threshold of power-ups required for level-up. */
    private static final int POWER_UP_THRESHOLD = 3;

    /** Current image index for the plane, used for upgrades. */
    private int planeImageIndex = 0;

    /** List of images for different upgrade levels of the plane. */
    private static final String[] PLANE_IMAGES = {"userplane.png", "userplane2.png", "userplane3.png", "userplane4.png"};

    /** List of images for different upgrade levels of the projectiles. */
    private static final String[] BULLET_IMAGES = {"userfire.png", "userfire2.png", "userfire3.png", "userfire4.png"};

    /** Multiplier for vertical movement velocity. */
    private int verticalVelocityMultiplier = 0;

    /** Multiplier for horizontal movement velocity. */
    private int horizontalVelocityMultiplier = 0;

    /** Total number of enemies killed by the player. */
    private int numberOfKills = 0;

    /** Counter for collected power-ups. */
    private int powerUpCount = 0;

    /** Number of extra rows of bullets added via upgrades. */
    private int extraBulletRows = 0;

    /** Component responsible for handling the shooting mechanics. */
    private final ShootingComponent shootingComponent;

    /** Component responsible for handling animations related to the user plane. */
    private AnimationComponent animationComponent;

    /**
     * Constructs a UserPlane with the specified initial health.
     *
     * @param initialHealth Initial health value for the player plane.
     */
    public UserPlane(int initialHealth) {
        super(PLANE_IMAGES[0], IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        UserProjectile.setCurrentImageName(BULLET_IMAGES[0]);

        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT);
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        shootingComponent.startFiring();

        shootingComponent.setBulletRows(10);
    }

    /**
     * Sets the animation component for the user plane.
     *
     * @param animationComponent The animation component to be used for level-up and related animations.
     */
    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }

    /**
     * Increases the firing rate of the plane's projectiles.
     */
    public void doubleFireRate() {
        shootingComponent.setFireRate(shootingComponent.getFireRate() + 1);
    }

    /**
     * Adds an extra row of bullets to the plane's shooting pattern.
     */
    public void addExtraBulletRow() {
        shootingComponent.addBulletRow();
        extraBulletRows++;
    }

    /**
     * Updates the position, collision, and shooting logic of the plane.
     *
     * @param deltaTime Time elapsed since the last update.
     * @param level     Reference to the current game level.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getUserProjectilePool());
        }
        shootingComponent.update(deltaTime, level);
    }

    /**
     * Increments the power-up count and triggers upgrades when thresholds are reached.
     */
    public void incrementPowerUpCount() {
        powerUpCount++;
        if (powerUpCount % POWER_UP_THRESHOLD == 0 && animationComponent != null) {
            SoundComponent.playUpgradeSound();
            addExtraBulletRow();
            planeImageIndex = Math.min(planeImageIndex + 1, PLANE_IMAGES.length - 1);
            setImageViewImage(PLANE_IMAGES[planeImageIndex]);
            UserProjectile.setCurrentImageName(BULLET_IMAGES[planeImageIndex]);

            animationComponent.playLevelUpRelative(this,
                    getCollisionComponent().getHitboxWidth() * 1.3,
                    getCollisionComponent().getHitboxHeight(),
                    5.0);
        }
    }

    /** Moves the plane upwards. */
    public void moveUp() {
        verticalVelocityMultiplier = -1;
        updateVelocity();
    }

    /** Moves the plane downwards. */
    public void moveDown() {
        verticalVelocityMultiplier = 1;
        updateVelocity();
    }

    /** Stops vertical movement. */
    public void stopVerticalMovement() {
        verticalVelocityMultiplier = 0;
        updateVelocity();
    }

    /** Moves the plane to the left. */
    public void moveLeft() {
        horizontalVelocityMultiplier = -1;
        updateVelocity();
    }

    /** Moves the plane to the right. */
    public void moveRight() {
        horizontalVelocityMultiplier = 1;
        updateVelocity();
    }

    /** Stops horizontal movement. */
    public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
        updateVelocity();
    }

    /** Updates the velocity based on current movement multipliers. */
    private void updateVelocity() {
        getMovementComponent().setVelocity(VELOCITY * horizontalVelocityMultiplier, VELOCITY * verticalVelocityMultiplier);
    }

    /**
     * Returns the current number of kills by the player.
     *
     * @return The number of kills.
     */
    public int getNumberOfKills() {
        return numberOfKills;
    }

    /** Increments the kill count. */
    public void incrementKillCount() {
        numberOfKills++;
    }

    /** Stops the plane from shooting projectiles. */
    public void stopShooting() {
        shootingComponent.stopFiring();
    }

    /**
     * Updates the position of the plane while ensuring it remains within bounds.
     */
    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX(), initialTranslateY = getTranslateY();
        super.updatePosition();
        double newX = getLayoutX() + getTranslateX(), newY = getLayoutY() + getTranslateY();
        if (newX < X_LEFT_BOUND || newX > X_RIGHT_BOUND) setTranslateX(initialTranslateX);
        if (newY < Y_UPPER_BOUND || newY > Y_LOWER_BOUND) setTranslateY(initialTranslateY);
    }

    /**
     * Reduces the plane's health and triggers blinking and temporary invincibility if health remains.
     *
     * @param damage The amount of damage to inflict.
     */
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getCurrentHealth() > 0) {
            SoundComponent.playBlinkSound();
            startBlinking();
            setActorCollisionEnabled(false, 2000);
        }
    }

    /** Starts a blinking animation to indicate temporary invincibility. */
    private void startBlinking() {
        Timeline blinkTimeline = new Timeline();
        int totalFlashes = 4, interval = 250;
        for (int i = 0; i < totalFlashes * 2; i++) {
            blinkTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * interval), e -> setVisible(!isVisible())));
        }
        blinkTimeline.setOnFinished(e -> setVisible(true));
        blinkTimeline.play();
    }

    /**
     * Enables or disables collision detection for the plane, with an optional delay for reenabling.
     *
     * @param enabled    Whether collision should be enabled.
     * @param durationMs The duration (in milliseconds) before reenabling collision, if disabled.
     */
    public void setActorCollisionEnabled(boolean enabled, int durationMs) {
        getCollisionComponent().SetActorCollisionEnable(enabled);
        if (!enabled) {
            new Timeline(new KeyFrame(Duration.millis(durationMs), event -> getCollisionComponent().SetActorCollisionEnable(true))).play();
        }
    }
}
