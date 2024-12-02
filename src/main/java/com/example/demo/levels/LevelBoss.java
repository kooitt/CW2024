package com.example.demo.levels;

import com.example.demo.actors.Boss;

import javafx.stage.Stage;

/**
 * LevelBoss represents the boss level in the game.
 * It manages the boss, the player, and the interactions specific to this level,
 * including shield mechanics and the boss health display.
 */
public class LevelBoss extends LevelParent {

    // Background image for the level
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/space3.jpg";

    // Initial health of the player's plane
    private static final int PLAYER_INITIAL_HEALTH = 5;

    // Instance of the boss plane
    private final Boss boss;

    // Specialized view for the boss level
    private LevelViewBoss levelView;

    /**
     * Constructor for LevelBoss.
     *
     * @param screenHeight Height of the game screen.
     * @param screenWidth  Width of the game screen.
     * @param stage        The main game stage.
     */
    public LevelBoss(double screenHeight, double screenWidth, Stage stage) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage);

        // Initialize the boss with a reference to this LevelBoss
        boss = new Boss(this);
    }

    /**
     * Initializes the friendly units for the level (e.g., the player's plane).
     */
    @Override
    protected void initializeFriendlyUnits() {
        // Add the user plane to the game root
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over based on the destruction of the player or the boss.
     */
    @Override
    protected void checkIfGameOver() {
        // Check if the user is destroyed
        if (userIsDestroyed()) {
            loseGame(); // End the game with a loss
        }
        // Check if the boss is destroyed to declare victory
        else if (boss.isDestroyed()) {
            winGame(); // End the game with a win
        }
    }

    /**
     * Spawns the enemy units for the level, which in this case is just the boss.
     */
    @Override
    protected void spawnEnemyUnits() {
        // Add the boss to the game root if no enemies are currently present
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }
    }

    /**
     * Instantiates the LevelView for this level, which is specific to the boss.
     *
     * @return An instance of LevelViewBoss.
     */
    @Override
    protected LevelView instantiateLevelView() {
        // Create a LevelViewBoss specific to this level
        levelView = new LevelViewBoss(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    /**
     * Updates the scene during each frame of the game.
     * This includes calling the parent class's update logic and additional boss-specific updates.
     */
    @Override
    protected void updateScene() {
        super.updateScene(); // Call the parent class's update logic
        updateShieldState(); // Update the shield state for the boss
    }

    /**
     * Updates the shield display for the boss.
     * Displays the shield if active, otherwise hides it.
     */
    public void updateShieldState() {
        if (boss.isDestroyed()) return; // Do nothing if the boss is already destroyed
        if (boss.isShielded()) {
            levelView.showShield(); // Display the shield
        } else {
            levelView.hideShield(); // Hide the shield
        }
    }

    /**
     * Updates the boss's health display in the LevelViewBoss.
     *
     * @param health The current health of the boss.
     */
    public void updateBossHealthDisplay(int health) {
        // Update the boss's health in the view if levelView is a LevelViewBoss
        if (levelView instanceof LevelViewBoss) {
            ((LevelViewBoss) levelView).updateBossHealth(health);
        }
    }

    /**
     * Retrieves the specialized LevelViewBoss instance for this level.
     *
     * @return The LevelViewBoss instance.
     */
    public LevelViewBoss getLevelViewBoss() {
        // Cast and return the level view as a LevelViewBoss
        return (LevelViewBoss) levelView;
    }
}
