package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;

public class UserPlane extends ActiveActor {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40, Y_LOWER_BOUND = 600.0;
    private static final double X_LEFT_BOUND = 0.0, X_RIGHT_BOUND = 800.0;
    private static final double INITIAL_X_POSITION = 5.0, INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 100, VERTICAL_VELOCITY = 8, HORIZONTAL_VELOCITY = 8;
    private static final double PROJECTILE_X_OFFSET = 110, PROJECTILE_Y_OFFSET = 45;
    private static final double FIRE_RATE = 5.0;
    private static final int INITIAL_HEALTH = 5, POWER_UP_THRESHOLD = 5;

    private int verticalVelocityMultiplier = 0, horizontalVelocityMultiplier = 0;
    private int numberOfKills = 0, powerUpCount = 0, extraBulletRows = 0;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;

    public UserPlane() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, INITIAL_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT);
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        shootingComponent.startFiring();
    }

    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }

    public void doubleFireRate() {
        shootingComponent.setFireRate(shootingComponent.getFireRate() + 1);
        System.out.println("Fire rate doubled!");
    }

    public void addExtraBulletRow() {
        shootingComponent.addBulletRow();
        extraBulletRows++;
        System.out.println("Extra bullet row added! Total rows: " + shootingComponent.getBulletRows());
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getUserProjectilePool());
        }
        shootingComponent.update(deltaTime, level);
    }

    public void incrementPowerUpCount() {
        powerUpCount++;
        if (animationComponent != null) {
            double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth() * 1.5;
            double y = getCollisionComponent().getHitboxY() + getCollisionComponent().getHitboxHeight();
            animationComponent.playLevelUp(x, y, 5.0);
        }
        System.out.println("Power-ups collected: " + powerUpCount);
        if (powerUpCount % POWER_UP_THRESHOLD == 0) {
            addExtraBulletRow();
        }
    }

    public void moveUp() {
        verticalVelocityMultiplier = -1;
        updateVelocity();
    }

    public void moveDown() {
        verticalVelocityMultiplier = 1;
        updateVelocity();
    }

    public void stopVerticalMovement() {
        verticalVelocityMultiplier = 0;
        updateVelocity();
    }

    public void moveLeft() {
        horizontalVelocityMultiplier = -1;
        updateVelocity();
    }

    public void moveRight() {
        horizontalVelocityMultiplier = 1;
        updateVelocity();
    }

    public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
        updateVelocity();
    }

    private void updateVelocity() {
        getMovementComponent().setVelocity(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier, VERTICAL_VELOCITY * verticalVelocityMultiplier);
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void incrementKillCount() {
        numberOfKills++;
    }

    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX(), initialTranslateY = getTranslateY();
        super.updatePosition();
        double newX = getLayoutX() + getTranslateX(), newY = getLayoutY() + getTranslateY();
        if (newX < X_LEFT_BOUND || newX > X_RIGHT_BOUND) setTranslateX(initialTranslateX);
        if (newY < Y_UPPER_BOUND || newY > Y_LOWER_BOUND) setTranslateY(initialTranslateY);
    }
}