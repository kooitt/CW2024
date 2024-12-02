package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EnemyPlane;

import javafx.stage.Stage;

/**
 * LevelTwo represents the second level of the game.
 * It introduces more enemies and a higher kill target to advance to the next level.
 */
public class LevelTwo extends LevelParent {

    // Background image for LevelTwo
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/space2.jpg";

    // Number of enemies to spawn in this level
    private static final int TOTAL_ENEMIES = 7;

    // Kill count required to transition to the next level
    private static final int KILLS_TO_ADVANCE = 15;

    // Probability of spawning an enemy on each frame
    private static final double ENEMY_SPAWN_PROBABILITY = 0.25;

    /**
     * Constructor for LevelTwo.
     *
     * @param screenHeight Height of the game screen.
     * @param screenWidth  Width of the game screen.
     * @param stage        The main game stage.
     */
    public LevelTwo(double screenHeight, double screenWidth, Stage stage) {
        // Initialize LevelParent with the background, screen dimensions, player health, and stage
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, 5, stage);
    }

    /**
     * Checks if the game is over based on the player's status or kill target.
     * Ends the game if the player is destroyed or transitions to the next level if the kill target is met.
     */
    @Override
    protected void checkIfGameOver() {
        // Check if the player is destroyed
        if (userIsDestroyed()) {
            loseGame(); // End the game with a loss
        }
        // Check if the player's kill count has reached the target to advance
        else if (userHasReachedKillTarget()) {
            goToNextLevel("com.example.demo.levels.LevelBoss"); // Transition to the boss level
        }
        // Debugging output to track player's progress
        System.out.println("Kill Target Reached: " + getUser().getNumberOfKills());
    }

    /**
     * Initializes the friendly units for the level (e.g., the player's plane).
     * Adds the user's plane to the game root.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser()); // Add the user plane to the game scene
    }

    /**
     * Spawns enemy units for the level based on spawn probability.
     * Ensures the number of enemies does not exceed the total allowed.
     */
    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies(); // Get the current number of enemies
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) { // Check spawn probability
                double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition(); // Random Y position
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition); // Create a new enemy
                addEnemyUnit(newEnemy); // Add the enemy to the game
            }
        }
    }

    /**
     * Instantiates the LevelView for LevelTwo.
     *
     * @return A LevelView instance specific to this level.
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), 5); // Create and return the LevelView with 5 hearts (player health)
    }

    /**
     * Checks if the player's kill count has reached the target to advance to the next level.
     *
     * @return True if the player's kill count meets or exceeds the target, otherwise false.
     */
    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
