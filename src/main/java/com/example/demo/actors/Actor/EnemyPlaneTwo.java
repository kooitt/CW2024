package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

/**
 * Represents an advanced enemy plane (EnemyPlaneTwo) in the game.
 * This plane has specific behavior, including firing projectiles at a constant rate,
 * moving at a fixed velocity, and playing animations and sounds upon destruction.
 */
public class EnemyPlaneTwo extends Actor {

    /**
     * The file name of the image used to represent the enemy plane.
     */
    private static final String IMAGE_NAME = "enemyplanetwo.png";

    /**
     * The height of the enemy plane's image in pixels.
     */
    private static final int IMAGE_HEIGHT = 150;

    /**
     * The horizontal offset for the projectile's spawn position, relative to the plane.
     */
    private static final double PROJECTILE_X_OFFSET = -100.0;

    /**
     * The vertical offset for the projectile's spawn position, relative to the plane.
     */
    private static final double PROJECTILE_Y_OFFSET = 50.0;

    /**
     * The initial health of the enemy plane.
     */
    private static final int INITIAL_HEALTH = 5;

    /**
     * The rate of fire for the plane's shooting component, in seconds per shot.
     */
    private static final double FIRE_RATE = 0.5;

    /**
     * The shooting component that handles the plane's ability to fire projectiles.
     */
    private final ShootingComponent shootingComponent;

    /**
     * The animation component that handles visual effects, such as explosions, for the plane.
     */
    private final AnimationComponent animationComponent;

    /**
     * Constructs a new instance of EnemyPlaneTwo with specified initial position and parent root.
     *
     * @param initialX the initial X-coordinate of the enemy plane.
     * @param initialY the initial Y-coordinate of the enemy plane.
     * @param root     the parent root where the animation components will be displayed.
     */
    public EnemyPlaneTwo(double initialX, double initialY, Group root) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialX, initialY, INITIAL_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.75, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(-3, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
    }

    /**
     * Updates the state of the enemy plane during each frame.
     * This includes updating its position, hitbox, and shooting behavior.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     * @param level     the current level context, providing access to shared resources.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        // Lazily initialize the projectile pool if not already set
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getEnemyProjectilePool());
        }

        shootingComponent.update(deltaTime, level);
    }

    /**
     * Handles the destruction of the enemy plane.
     * Plays explosion animations and sounds, and ensures the plane is marked as destroyed.
     */
    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();

            // Calculate the center of the plane for accurate explosion placement
            double planeWidth = getCollisionComponent().getHitboxWidth();
            double planeHeight = getCollisionComponent().getHitboxHeight();
            double x = getCollisionComponent().getHitboxX();
            double y = getCollisionComponent().getHitboxY();

            // Play explosion animation and sound
            animationComponent.playExplosion(x + planeWidth / 2, y + planeHeight / 2, 1.5);
            SoundComponent.playExplosionSound();
        }
    }
}
