package com.example.demo.levels;

import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.Boss;
import com.example.demo.actors.HeartItem;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;
import com.example.demo.components.SoundComponent;
import javafx.application.Platform;

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
        getUser().getHealthComponent().setCurrentHealth(PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss.isDestroyed() && !transitioningToNextLevel) {
            transitioningToNextLevel = true;
            isInputEnabled = false;
            user.stopShooting();
            bossdownSoundFinished = false;
            SoundComponent.playBossdownSound(() -> {
                bossdownSoundFinished = true;
                checkIfReadyToProceed();
            });
        }
    }

    private void checkIfReadyToProceed() {
        if (bossdownSoundFinished && bossExplosionFinished) {
            Platform.runLater(() -> goToNextLevel("com.example.demo.levels.LevelThree"));
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled) return;
        if (getCurrentNumberOfEnemies() == 0) addEnemyUnit(boss);
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            double x = getScreenWidth();
            double y = Math.random() * getEnemyMaximumYPosition();
            ActorLevelUp powerUp = new ActorLevelUp(x, y);
            powerUps.add(powerUp);
            getRoot().getChildren().add(powerUp);
        }
        if (Math.random() < HEART_SPAWN_PROBABILITY) {
            double x = getScreenWidth();
            double y = Math.random() * getEnemyMaximumYPosition();
            HeartItem heart = new HeartItem(x, y);
            powerUps.add(heart);
            getRoot().getChildren().add(heart);
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_MAX_HEALTH);
        return levelView;
    }
}