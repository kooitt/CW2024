package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

public class ActorLevelUp extends ActiveActor {

    private static final String IMAGE_NAME = "ActorLevelUp.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final double SPEED_X = -10;
    private static final double AMPLITUDE = 30;
    private static final double FREQUENCY = 0.5;

    private double initialY;
    private double time;

    public ActorLevelUp(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, 1);
        this.initialY = initialYPos;
        this.time = 0;
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        time += deltaTime;
        double newX = getLayoutX() + SPEED_X;
        double newY = initialY + AMPLITUDE * Math.sin(FREQUENCY * time * 2 * Math.PI);
        setLayoutX(newX);
        setLayoutY(newY);
        getCollisionComponent().updateHitBoxPosition();
        if (newX + getCollisionComponent().getHitboxWidth() < 0) {
            destroy();
        }
    }

    public void onPickup(LevelParent level) {
        UserPlane user = level.getUser();
        user.incrementPowerUpCount();
        user.doubleFireRate();
        destroy();
    }
}