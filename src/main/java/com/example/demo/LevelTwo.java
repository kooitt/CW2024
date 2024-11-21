package com.example.demo;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/space2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private LevelViewLevelTwo levelView;

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        // Initialize the boss enemy
        boss = new Boss();
    }

    @Override
    protected void initializeFriendlyUnits() {
        // Add the user plane to the game root
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        // Check if the player is destroyed
        if (userIsDestroyed()) {
            loseGame();
        }
        // Check if the boss has been destroyed to declare victory
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
        // Create a LevelViewLevelTwo specific to this level
        levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }

    // Additional functionality for managing the boss shield
    public void updateShieldState() {
        if (boss.isDestroyed()) return; // Skip if the boss is already destroyed
        if (boss.isShielded()) {
            levelView.showShield();
        } else {
            levelView.hideShield();
        }
    }

    @Override
    protected void updateScene() {
        super.updateScene(); // Call the parent class method
        updateShieldState(); // Add specific functionality for LevelTwo
    }

}
