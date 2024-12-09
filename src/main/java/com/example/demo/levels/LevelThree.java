package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.BossTwo;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01;
    private static final double HEART_SPAWN_PROBABILITY = 0.005;
    private final BossTwo boss;

    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        boss = new BossTwo(getRoot(), this);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel3Sound();
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

    private void animateBossTwoEntry() {
        // 设置BossTwo初始的X坐标在屏幕外的右边
        double bossFinalX = boss.getLayoutX();
        double offScreenStartX = getScreenWidth() + 100;
        boss.setLayoutX(offScreenStartX);

        // 创建入场动画Timeline
        Timeline bossTwoIntroTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                new KeyFrame(
                        Duration.millis(1000), // 动画持续时间
                        new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN) // 从屏幕外飞到目标位置
                )
        );

        bossTwoIntroTimeline.play();
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            animateBossTwoEntry(); // 调用入场动画
        }
        spawnPowerUps();
    }


    private void spawnPowerUps() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            addPowerUp(new ActorLevelUp(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
        if (getUser().getCurrentHealth() < getUser().getMaxHealth() && Math.random() < HEART_SPAWN_PROBABILITY) {
            addPowerUp(new HeartItem(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
    }

    private void addPowerUp(Actor powerUp) {
        powerUps.add(powerUp);
        getRoot().getChildren().add(powerUp);
    }
}