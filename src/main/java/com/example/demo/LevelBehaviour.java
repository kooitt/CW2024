package com.example.demo;

public interface LevelBehaviour {
    void initializeFriendlyUnits();
    void checkIfGameOver();
    void spawnEnemyUnits();
    LevelParentView instantiateLevelView();
}
