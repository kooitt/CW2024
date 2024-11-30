package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

/**
 * Represents a level-up item in the game with a sine wave movement trajectory.
 */
public class ActorLevelUp extends ActiveActor {

    private static final String IMAGE_NAME = "ActorLevelUp.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final double SPEED_X = -10; // 水平方向速度
    private static final double AMPLITUDE = 30; // 正弦波的幅度
    private static final double FREQUENCY = 0.5; // 正弦波的频率

    private double initialY; // 初始Y位置
    private double time; // 累积时间，用于计算正弦波轨迹


    /**
     * Constructs an `ActorLevelUp` object with the specified position.
     *
     * @param initialXPos the initial X-coordinate of the item.
     * @param initialYPos the initial Y-coordinate of the item.
     */
    public ActorLevelUp(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, 1);

        this.initialY = initialYPos;
        this.time = 0;

        // 设置碰撞盒大小
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
    }

    /**
     * Updates the item's position during the game loop.
     *
     * @param deltaTime the time elapsed since the last update.
     * @param level     the current level instance.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        time += deltaTime; // 累积时间

        // 更新水平位置和垂直位置
        double newX = getLayoutX() + SPEED_X;
        double newY = initialY + AMPLITUDE * Math.sin(FREQUENCY * time * 2 * Math.PI); // 正弦波轨迹

        // 设置位置
        setLayoutX(newX);
        setLayoutY(newY);

        // 更新碰撞盒位置
        getCollisionComponent().updateHitBoxPosition();

        // 检测是否超出屏幕范围
        if (newX + getCollisionComponent().getHitboxWidth() < 0) {
            destroy(); // 超出屏幕范围后销毁
        }
    }

    /**
     * Handles the logic when the item is picked up by the user.
     *
     * @param level the current level instance.
     */
    public void onPickup(LevelParent level) {
        UserPlane user = level.getUser();
        user.incrementPowerUpCount(); // 增加道具计数并可能增加子弹排数
        user.doubleFireRate(); // 如果不需要，可以注释掉
        destroy(); // Remove the item from the game
    }

}
