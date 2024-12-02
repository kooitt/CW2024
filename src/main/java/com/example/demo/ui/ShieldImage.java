package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ShieldImage represents the visual appearance of a shield in the game.
 * It is designed to be displayed over a game object, such as the boss plane,
 * when the shield is active.
 */
public class ShieldImage extends ImageView {

    // Constants for shield properties
    private static final int SHIELD_SIZE = 200; // Size of the shield (both width and height)
    private static final String IMAGE_NAME = "/com/example/demo/images/shield.png"; // Path to the shield image resource

    /**
     * Constructor for ShieldImage.
     *
     * @param xPosition The X position where the shield image should be displayed.
     * @param yPosition The Y position where the shield image should be displayed.
     */
    public ShieldImage(double xPosition, double yPosition) {
        // Set the position of the shield
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);

        // Load and set the shield image
        this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

        // Set the shield's size (width and height)
        this.setFitHeight(SHIELD_SIZE);
        this.setFitWidth(SHIELD_SIZE);

        // Initially make the shield invisible
        this.setVisible(false);
    }

    /**
     * Displays the shield by making it visible.
     * This method is typically called when the shield is activated in the game.
     */
    public void showShield() {
        this.setVisible(true); // Make the shield visible
        this.toFront(); // Ensure the shield is drawn above other game elements
    }

    /**
     * Hides the shield by making it invisible.
     * This method is typically called when the shield is deactivated in the game.
     */
    public void hideShield() {
        this.setVisible(false); // Make the shield invisible
    }
}
