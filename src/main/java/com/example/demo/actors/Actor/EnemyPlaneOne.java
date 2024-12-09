package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

/**
 * Represents an enemy plane actor in the game.
 * <p>
 * EnemyPlaneOne is a specific type of {@link Actor} that has a projectile shooting capability,
 * an animation component for explosions, and a defined movement pattern.
 * It interacts with other game components such as the level, sound system, and collision detection.
 * </p>
 */
public class EnemyPlaneOne extends Actor {

    /**
     * The name of the image resource used to visually represent the enemy plane.
     */
    private static final String IMAGE_NAME = "enemyplaneone.png";

    /**
     * The height of the enemy plane image in pixels.
     */
    private static final int IMAGE_HEIGHT = 80;

    /**
     * The X-axis offset for the projectiles fired by this plane, relative to the plane's position.
     */
    private static final double PROJECTILE_X_OFFSET = -100.0;

    /**
     * The Y-axis offset for the projectiles fired by this plane, relative to the plane's position.
     */
    private static final double PROJECTILE_Y_OFFSET = 40.0;

    /**
     * The initial health of the enemy plane.
     */
    private static final int INITIAL_HEALTH = 1;

    /**
     * The rate at which the enemy plane fires projectiles, in shots per second.
     */
    private static final double FIRE_RATE = 0.5;

    /**
     * The shooting component used to handle projectile firing behavior.
     */
    private final ShootingComponent shootingComponent;

    /**
     * The animation component used to handle visual effects such as explosions.
     */
    private final AnimationComponent animationComponent;

    /**
     * Constructs an instance of {@code EnemyPlaneOne}.
     *
     * @param initialX the initial X position of the enemy plane.
     * @param initialY the initial Y position of the enemy plane.
     * @param root     the {@link Group} root where animations and visual elements are added.
     */
    public EnemyPlaneOne(double initialX, double initialY, Group root) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialX, initialY, INITIAL_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(-6, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
    }

    /**
     * Updates the state of the enemy plane during the game loop.
     * <p>
     * This method updates the position of the enemy plane, its hitbox position,
     * and its shooting component. It also ensures that the projectile pool is set for the shooting component.
     * </p>
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     * @param level     the current level of the game.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        // Ensure the shooting component has the correct projectile pool from the level
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getEnemyProjectilePool());
        }

        shootingComponent.update(deltaTime, level);
    }

    /**
     * Destroys the enemy plane, triggering visual and auditory effects.
     * <p>
     * This method plays an explosion animation and sound effect. It ensures that
     * the destruction process occurs only once, even if the method is called multiple times.
     * </p>
     */
    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();
            double planeWidth = getCollisionComponent().getHitboxWidth();
            double planeHeight = getCollisionComponent().getHitboxHeight();
            double x = getCollisionComponent().getHitboxX();
            double y = getCollisionComponent().getHitboxY();
            animationComponent.playExplosion(x + planeWidth / 2, y + planeHeight / 2, 1.5);
            SoundComponent.playExplosionSound();
        }
    }
}
