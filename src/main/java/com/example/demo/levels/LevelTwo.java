package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.planes.EnemyPlaneTwo;
import com.example.demo.factory.EnemyFactory;
import com.example.demo.view.LevelView;

/**
 * Represents the second level in the game.
 */
public class LevelTwo extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelBoss";
    private static final String NEXT_LEVEL_NAME = "Boss Level";
    private static final int TOTAL_ENEMIES = 7;
    private static final int KILLS_TO_ADVANCE = 10;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double ENEMY_Y_UPPER_BOUND = 100;
    private final EnemyFactory enemyFactory;


    /**
     * Constructs a LevelTwo with the specified screen dimensions.
     *
     * @param screenHeight the height of the screen.
     * @param screenWidth the width of the screen.
     */
    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        this.enemyFactory = new EnemyFactory(EnemyFactory.EnemyType.ENEMYPLANETWO);
    }

    /**
     * Checks if the game is over by evaluating the state of the user and the kill target.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (userHasReachedKillTarget()) {
            goToNextLevel(NEXT_LEVEL, NEXT_LEVEL_NAME);
        }
    }


    /**
     * Spawns enemy units in the level based on the spawn probability.
     */
    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {

                double newEnemyInitialYPosition = ENEMY_Y_UPPER_BOUND + Math.random() * (getEnemyMaximumYPosition() - ENEMY_Y_UPPER_BOUND);
                ActiveActorDestructible newEnemy = enemyFactory.createActor(getScreenWidth(), newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
            }
        }
    }

    /**
     * Instantiates the view for the second level.
     *
     * @return the view for the second level.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, KILLS_TO_ADVANCE);
    }

    /**
     * Checks if the user has reached the kill target to advance to the next level.
     *
     * @return true if the user has reached the kill target, false otherwise.
     */
    private boolean userHasReachedKillTarget() {
        return getUser().getKillCount() >= KILLS_TO_ADVANCE;
    }
}