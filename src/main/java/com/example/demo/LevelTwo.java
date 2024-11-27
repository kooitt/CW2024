package com.example.demo;

import javafx.stage.Stage;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/space2.jpg";
    private static final int TOTAL_ENEMIES = 7; // Number of enemies to spawn
    private static final int KILLS_TO_ADVANCE = 15; // Kill count required to move to the next level
    private static final double ENEMY_SPAWN_PROBABILITY = 0.25; // Probability for spawning enemies

    public LevelTwo(double screenHeight, double screenWidth, Stage stage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, 5, stage); // Initialize LevelParent with 5 player health
    }

    @Override
    protected void checkIfGameOver() {
        // Check if the player is destroyed
        if (userIsDestroyed()) {
            loseGame();
        }
        // Check if the player has reached the kill target to transition to the next level
        else if (userHasReachedKillTarget()) {
            goToNextLevel("com.example.demo.LevelBoss"); // Transition to LevelBoss
        }
        System.out.println("Kill Target Reached: " + getUser().getNumberOfKills());
    }

    @Override
    protected void initializeFriendlyUnits() {
        // Add the user plane to the game root
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
            }
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        // Create a LevelView specific to this level
        return new LevelView(getRoot(), 5); // Display 5 hearts (player health)
    }

    // Helper method to check if the user has reached the kill target
    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
