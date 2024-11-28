package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.factory.EnemyFactory;
import com.example.demo.view.LevelView;
import com.example.demo.config.GameConfig;
import com.example.demo.managers.LeaderboardManager;

public class ArcadeLevel extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final int INITIAL_ENEMIES = 5;
    private static final int MAXIMUM_ENEMIES = 8;
    private static final double INITIAL_SPAWN_RATE = 0.25;
    private int totalEnemies;
    private double spawnRate;
    private int currentWave;
    private final EnemyFactory enemyFactory;

    public ArcadeLevel() {
        super(BACKGROUND_IMAGE_NAME, GameConfig.PLAYER_INITIAL_HEALTH);
        this.enemyFactory = new EnemyFactory(EnemyFactory.EnemyType.ENEMYPLANEONE);
        this.totalEnemies = INITIAL_ENEMIES;
        this.spawnRate = INITIAL_SPAWN_RATE;
        this.currentWave = 1;
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            // Save score before ending game
            saveScore(getUser().getKillCount());
            loseGame();
        } else if (getUser().getKillCount() >= totalEnemies) {
            nextWave();
        }
    }

    private void nextWave() {
        currentWave++;
        totalEnemies += 1;
        spawnRate = Math.min(spawnRate + 0.05, 0.75); // Cap at 75% spawn rate
    }

    @Override
    protected void spawnEnemyUnits() {
        int currentEnemies = getCurrentNumberOfEnemies();
        int maxEnemies = Math.min(totalEnemies, MAXIMUM_ENEMIES);
        for (int i = 0; i < maxEnemies- currentEnemies; i++) {
            if (Math.random() < spawnRate) {
                double yPos = 30 + Math.random() * getEnemyMaximumYPosition();
                ActiveActorDestructible enemy = enemyFactory.createActor(getScreenWidth(), yPos);
                addEnemyUnit(enemy);
            }
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), GameConfig.PLAYER_INITIAL_HEALTH, totalEnemies);
    }

    private void saveScore(int score) {
        LeaderboardManager.addScore(score);
    }

}