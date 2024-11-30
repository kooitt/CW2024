package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.projectiles.Projectile;

/**
 * Represents the user-controlled plane in the game.
 * Can move in all directions and shoot projectiles.
 */
public class UserPlane extends ActiveActor {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40;
    private static final double Y_LOWER_BOUND = 600.0;
    private static final double X_LEFT_BOUND = 0.0;
    private static final double X_RIGHT_BOUND = 800.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 150;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HORIZONTAL_VELOCITY = 8;
    private static final double PROJECTILE_X_OFFSET = 110;
    private static final double PROJECTILE_Y_OFFSET = 20;
    private static final double FIRE_RATE = 10.0;
    private static final int INITIAL_HEALTH = 5;
    private static final int POWER_UP_THRESHOLD = 5; // 每5个道具增加一排子弹

    private int verticalVelocityMultiplier;
    private int horizontalVelocityMultiplier;
    private int numberOfKills;

    private ShootingComponent shootingComponent;
    private int powerUpCount; // 记录吃掉的道具数量
    private int extraBulletRows; // 额外发射的子弹排数

    private AnimationComponent animationComponent; // 新增字段

    /**
     * Constructs a UserPlane with default position.
     */
    public UserPlane() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, INITIAL_HEALTH);
        this.powerUpCount = 0;
        this.extraBulletRows = 0;
        verticalVelocityMultiplier = 0;
        horizontalVelocityMultiplier = 0;
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.3);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        shootingComponent.startFiring();
    }

    // 新增 setter 方法
    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }

    /**
     * Doubles the firing rate of the plane.
     */
    public void doubleFireRate() {
        double currentFireRate = shootingComponent.getFireRate();
        shootingComponent.setFireRate(currentFireRate + 1); // Double the firing rate
        System.out.println("Fire rate doubled!");
    }

    /**
     * Adds an extra bullet row.
     */
    public void addExtraBulletRow() {
        shootingComponent.addBulletRow();
        extraBulletRows++;
        System.out.println("Extra bullet row added! Total rows: " + shootingComponent.getBulletRows());
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getUserProjectilePool());
        }

        shootingComponent.update(deltaTime, level);
    }

    /**
     * Increments the power-up count and adds an extra bullet row every 5 power-ups.
     */
    public void incrementPowerUpCount() {
        powerUpCount++;

        // 播放升级动画
        if (animationComponent != null) {
            double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth() *1.5;
            double y = getCollisionComponent().getHitboxY() + getCollisionComponent().getHitboxHeight() ;
            double scale = 5.0; // 根据需要调整缩放比例
            animationComponent.playLevelUp(x, y, scale);
        }

        System.out.println("Power-ups collected: " + powerUpCount);
        if (powerUpCount % POWER_UP_THRESHOLD == 0) {
            addExtraBulletRow();
        }
    }

    /**
     * Moves the plane upwards.
     */
    public void moveUp() {
        verticalVelocityMultiplier = -1;
        updateVelocity();
    }

    /**
     * Moves the plane downwards.
     */
    public void moveDown() {
        verticalVelocityMultiplier = 1;
        updateVelocity();
    }

    /**
     * Stops vertical movement.
     */
    public void stopVerticalMovement() {
        verticalVelocityMultiplier = 0;
        updateVelocity();
    }

    /**
     * Moves the plane to the left.
     */
    public void moveLeft() {
        horizontalVelocityMultiplier = -1;
        updateVelocity();
    }

    /**
     * Moves the plane to the right.
     */
    public void moveRight() {
        horizontalVelocityMultiplier = 1;
        updateVelocity();
    }

    /**
     * Stops horizontal movement.
     */
    public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
        updateVelocity();
    }

    private void updateVelocity() {
        int dx = HORIZONTAL_VELOCITY * horizontalVelocityMultiplier;
        int dy = VERTICAL_VELOCITY * verticalVelocityMultiplier;
        getMovementComponent().setVelocity(dx, dy);
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void incrementKillCount() {
        numberOfKills++;
    }

    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX();
        double initialTranslateY = getTranslateY();
        super.updatePosition();
        double newX = getLayoutX() + getTranslateX();
        double newY = getLayoutY() + getTranslateY();

        if (newX < X_LEFT_BOUND || newX > X_RIGHT_BOUND) setTranslateX(initialTranslateX);
        if (newY < Y_UPPER_BOUND || newY > Y_LOWER_BOUND) setTranslateY(initialTranslateY);
    }
}
