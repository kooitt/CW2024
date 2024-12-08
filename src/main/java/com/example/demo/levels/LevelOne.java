package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.EnemyPlane;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.views.LevelView;
import com.example.demo.components.SoundComponent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.animation.Interpolator;

public class LevelOne extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
    private static final int TOTAL_ENEMIES = 5;
    private static final int KILLS_TO_ADVANCE = 20;
    private static final double ENEMY_SPAWN_PROBABILITY = 0.20;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01;
    private static final double HEART_SPAWN_PROBABILITY = 0.005;

    private boolean transitioningToNextLevel = false;

    public LevelOne(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel1Sound();
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (userHasReachedKillTarget() && !transitioningToNextLevel) {
            transitioningToNextLevel = true;
            isInputEnabled = false;
            getUser().stopShooting();
            SoundComponent.stopLevel1Sound();
            checkIfReadyToProceed();
        }
    }

    private void checkIfReadyToProceed() {
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

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled) return;
        int currentEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                addEnemyUnit(new EnemyPlane(getScreenWidth(), Math.random() * getEnemyMaximumYPosition(), getRoot()));
            }
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
        getRoot().getChildren().add(powerUp);
        powerUps.add(powerUp);
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}