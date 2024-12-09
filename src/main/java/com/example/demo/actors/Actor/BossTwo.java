package com.example.demo.actors.Actor;

import com.example.demo.actors.Projectile.Projectile;
import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the second boss in the game, featuring shooting, movement, animations, and health management.
 * Extends {@link Actor} to inherit basic actor properties and behavior.
 */
public class BossTwo extends Actor {

    /**
     * Prefix for the boss's animation frames.
     */
    private static final String FRAME_PREFIX = "bosstwo";

    /**
     * Total number of animation frames.
     */
    private static final int FRAME_COUNT = 14;

    /**
     * Height of the boss's image.
     */
    private static final int IMAGE_HEIGHT = 200;

    /**
     * Maximum health of the boss.
     */
    private static final int MAX_HEALTH = 500;

    /**
     * Width of the boss's health bar.
     */
    private static final int HEALTH_BAR_WIDTH = 150;

    /**
     * Height of the boss's health bar.
     */
    private static final int HEALTH_BAR_HEIGHT = 15;

    /**
     * Cooldown time for firing special scatter shots, in seconds.
     */
    private static final double SPECIAL_SHOT_COOLDOWN = 5.0;

    /**
     * Interval for changing movement direction, in seconds.
     */
    private static final double MOVEMENT_INTERVAL = 1.0;

    /**
     * Vertical velocity for the boss's movement.
     */
    private static final double VERTICAL_VELOCITY = 5;

    /**
     * Handles shooting mechanics for the boss.
     */
    private final ShootingComponent shootingComponent;

    /**
     * Handles explosion animations for the boss.
     */
    private final AnimationComponent animationComponent;

    /**
     * Height of the game screen, used to constrain the boss's movement.
     */
    private final double screenHeight;

    /**
     * Timer for tracking movement direction changes.
     */
    private double movementTimer = 0;

    /**
     * Timer for tracking the cooldown of the special scatter shot.
     */
    private double timeSinceLastSpecialShot = 0.0;

    /**
     * Progress bar displaying the boss's current health.
     */
    private ProgressBar healthBar;

    /**
     * List of images used for the boss's animation.
     */
    private List<Image> frames;

    /**
     * Index of the current frame in the animation sequence.
     */
    private int currentFrameIndex = 0;

    /**
     * Timeline used for handling frame animations.
     */
    private Timeline animationTimeline;

    /**
     * Constructs a new BossTwo instance with the given root group and level.
     *
     * @param root  The root group of the game scene.
     * @param level The level instance to which the boss belongs.
     */
    public BossTwo(Group root, LevelParent level) {
        super(FRAME_PREFIX + "1.png", IMAGE_HEIGHT, 1000.0, 300.0, MAX_HEALTH);
        this.screenHeight = level.getScreenHeight();
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, 1.5, level.getBossTwoProjectilePool(), 0, 75.0);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
        initializeHealthBar();
        loadAnimationFrames();
        startAnimation();
    }

    /**
     * Loads the animation frames for the boss.
     */
    private void loadAnimationFrames() {
        frames = new ArrayList<>();
        for (int i = 1; i <= FRAME_COUNT; i++) {
            frames.add(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + FRAME_PREFIX + i + ".png")).toExternalForm()));
        }
    }

    /**
     * Starts the animation timeline for the boss's movement and appearance.
     */
    private void startAnimation() {
        animationTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
            imageView.setImage(frames.get(currentFrameIndex));
        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    /**
     * Initializes the health bar for the boss and adds it to the display.
     */
    private void initializeHealthBar() {
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setStyle("-fx-accent: red; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 2;");
        this.getChildren().add(healthBar);
        healthBar.toFront();
        healthBar.setLayoutX((getCollisionComponent().getHitboxWidth() - HEALTH_BAR_WIDTH) / 2);
        healthBar.setLayoutY(-HEALTH_BAR_HEIGHT - 10);
        healthBar.setVisible(getCurrentHealth() > 0);
    }

    /**
     * Updates the boss's state, including movement, shooting, and health bar.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     * @param level     The current level instance.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        movementTimer += deltaTime;
        if (movementTimer >= MOVEMENT_INTERVAL) {
            movementTimer = 0;
            getMovementComponent().setVelocity(0, VERTICAL_VELOCITY * (Math.random() < 0.5 ? -1 : 1));
        }

        updatePosition();
        getCollisionComponent().updateHitBoxPosition();
        double currentY = getLayoutY() + getTranslateY();
        if (currentY < 0) {
            setTranslateY(-getLayoutY());
            getMovementComponent().setVelocity(0, VERTICAL_VELOCITY);
        } else if (currentY + getCollisionComponent().getHitboxHeight() > screenHeight) {
            setTranslateY(screenHeight - getLayoutY() - getCollisionComponent().getHitboxHeight());
            getMovementComponent().setVelocity(0, -VERTICAL_VELOCITY);
        }

        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getBossTwoProjectilePool());
        }
        shootingComponent.update(deltaTime, level);

        timeSinceLastSpecialShot += deltaTime;
        if (timeSinceLastSpecialShot >= SPECIAL_SHOT_COOLDOWN) {
            fireScatterShot(level);
            timeSinceLastSpecialShot = 0.0;
        }
        updateHealthBar();
    }

    /**
     * Fires a scatter shot with projectiles at predefined angles.
     *
     * @param level The current level instance.
     */
    private void fireScatterShot(LevelParent level) {
        if (level.getBossTwoProjectilePool() == null) return;
        double[] angles = {-15, -7.5, 0, 7.5, 15};
        for (double angle : angles) {
            Projectile projectile = level.getBossTwoProjectilePool().acquire();
            if (projectile != null) {
                double rad = Math.toRadians(angle);
                double x = getProjectileXPosition(0);
                double y = getProjectileYPosition(75.0);
                projectile.resetPosition(x, y);
                double baseSpeed = 12;
                projectile.getMovementComponent().setVelocity(Math.cos(rad) * -baseSpeed, Math.sin(rad) * -baseSpeed);
                level.getRoot().getChildren().add(projectile);
                level.addProjectile(projectile, this);
            }
        }
    }

    /**
     * Updates the boss's health bar to reflect the current health.
     */
    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    /**
     * Takes damage, updates health, and refreshes the health bar.
     *
     * @param damage The amount of damage to be taken.
     */
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        updateHealthBar();
    }

    /**
     * Destroys the boss, plays the explosion animation, and stops the animation timeline.
     */
    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();
            animationComponent.playExplosion(getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth(), getCollisionComponent().getHitboxY(), 2.5);
            if (animationTimeline != null) {
                animationTimeline.stop();
            }
        }
    }
}
