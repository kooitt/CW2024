package com.example.demo.levels;

import com.example.demo.planes.Boss;
import com.example.demo.levelviews.LevelView;
import com.example.demo.levelviews.LevelViewBoss;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.Levels.LevelThree";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private LevelViewBoss levelView;

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        boss = new Boss(levelView);
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
            goToNextLevel(NEXT_LEVEL);
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewBoss(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

}
