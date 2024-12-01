// LevelThree.java
package com.example.demo.levels;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.BossTwo;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelThree;

/**
 * Represents the third level of the game with a new boss fight.
 */
public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01; // Power-up spawn probability
    private BossTwo boss;
    private LevelViewLevelThree levelView;

    /**
     * Constructs LevelThree with specified screen dimensions.
     *
     * @param screenHeight screen height.
     * @param screenWidth  screen width.
     */
    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);
        boss = new BossTwo(getRoot(), this);
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
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelThree(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }
}
