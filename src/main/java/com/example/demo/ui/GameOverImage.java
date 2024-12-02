package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * GameOverImage represents the "Game Over" screen image displayed when the player loses the game.
 * It extends the ImageView class to handle the image and its positioning.
 */
public class GameOverImage extends ImageView {

    // Path to the "Game Over" image file
    private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

    /**
     * Constructor for GameOverImage.
     * Sets up the "Game Over" image and its position on the screen.
     *
     * @param xPosition The X coordinate for the image's position.
     * @param yPosition The Y coordinate for the image's position.
     */
    public GameOverImage(double xPosition, double yPosition) {
        // Set the image for the "Game Over" screen
        setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

        // Set the position of the image on the screen
        setLayoutX(xPosition);
        setLayoutY(yPosition);
    }
}
