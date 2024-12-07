// HeartItem.java
package com.example.demo.actors.Actor;

import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;

/**
 * HeartItem 类表示一个爱心道具，当玩家拾取时会恢复生命值。
 */
public class HeartItem extends Actor {

    private static final String IMAGE_NAME = "heartitem.png"; // 确保在资源文件夹中有 heart.png
    private static final int IMAGE_HEIGHT = 50;
    private static final double SPEED_X = -10;
    private static final double AMPLITUDE = 30;
    private static final double FREQUENCY = 0.5;
    private static final int MAX_HEALTH_RESTORE = 1; // 每个爱心恢复的最大生命值
    private double initialY;
    private double time;

    public HeartItem(double initialXPos, double initialYPos) {
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
        user.getHealthComponent().heal(MAX_HEALTH_RESTORE);
        level.getLevelView().addHearts(MAX_HEALTH_RESTORE);
        destroy();
        SoundComponent.playGethealthSound();
    }
}
