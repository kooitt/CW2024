package com.example.demo;

public class EnemyProjectile extends Projectile {

    private static final String IMAGE_NAME = "enemyFire.png";
    private static final int IMAGE_HEIGHT = 35;
    private int horizontalVelocity; // Make velocity configurable

    // Constructor with default speed for regular levels
    public EnemyProjectile(double initialXPos, double initialYPos) {
        this(initialXPos, initialYPos, -20); // Default speed for regular levels
    }

    // Constructor with adjustable speed for levels like LevelTwo
    public EnemyProjectile(double initialXPos, double initialYPos, int horizontalVelocity) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
        this.horizontalVelocity = horizontalVelocity;
    }

    @Override
    public void updatePosition() {
        moveHorizontally(horizontalVelocity); // Use the configurable velocity
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
}
