package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * WinImage represents the visual display of a "You Win" screen in the game.
 * It is displayed when the player successfully completes the game.
 */
public class WinImage extends ImageView {

    // Constants for the win image properties
    private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png"; // Path to the win image resource
    private static final int HEIGHT = 500; // Height of the win image
    private static final int WIDTH = 600;  // Width of the win image

    /**
     * Constructor for WinImage.
     * Initializes the image, sets its size, and positions it on the screen.
     *
     * @param xPosition The X coordinate for positioning the win image.
     * @param yPosition The Y coordinate for positioning the win image.
     */
    public WinImage(double xPosition, double yPosition) {
        // Set the win image
        this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

        // Make the image initially invisible
        this.setVisible(false);

        // Set the size of the image
        this.setFitHeight(HEIGHT);
        this.setFitWidth(WIDTH);

        // Position the image on the screen
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
    }

    /**
     * Displays the win image by making it visible.
     * This method is typically called when the player wins the game.
     */
    public void showWinImage() {
        this.setVisible(true); // Make the win image visible
    }
}
