package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

public class Shield extends ActiveActor {

    public static final int SHIELD_SIZE = 100; // 定义盾牌大小
    private static final String IMAGE_NAME = "shield.png";
    private static final int IMAGE_HEIGHT = 100;
    private static final int MAX_HEALTH = 10;

    public Shield(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, MAX_HEALTH);
        // 设置碰撞盒大小
        getCollisionComponent().setHitboxSize(SHIELD_SIZE, SHIELD_SIZE);
        getCollisionComponent().updateHitBoxPosition();
        // Shield 不在构造函数中添加到 root，交由 LevelParent 添加
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        // Shield 的移动逻辑保持不变，始终在Boss前方
        getCollisionComponent().updateHitBoxPosition();
    }
}
