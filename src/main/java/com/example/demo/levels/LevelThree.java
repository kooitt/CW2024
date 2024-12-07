// LevelThree.java
package com.example.demo.levels;

import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.BossTwo;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelThree;
import com.example.demo.components.SoundComponent;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01; // Power-up spawn probability
    private static final double HEART_SPAWN_PROBABILITY = 0.005; // Heart item spawn probability
    private BossTwo boss;
    private LevelViewLevelThree levelView;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);
        boss = new BossTwo(getRoot(), this);
        SoundComponent.stopAllSound();
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss.isDestroyed()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }

        // Randomly spawn power-ups
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            double x = getScreenWidth(); // Spawn from the right side of the screen
            double y = Math.random() * getEnemyMaximumYPosition(); // Random Y coordinate
            ActorLevelUp powerUp = new ActorLevelUp(x, y);
            powerUps.add(powerUp);
            getRoot().getChildren().add(powerUp);
        }

        if (Math.random() < HEART_SPAWN_PROBABILITY) { // 定义一个合适的生成概率，例如 0.02
            double x = getScreenWidth(); // 从屏幕右侧生成
            double y = Math.random() * getEnemyMaximumYPosition(); // 随机Y坐标
            HeartItem heart = new HeartItem(x, y);
            powerUps.add(heart); // 如果已有 powerUps 列表用于存放道具
            getRoot().getChildren().add(heart);
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }
}
