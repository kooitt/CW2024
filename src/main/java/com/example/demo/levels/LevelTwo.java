package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.Boss;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.views.LevelView;
import com.example.demo.components.SoundComponent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";
    private static final double POWER_UP_PROBABILITY = 0.005;
    private static final double HEART_SPAWN_PROBABILITY = 0.005;
    private static final int MAX_HEALTH = 2;
    private final Boss boss;
    private boolean transitioningToNextLevel = false;
    private boolean bossdownSoundFinished = false;
    private boolean bossExplosionFinished = false;

    public LevelTwo(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE, screenHeight, screenWidth, controller);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel2Sound();
        boss = new Boss(getRoot(), this);
        boss.setOnExplosionFinished(() -> {
            bossExplosionFinished = true;
            checkIfReadyToProceed();
        });
        getUser().getHealthComponent().setMaxHealth(MAX_HEALTH);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            if (boss != null) {
                boss.stopShieldTimeline();
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
                getRoot().getChildren().remove(pauseButton);
                clearAllProjectiles();
                double offScreenX = getScreenWidth() + 100;
                Timeline exitTimeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(getUser().layoutXProperty(), getUser().layoutXProperty().getValue())),
                        new KeyFrame(Duration.millis(1000), event -> goToNextLevel(NEXT_LEVEL), new KeyValue(getUser().layoutXProperty(), offScreenX, Interpolator.EASE_IN))
                );
                exitTimeline.play();
            });
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled) return;
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            animateBossEntry();
        }
        spawnPowerUps();
    }

    private void animateBossEntry() {
        double bossFinalX = boss.getLayoutX();
        double offScreenStartX = getScreenWidth() + 100;
        boss.setLayoutX(offScreenStartX);
        Timeline bossIntroTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                new KeyFrame(Duration.millis(800), new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN))
        );
        bossIntroTimeline.play();
    }

    private void spawnPowerUps() {
        if (Math.random() < POWER_UP_PROBABILITY) {
            addPowerUp(new ActorLevelUp(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
        if (getUser().getCurrentHealth() < getUser().getMaxHealth() && Math.random() < HEART_SPAWN_PROBABILITY) {
            addPowerUp(new HeartItem(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
    }

    private void addPowerUp(Actor powerUp) {
        getRoot().getChildren().add(powerUp);
        powerUps.add(powerUp);
    }

    @Override
    protected LevelView NewLevelView(int hearts) {
        return super.NewLevelView(2);
    }
}