package com.example.demo.levels;

import com.example.demo.actors.planes.PlaneFactory;
import com.example.demo.actors.planes.bosses.MonstrousNightmare;

public class LevelFour extends LevelParent{

    private static final String BACKGROUND_IMAGE_NAME = "background4.jpg";
    private static final String BGM_NAME = "/com/example/demo/audio/httyd.mp3";
    //private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    //private final Boss boss;
    private final MonstrousNightmare monstrousNightmare;;
    private LevelFourView levelView;

    public LevelFour(double screenHeight, double screenWidth) {
        super(
                BACKGROUND_IMAGE_NAME,
                //BGM_NAME,
                screenHeight,
                screenWidth,
                PLAYER_INITIAL_HEALTH,
                () -> PlaneFactory.createToothless());
        //boss = new Boss();
        monstrousNightmare = PlaneFactory.createMonstrousNightmare();
    }

    @Override
    public void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
        getRoot().getChildren().add(monstrousNightmare.getShieldImage());
        getRoot().getChildren().add(monstrousNightmare.getHitbox());
    }

    @Override
    public void checkIfGameOver() {
        if (userIsDestroyed()) {
            getController().loseGame();
        }
        else if (monstrousNightmare.isDestroyed()) {
            System.out.println("level4");
            getController().winGame();
            //goToNextLevel(NEXT_LEVEL);
        }
    }

    @Override
    public void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(monstrousNightmare);
        }
    }

    @Override
    public LevelParentView instantiateLevelView() {
        levelView = new LevelFourView(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }
}
