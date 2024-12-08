package com.example.demo.actors;

public class Asteroid extends Obstacle {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final int HORIZONTAL_VELOCITY = -20;
    private static final int INITIAL_HEALTH = 1;

    public Asteroid(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

}
