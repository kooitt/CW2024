package com.example.demo;

public class BossProjectile extends Projectile {

    private static final String IMAGE_NAME = "fireball.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final int HORIZONTAL_VELOCITY = -30;
    private static final int INITIAL_X_POSITION = 950;
    private int zigzagDirection = 1; // 1 for down, -1 for up
    private static final int ZIGZAG_OFFSET = 5;

    public BossProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
        moveVertically(ZIGZAG_OFFSET * zigzagDirection);
        zigzagDirection *= -1; // Change direction to create zigzag motion
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
}
