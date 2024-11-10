package com.example.demo;

public class EnemyPlaneTwo extends FighterPlane{
    private static final String IMAGE_NAME = "enemyplane2.png";
    private static final int IMAGE_HEIGHT = 60;
    private static final int HORIZONTAL_VELOCITY = -6;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 20.0;
    private static final int INITIAL_HEALTH = 2;
    private static final double FIRE_RATE = .03;

    public EnemyPlaneTwo(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPostion = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPostion);
        }
        return null;
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

}
