package com.example.demo.actors;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract class representing an active actor in the game.
 * Extends ImageView to include graphical representation and movement functionality.
 */
public abstract class ActiveActor extends ImageView {

    // Path to the directory containing actor images
    private static final String IMAGE_LOCATION = "/com/example/demo/images/";
    
    // Margin to reduce the hitbox size for collision detection
    private static final int HITBOX_MARGIN = 40;

    /**
     * Constructor for ActiveActor.
     * Sets up the actor's image, size, and initial position.
     *
     * @param imageName    Name of the image file representing the actor
     * @param imageHeight  Height of the image (will scale proportionally)
     * @param initialXPos  Initial X position of the actor on the screen
     * @param initialYPos  Initial Y position of the actor on the screen
     */
    public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        // Load the image for the actor
        this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
        
        // Set the initial position of the actor
        this.setLayoutX(initialXPos);
        this.setLayoutY(initialYPos);
        
        // Set the size and maintain aspect ratio of the image
        this.setFitHeight(imageHeight);
        this.setPreserveRatio(true);
    }

    /**
     * Abstract method to be implemented by subclasses to update the position of the actor.
     * This defines the movement logic specific to each actor type.
     */
    public abstract void updatePosition();

    /**
     * Moves the actor horizontally by a specified amount.
     *
     * @param horizontalMove Amount to move horizontally (positive or negative)
     */
    protected void moveHorizontally(double horizontalMove) {
        this.setTranslateX(getTranslateX() + horizontalMove);
    }

    /**
     * Moves the actor vertically by a specified amount.
     *
     * @param verticalMove Amount to move vertically (positive or negative)
     */
    protected void moveVertically(double verticalMove) {
        this.setTranslateY(getTranslateY() + verticalMove);
    }

    /**
     * Calculates a reduced bounding box for the actor.
     * The reduced bounds are used for collision detection with a smaller hitbox.
     *
     * @return A {@link Bounds} object representing the reduced hitbox of the actor
     */
    public Bounds getReducedBounds() {
        // Get the original bounds of the actor's image
        Bounds originalBounds = this.getBoundsInLocal();
        
        // Return a reduced bounding box with margins applied
        return new BoundingBox(
            originalBounds.getMinX() + HITBOX_MARGIN,  // Adjust left margin
            originalBounds.getMinY() + HITBOX_MARGIN,  // Adjust top margin
            originalBounds.getWidth() - 2 * HITBOX_MARGIN, // Reduce width by margins
            originalBounds.getHeight() - 2 * HITBOX_MARGIN // Reduce height by margins
        );
    }
}
