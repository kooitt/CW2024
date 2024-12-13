package com.example.demo.levels;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.planes.PlaneFactory;

public class LevelThree extends LevelParent{

    private static final String BACKGROUND_IMAGE_NAME = "background3.png";
    private static final String BGM_NAME = "/com/example/demo/audio/httyd.mp4";
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelFour";
    private static final int TOTAL_ENEMIES = 5;
    private static final int KILLS_TO_ADVANCE = 10;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelThree(double screenHeight, double screenWidth) {
        super(
                BACKGROUND_IMAGE_NAME,
                //BGM_NAME,
                screenHeight,
                screenWidth,
                PLAYER_INITIAL_HEALTH,
                () -> PlaneFactory.createToothless());
    }

    @Override
    public void checkIfGameOver() {
        if (userIsDestroyed()) {
            getController().loseGame();
        }
        else if (userHasReachedKillTarget()){
            System.out.println("level3");
            goToNextLevel(NEXT_LEVEL);
        }
    }

    @Override
    public void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    public void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                ActiveActorDestructible newEnemy = PlaneFactory.createSeashocker(getScreenWidth(), newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
                getRoot().getChildren().add(newEnemy.getHitbox());
            }
        }
    }

    @Override
    public LevelParentView instantiateLevelView() {
        return new LevelParentView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }


}
