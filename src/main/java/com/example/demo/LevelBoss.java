package com.example.demo;

import javafx.stage.Stage;

public class LevelBoss extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/space3.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;

    private final Boss boss;
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

    @Override
    protected void initializeFriendlyUnits() {
        // Add the user plane to the game root
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        // Check if the user is destroyed
        if (userIsDestroyed()) {
            loseGame();
        }
        // Check if the boss is destroyed to declare victory
        else if (boss.isDestroyed()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        // Ensure only one boss is added as the enemy
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        // Create a LevelViewBoss specific to this level
        levelView = new LevelViewBoss(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    @Override
    protected void updateScene() {
        super.updateScene(); // Call the parent class's updateScene method
        updateShieldState(); // Update the shield state of the boss
    }

    /**
     * Updates the shield display for the boss.
     */
    public void updateShieldState() {
        if (boss.isDestroyed()) return; // Skip if the boss is already destroyed
        if (boss.isShielded()) {
            levelView.showShield();
        } else {
            levelView.hideShield();
        }
    }

    /**
     * Updates the boss's health display in the LevelViewBoss.
     *
     * @param health The current health of the boss.
     */
    public void updateBossHealthDisplay(int health) {
        if (levelView instanceof LevelViewBoss) {
            ((LevelViewBoss) levelView).updateBossHealth(health);
        }
    }

    /**
     * Custom method to disable kill count updates for the Boss level.
     */
    protected void updateKillCount() {
        // Do nothing to disable score updates
    }

    /**
     * Custom method to disable score display updates for the Boss level.
     */
    protected void updateScoreDisplay() {
        // Do nothing to disable score display
    }
}
