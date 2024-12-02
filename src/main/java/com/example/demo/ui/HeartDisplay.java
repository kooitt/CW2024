package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * HeartDisplay represents the UI component for displaying the player's health using heart icons.
 * The hearts are displayed in a horizontal box (HBox) and can be updated dynamically.
 */
public class HeartDisplay {

    // Constants for the heart image and display properties
    private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png"; // Path to the heart image
    private static final int HEART_HEIGHT = 50;                                         // Height of the heart icon
    private static final int INDEX_OF_FIRST_ITEM = 0;                                   // Index for removing the first heart

    // Fields for managing the heart container and its properties
    private HBox container;                // Horizontal container for the hearts
    private double containerXPosition;     // X position of the heart container
    private double containerYPosition;     // Y position of the heart container
    private int numberOfHeartsToDisplay;   // Number of hearts to display initially

    /**
     * Constructor for HeartDisplay.
     * Initializes the heart container and adds the specified number of hearts.
     *
     * @param xPosition        The X position of the heart display container.
     * @param yPosition        The Y position of the heart display container.
     * @param heartsToDisplay  The initial number of hearts to display.
     */
    public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.numberOfHeartsToDisplay = heartsToDisplay;

        // Initialize the container and hearts
        initializeContainer();
        initializeHearts();
    }

    /**
     * Initializes the heart container (HBox) and sets its position on the screen.
     */
    private void initializeContainer() {
        container = new HBox(); // Create a new horizontal box for hearts
        container.setLayoutX(containerXPosition); // Set the X position of the container
        container.setLayoutY(containerYPosition); // Set the Y position of the container
    }

    /**
     * Adds the specified number of heart icons to the container.
     * Each heart icon is represented by an ImageView.
     */
    private void initializeHearts() {
        for (int i = 0; i < numberOfHeartsToDisplay; i++) {
            // Create a new ImageView for the heart icon
            ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
            
            // Set the size and preserve the aspect ratio of the heart icon
            heart.setFitHeight(HEART_HEIGHT);
            heart.setPreserveRatio(true);

            // Add the heart icon to the container
            container.getChildren().add(heart);
        }
    }

    /**
     * Removes one heart from the display.
     * Removes the first heart in the container if it is not empty.
     */
    public void removeHeart() {
        if (!container.getChildren().isEmpty()) { // Check if there are any hearts left
            container.getChildren().remove(INDEX_OF_FIRST_ITEM); // Remove the first heart
        }
    }

    /**
     * Retrieves the container holding the hearts.
     *
     * @return The HBox containing the hearts.
     */
    public HBox getContainer() {
        return container; // Return the heart container
    }
}
