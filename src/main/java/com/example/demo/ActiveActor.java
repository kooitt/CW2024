package com.example.demo;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ActiveActor extends ImageView {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";
    private static final int HITBOX_MARGIN = 40; // Margin to reduce the hitbox size

    public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        // Set up the image for the actor
        this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
        this.setLayoutX(initialXPos);
        this.setLayoutY(initialYPos);
        this.setFitHeight(imageHeight);
        this.setPreserveRatio(true);
    }

    // Abstract method to update the position of the actor
    public abstract void updatePosition();

    // Helper method to move the actor horizontally
    protected void moveHorizontally(double horizontalMove) {
        this.setTranslateX(getTranslateX() + horizontalMove);
    }

    // Helper method to move the actor vertically
    protected void moveVertically(double verticalMove) {
        this.setTranslateY(getTranslateY() + verticalMove);
    }

    // Custom method to calculate reduced hitbox bounds
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        return new BoundingBox(
            originalBounds.getMinX() + HITBOX_MARGIN,
            originalBounds.getMinY() + HITBOX_MARGIN,
            originalBounds.getWidth() - 2 * HITBOX_MARGIN,
            originalBounds.getHeight() - 2 * HITBOX_MARGIN
        );
    }
}
