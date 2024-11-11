package com.example.demo;

import com.example.demo.LevelView;
import com.example.demo.Boss;
import com.example.demo.BossProjectile;
import com.example.demo.LevelViewLevelTwo;

/**
 * LevelTwo represents the second level of the game, introducing a Boss enemy.
 * It extends the LevelParent class, handling level-specific initialization and game-over conditions.
 */
public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private LevelViewLevelTwo levelView;

    /**
     * Constructs a new LevelTwo instance with a background image, screen dimensions, and initial player health.
     *
     * @param screenHeight the height of the game screen
     * @param screenWidth  the width of the game screen
     */
    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        boss = new Boss();  // Initialize the boss for this level
    }

    /**
     * Initializes the friendly units (e.g., player's plane) for the level by adding them to the root node.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over based on the player's or the boss's status.
     * The game is lost if the player is destroyed, and won if the boss is destroyed.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss.isDestroyed()) {
            winGame();
        }
    }

    /**
     * Spawns enemy units in the level. In LevelTwo, only the boss is added as an enemy.
     * The boss is added once at the beginning if no other enemies are present.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);  // Add the boss as the only enemy in this level
        }
    }

    /**
     * Instantiates and returns the LevelView specific to LevelTwo, which may have unique elements like a boss health display.
     *
     * @return the LevelView for LevelTwo
     */
    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }
}
