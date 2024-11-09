package com.example.demo;

public class LevelTwo extends LevelParent{
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.LevelBoss";
    private static final String NEXT_LEVEL_NAME = "Boss Level";
    private static final int TOTAL_ENEMIES = 8;
    private static final int KILLS_TO_ADVANCE = 15;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double ENEMY_Y_UPPER_BOUND = 100;

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        }
        else if (userHasReachedKillTarget())
            goToNextLevel(NEXT_LEVEL, NEXT_LEVEL_NAME);
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
                double newEnemyInitialYPosition = 25 + Math.random() * (getEnemyMaximumYPosition() - ENEMY_Y_UPPER_BOUND);
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
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

