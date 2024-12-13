package com.example.demo.levels;

public interface LevelBehaviour {

    void initializeFriendlyUnits();

    void checkIfGameOver();

    void spawnEnemyUnits();

    LevelParentView instantiateLevelView();
}
