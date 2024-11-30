// EnemyPlane.java
package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

/**
 * Represents an enemy plane in the game.
 * Can shoot projectiles and has collision detection.
 */
public class EnemyPlane extends ActiveActor {

    private static final String IMAGE_NAME = "enemyplane.png";
    private static final int IMAGE_HEIGHT = 150;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
    private static final int INITIAL_HEALTH = 1;
    private static final double FIRE_RATE = 0.5;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;

    /**
     * Constructs an EnemyPlane with specified position and root group.
     *
     * @param initialXPos initial X position.
     * @param initialYPos initial Y position.
     * @param root        root group.
     */
    public EnemyPlane(double initialXPos, double initialYPos, Group root) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.35);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(-6, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_POSITION_OFFSET, PROJECTILE_Y_POSITION_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getEnemyProjectilePool());
        }

        shootingComponent.update(deltaTime, level);
    }

    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();
            double planeWidth = getCollisionComponent().getHitboxWidth();
            double planeHeight = getCollisionComponent().getHitboxHeight();
            double x = getCollisionComponent().getHitboxX();
            double y = getCollisionComponent().getHitboxY();
            animationComponent.playExplosion(x + planeWidth, y + planeHeight, 1.5);
        }
    }
}
