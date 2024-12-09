package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

public class EnemyPlaneTwo extends Actor {

    private static final String IMAGE_NAME = "enemyplanetwo.png";
    private static final int IMAGE_HEIGHT = 150;
    private static final double PROJECTILE_X_OFFSET = -100.0;
    private static final double PROJECTILE_Y_OFFSET = 50.0;
    private static final int INITIAL_HEALTH = 5;
    private static final double FIRE_RATE = 0.5;

    private final ShootingComponent shootingComponent;
    private final AnimationComponent animationComponent;

    public EnemyPlaneTwo(double initialX, double initialY, Group root) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialX, initialY, INITIAL_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.75, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(-3, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        if (shootingComponent.getProjectilePool() == null) {
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
            animationComponent.playExplosion(x + planeWidth / 2, y + planeHeight / 2, 1.5);
            SoundComponent.playExplosionSound();
        }
    }
}