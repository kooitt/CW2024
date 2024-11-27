package com.example.demo;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class LevelViewBoss extends LevelView {

    private static final double BOSS_HEALTH_X_POSITION = 1100; // X position for the boss health display
    private static final double BOSS_HEALTH_Y_POSITION = 50;   // Y position for the boss health display
    private static final int SHIELD_X_POSITION = 1150;         // X position for the shield image
    private static final int SHIELD_Y_POSITION = 500;          // Y position for the shield image

    private final Text bossHealthDisplay;
    private final ShieldImage shieldImage;

    /**
     * Constructor for LevelViewBoss.
     *
     * @param root            The root group of the level.
     * @param heartsToDisplay Number of hearts to display for the player.
     */
    public LevelViewBoss(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);

        // Initialize boss health display
        bossHealthDisplay = new Text(BOSS_HEALTH_X_POSITION, BOSS_HEALTH_Y_POSITION, "Boss Health: 100");
        bossHealthDisplay.setStyle("-fx-font-size: 24px; -fx-fill: white;");

        // Initialize shield image
        shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);

        // Add the shield image first
        root.getChildren().add(shieldImage);

        // Add the boss health display last to ensure it renders on top
        root.getChildren().add(bossHealthDisplay);
    }

    /**
     * Updates the boss's health display.
     *
     * @param health The current health of the boss.
     */
    public void updateBossHealth(int health) {
        bossHealthDisplay.setText("Boss Health: " + health);
        bossHealthDisplay.toFront(); // Ensure the health display is always on top
    }

    /**
     * Shows the boss's shield.
     */
    public void showShield() {
        shieldImage.showShield();
    }

    /**
     * Hides the boss's shield.
     */
    public void hideShield() {
        shieldImage.hideShield();
    }
}
