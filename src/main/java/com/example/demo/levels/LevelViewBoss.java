package com.example.demo.levels;

import com.example.demo.ui.ShieldImage;

import javafx.scene.Group;
import javafx.scene.text.Text;

/**
 * LevelViewBoss is a specialized view class for displaying elements specific to the boss level.
 * This includes the boss health display and the shield image for the boss.
 */
public class LevelViewBoss extends LevelView {

    // Constants for positioning UI elements specific to the boss level
    private static final double BOSS_HEALTH_X_POSITION = 1100; // X position for the boss health display
    private static final double BOSS_HEALTH_Y_POSITION = 50;   // Y position for the boss health display
    private static final int SHIELD_X_POSITION = 1150;         // X position for the shield image
    private static final int SHIELD_Y_POSITION = 500;          // Y position for the shield image

    // Text element for displaying the boss's health
    private final Text bossHealthDisplay;

    // Visual representation of the boss's shield
    private final ShieldImage shieldImage;

    /**
     * Constructor for LevelViewBoss.
     *
     * @param root            The root group of the level, where all visual elements are added.
     * @param heartsToDisplay Number of hearts to display for the player.
     */
    public LevelViewBoss(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay); // Call the constructor of the parent LevelView class

        // Initialize the boss health display text
        bossHealthDisplay = new Text(BOSS_HEALTH_X_POSITION, BOSS_HEALTH_Y_POSITION, "Boss Health: 100");
        bossHealthDisplay.setStyle("-fx-font-size: 24px; -fx-fill: white;"); // Set font size and color

        // Initialize the shield image with specific coordinates
        shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);

        // Add the shield image to the root group
        root.getChildren().add(shieldImage);

        // Add the boss health display text to the root group
        root.getChildren().add(bossHealthDisplay);
    }

    /**
     * Updates the boss's health display on the screen.
     *
     * @param health The current health of the boss.
     */
    public void updateBossHealth(int health) {
        bossHealthDisplay.setText("Boss Health: " + health); // Update the health text
        bossHealthDisplay.toFront(); // Ensure the health display is always on top of other elements
    }

    /**
     * Displays the boss's shield by making the ShieldImage visible.
     * The shield image is brought to the front to render above other elements.
     */
    public void showShield() {
        shieldImage.showShield(); // Make the shield visible
        shieldImage.toFront(); // Bring the shield to the front of the scene
    }

    /**
     * Hides the boss's shield by making the ShieldImage invisible.
     */
    public void hideShield() {
        shieldImage.hideShield(); // Make the shield invisible
    }

    /**
     * Retrieves the ShieldImage instance representing the boss's shield.
     *
     * @return The ShieldImage instance.
     */
    public ShieldImage getShieldImage() {
        return shieldImage; // Return the shield image instance
    }
}
