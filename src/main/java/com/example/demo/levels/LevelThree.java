package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.BossTwo;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.views.LevelView;
import com.example.demo.components.SoundComponent;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01;
    private static final double HEART_SPAWN_PROBABILITY = 0.005;
    private BossTwo boss;

    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
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