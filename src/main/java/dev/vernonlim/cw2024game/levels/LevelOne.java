package dev.vernonlim.cw2024game.levels;

import dev.vernonlim.cw2024game.ActiveActorDestructible;
import dev.vernonlim.cw2024game.EnemyPlane;
import dev.vernonlim.cw2024game.LevelView;
import dev.vernonlim.cw2024game.controller.Controller;

public class LevelOne extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/images/background1.jpg";
    private static final String NEXT_LEVEL = "LEVEL_TWO";
    private static final int TOTAL_ENEMIES = 5;
    private static final int KILLS_TO_ADVANCE = 10;
    private static final double ENEMY_SPAWN_PROBABILITY = .20;
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelOne(Controller controller, double screenHeight, double screenWidth) {
        super(controller, BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
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
    protected void spawnEnemyUnits(double deltaTime) {
        timeSinceLastEnemySpawn += deltaTime;

        // simulates the original 50ms tickrate
        if (timeSinceLastEnemySpawn > 50.0f) {
            int currentNumberOfEnemies = getCurrentNumberOfEnemies();
            for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
                if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                    double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                    ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
                    addEnemyUnit(newEnemy);
                }
            }

            timeSinceLastEnemySpawn = 0;
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
