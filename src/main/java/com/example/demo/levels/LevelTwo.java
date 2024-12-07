package com.example.demo.levels;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.Boss;
import com.example.demo.actors.HeartItem;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;
import com.example.demo.components.SoundComponent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.005;
    private static final double HEART_SPAWN_PROBABILITY = 0.005;
    private static final int PLAYER_INITIAL_HEALTH = 1;
    private static final int PLAYER_MAX_HEALTH = 2;
    private final Boss boss;
    private LevelViewLevelTwo levelView;
    private boolean transitioningToNextLevel = false;
    private boolean bossdownSoundFinished = false;
    private boolean bossExplosionFinished = false;

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel2Sound();
        boss = new Boss(getRoot(), this);
        boss.setOnExplosionFinished(() -> {
            bossExplosionFinished = true;
            checkIfReadyToProceed();
        });
        getUser().getHealthComponent().setMaxHealth(PLAYER_MAX_HEALTH);
//        getUser().getHealthComponent().setCurrentHealth(PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            if (getRoot().getChildren().contains(pauseButton)) {
                getRoot().getChildren().remove(pauseButton);
            }
            loseGame();
        } else if (boss.isDestroyed() && !transitioningToNextLevel) {
            transitioningToNextLevel = true;
            isInputEnabled = false;
            getUser().stopShooting();
            SoundComponent.playBossdownSound(() -> {
                bossdownSoundFinished = true;
                checkIfReadyToProceed();
            });
        }
    }

    private void checkIfReadyToProceed() {
        if (bossdownSoundFinished && bossExplosionFinished) {
            Platform.runLater(() -> {
                if (getRoot().getChildren().contains(pauseButton)) {
                    getRoot().getChildren().remove(pauseButton);
                }
                clearAllProjectiles();
                double offScreenX = getScreenWidth() + 100;
                Timeline exitTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(getUser().layoutXProperty(), getUser().layoutXProperty().getValue())),
                    new KeyFrame(Duration.millis(1000), event -> goToNextLevel("com.example.demo.levels.LevelThree"), new KeyValue(getUser().layoutXProperty(), offScreenX, Interpolator.EASE_IN))
                );
                exitTimeline.play();
            });
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled) return; // 如果输入被禁用，则暂时不生成敌人
        if (getCurrentNumberOfEnemies() == 0) {
            // 创建并添加Boss
            addEnemyUnit(boss); // 此时boss已加入root

            // 记录Boss最终的X位置（假设Boss构造中使用了固定的INITIAL_X_POSITION = 1000.0）
            double bossFinalX = boss.getLayoutX();
            // 将Boss初始位置设在屏幕右侧外部
            double offScreenStartX = getScreenWidth() + 100;
            boss.setLayoutX(offScreenStartX);

            // 创建KeyValue，让Boss从offScreenStartX移动到bossFinalX
            KeyValue kv = new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN);

            // 创建KeyFrame，在1秒内完成移动（1000毫秒）
            KeyFrame kf = new KeyFrame(Duration.millis(800), event -> {
                // 动画结束后可执行回调，例如开始Boss的射击或移动逻辑
                // 例如:
                // boss.startShooting();
                // 当然如果boss默认就会射击，可以在Boss类构造中暂时stopFiring()，此处再startFiring()
            }, kv);

            // 创建Timeline并播放动画
            Timeline bossIntroTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                    kf
            );
            bossIntroTimeline.play();
        }
        spawnPowerUps();
    }

    private void spawnPowerUps() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            addPowerUp(new ActorLevelUp(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
        if (Math.random() < HEART_SPAWN_PROBABILITY) {
            addPowerUp(new HeartItem(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
    }

    private void addPowerUp(ActiveActor powerUp) {
        powerUps.add(powerUp);
        getRoot().getChildren().add(powerUp);
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_MAX_HEALTH);
        return levelView;
    }
}