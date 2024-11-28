package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.planes.EnemyPlane;
import com.example.demo.planes.AdvancedEnemyPlane;
import com.example.demo.levelviews.LevelView;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.Levels.LevelFour";
    private static final int TOTAL_ENEMIES = 5;
    private static final int KILLS_TO_ADVANCE = 20;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int PLAYER_INITIAL_HEALTH = 5;

    private boolean spawnEnemyPlaneNext = true;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (userHasReachedKillTarget()) {
            goToNextLevel(NEXT_LEVEL);
        }
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();

                ActiveActorDestructible newEnemy;
                if (spawnEnemyPlaneNext) {
                    newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
                } else {
                    newEnemy = new AdvancedEnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
                }

                spawnEnemyPlaneNext = !spawnEnemyPlaneNext;

                addEnemyUnit(newEnemy);
            }
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
