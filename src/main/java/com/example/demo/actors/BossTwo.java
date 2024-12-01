// BossTwo.java
package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

/**
 * Represents the second boss character in the game with new behaviors.
 */
public class BossTwo extends ActiveActor {

    private static final String IMAGE_NAME = "bosscxk.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FIRE_RATE = 1.5;
    private static final int IMAGE_HEIGHT = 200;
    private static final int MAX_HEALTH = 100;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;

    private LevelParent level;

    private double screenHeight;

    /**
     * Constructs a BossTwo with specified root and level.
     *
     * @param root  the root group.
     * @param level the current level.
     */
    public BossTwo(Group root, LevelParent level) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
        this.level = level;
        this.screenHeight = level.getScreenHeight();

        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();

        shootingComponent = new ShootingComponent(this, FIRE_RATE, level.getBossTwoProjectilePool(), 0, PROJECTILE_Y_POSITION_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();

        // 初始化Boss的移动速度为0
        getMovementComponent().setVelocity(0, 0);
    }

    private double movementTimer = 0;
    private double movementInterval = 1.0; // 每隔1秒改变一次方向
    private double verticalVelocity = 5;

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        movementTimer += deltaTime;
        if (movementTimer >= movementInterval) {
            movementTimer = 0;
            // 随机决定向上或向下移动
            int direction = (Math.random() < 0.5) ? -1 : 1;
            getMovementComponent().setVelocity(0, verticalVelocity * direction);
        }

        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        // 确保Boss不会超出垂直边界
        double currentY = getLayoutY() + getTranslateY();
        if (currentY < 0) {
            setTranslateY(-getLayoutY());
            getMovementComponent().setVelocity(0, verticalVelocity);
        } else if (currentY + getCollisionComponent().getHitboxHeight() > screenHeight) {
            setTranslateY(screenHeight - getLayoutY() - getCollisionComponent().getHitboxHeight());
            getMovementComponent().setVelocity(0, -verticalVelocity);
        }

        if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getBossTwoProjectilePool());
        }

        shootingComponent.update(deltaTime, level);

        // 实现其他特殊行为，例如特殊攻击
        performSpecialAttack(deltaTime);
    }

    private void performSpecialAttack(double deltaTime) {
        // 示例：每隔一定时间，发射一组子弹
    }

    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();
            double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth();
            double y = getCollisionComponent().getHitboxY();
            animationComponent.playExplosion(x, y, 2.5);
        }
    }
}
