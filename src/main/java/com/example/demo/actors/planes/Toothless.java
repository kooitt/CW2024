package com.example.demo.actors.planes;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.projectiles.ProjectileFactory;

public class Toothless extends UserPlane{
    private static final String IMAGE_NAME = "toothless.png";
    private static final double Y_UPPER_BOUND = 10;
    private static final double Y_LOWER_BOUND = 600.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 200;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int PROJECTILE_X_POSITION = 170;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
    private int velocityMultiplier;
    private int numberOfKills;

    public Toothless(String IMAGE_NAME, int IMAGE_HEIGHT, double INITIAL_X_POSITION, double INITIAL_Y_POSITION, int initialHealth) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        velocityMultiplier = 0;
    }

    @Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
            double newPosition = getLayoutY() + getTranslateY();
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY);
            }
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        return ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.TOOTHLESS, PROJECTILE_X_POSITION, getProjectileYPosition());
    }

    @Override
    public double getImageHeight() {
        return IMAGE_HEIGHT;
    }

    private boolean isMoving() {
        return velocityMultiplier != 0;
    }

    public void moveUp() {
        velocityMultiplier = -1;
    }

    public void moveDown() {
        velocityMultiplier = 1;
    }

    public void stop() {
        velocityMultiplier = 0;
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void incrementKillCount() {
        numberOfKills++;
    }

}

