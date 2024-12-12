package com.example.demo.actors.obstacles;

public class Satellite extends Obstacle {

    private static final String IMAGE_NAME = "Obstacles/satellite.png";
    private static final int IMAGE_HEIGHT = 100;
    private static final int HORIZONTAL_VELOCITY = -5;
    private static final int INITIAL_HEALTH = 10;

    public Satellite(double initialXPos, double initialYPos) {
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
