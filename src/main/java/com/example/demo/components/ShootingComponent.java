package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import com.example.demo.actors.Projectile.Projectile;
import com.example.demo.utils.ObjectPool;
import com.example.demo.actors.Actor.UserPlane;

/**
 * The ShootingComponent handles the shooting functionality of an {@link Actor}.
 * It manages the firing rate, projectile pooling, and the placement of projectiles.
 */
public class ShootingComponent {
    /** The rate of fire (shots per second). */
    private double fireRate;

    /** The time since the last shot was fired. */
    private double timeSinceLastShot;

    /** The pool of projectiles to be reused during firing. */
    private ObjectPool<Projectile> projectilePool;

    /** The actor that owns this shooting component. */
    private final Actor owner;

    /** The horizontal offset for spawning projectiles relative to the owner. */
    private final double projectileXOffset;

    /** The vertical offset for spawning projectiles relative to the owner. */
    private final double projectileYOffset;

    /** Indicates whether the actor is currently firing. */
    private boolean isFiring;

    /** The number of rows of bullets fired simultaneously. */
    private int bulletRows = 1;

    /** The vertical spacing between bullet rows. */
    private double rowSpacing = 30;

    /**
     * Constructs a ShootingComponent for the given actor.
     *
     * @param owner             The actor that owns this component.
     * @param fireRate          The rate of fire (shots per second).
     * @param projectilePool    The pool of projectiles to use during firing.
     * @param projectileXOffset The horizontal offset for spawning projectiles.
     * @param projectileYOffset The vertical offset for spawning projectiles.
     */
    public ShootingComponent(Actor owner, double fireRate, ObjectPool<Projectile> projectilePool, double projectileXOffset, double projectileYOffset) {
        this.owner = owner;
        this.fireRate = fireRate;
        this.projectilePool = projectilePool;
        this.projectileXOffset = projectileXOffset;
        this.projectileYOffset = projectileYOffset;
    }

    /**
     * Updates the shooting component, handling firing logic if the actor is firing.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     * @param level     The current level in which the actor resides.
     */
    public void update(double deltaTime, LevelParent level) {
        if (isFiring) {
            timeSinceLastShot += deltaTime;
            if (timeSinceLastShot >= (1.0 / fireRate)) {
                fire(level);
                timeSinceLastShot = 0;
            }
        }
    }

    /**
     * Fires projectiles based on the current configuration.
     *
     * @param level The current level in which the projectiles are fired.
     */
    private void fire(LevelParent level) {
        if (projectilePool == null) return;
        for (int i = 0; i < bulletRows; i++) {
            Projectile projectile = projectilePool.acquire();
            if (projectile != null) {
                double x = owner.getProjectileXPosition(projectileXOffset);
                double y = owner.getProjectileYPosition(projectileYOffset) + (i - bulletRows / 2.0) * rowSpacing;
                projectile.resetPosition(x, y);
                level.getRoot().getChildren().add(projectile);
                level.addProjectile(projectile, owner);

                if (owner instanceof UserPlane) {
                    SoundComponent.playShootingSound();
                }
            }
        }
    }

    /**
     * Starts the firing process.
     */
    public void startFiring() {
        isFiring = true;
    }

    /**
     * Stops the firing process and resets the time since the last shot.
     */
    public void stopFiring() {
        isFiring = false;
        timeSinceLastShot = 0;
    }

    /**
     * Sets the rate of fire (shots per second).
     *
     * @param fireRate The new rate of fire.
     */
    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Gets the current rate of fire (shots per second).
     *
     * @return The current rate of fire.
     */
    public double getFireRate() {
        return fireRate;
    }

    /**
     * Gets the current projectile pool used for firing.
     *
     * @return The projectile pool.
     */
    public ObjectPool<Projectile> getProjectilePool() {
        return projectilePool;
    }

    /**
     * Sets the projectile pool to use for firing.
     *
     * @param projectilePool The new projectile pool.
     */
    public void setProjectilePool(ObjectPool<Projectile> projectilePool) {
        this.projectilePool = projectilePool;
    }

    /**
     * Adds an additional row of bullets to be fired.
     */
    public void addBulletRow() {
        bulletRows++;
    }

    /**
     * Sets the number of rows of bullets to be fired.
     *
     * @param rows The number of bullet rows. Must be greater than 0.
     */
    public void setBulletRows(int rows) {
        if (rows > 0) {
            bulletRows = rows;
        }
    }

    /**
     * Gets the current number of rows of bullets to be fired.
     *
     * @return The number of bullet rows.
     */
    public int getBulletRows() {
        return bulletRows;
    }

    /**
     * Sets the vertical spacing between bullet rows.
     *
     * @param spacing The spacing between rows, in pixels. Must be non-negative.
     */
    public void setRowSpacing(double spacing) {
        if (spacing >= 0) {
            rowSpacing = spacing;
        }
    }

    /**
     * Gets the vertical spacing between bullet rows.
     *
     * @return The vertical spacing between rows, in pixels.
     */
    public double getRowSpacing() {
        return rowSpacing;
    }
}
