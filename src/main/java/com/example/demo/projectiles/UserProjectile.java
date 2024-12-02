package com.example.demo.projectiles;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * UserProjectile represents the projectiles fired by the player's plane.
 * It moves horizontally to the right with a specified velocity and has a reduced hitbox for better precision.
 */
public class UserProjectile extends Projectile {

    // Constants for projectile properties
    private static final String IMAGE_NAME = "userfire.png";  // Image representing the projectile
    private static final int IMAGE_HEIGHT = 125;             // Height of the projectile image
    private static final int HORIZONTAL_VELOCITY = 15;       // Speed of horizontal movement
    private static final int HITBOX_MARGIN = 30;             // Margin to reduce the hitbox size
    private static final boolean DEBUG_HITBOXES = true;      // Enable/disable hitbox visualization

    /**
     * Constructor for UserProjectile.
     *
     * @param initialXPos The initial X position of the projectile.
     * @param initialYPos The initial Y position of the projectile.
     */
    public UserProjectile(double initialXPos, double initialYPos) {
        // Initialize the projectile with its image, size, and position
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    /**
     * Updates the position of the projectile.
     * Moves it horizontally to the right based on its velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY); // Move the projectile to the right
    }

    /**
     * Updates the actor's state. In this case, it updates the projectile's position.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Call the updatePosition method to move the projectile
    }

    /**
     * Calculates the reduced hitbox bounds for the projectile.
     * This reduces the size of the hitbox for more precise collision detection.
     *
     * @return A BoundingBox representing the reduced hitbox of the projectile.
     */
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal(); // Get the original bounds
        return new BoundingBox(
            originalBounds.getMinX() + HITBOX_MARGIN,    // Add a margin to the left
            originalBounds.getMinY() + HITBOX_MARGIN,    // Add a margin to the top
            originalBounds.getWidth() - 2 * HITBOX_MARGIN,  // Reduce the width by twice the margin
            originalBounds.getHeight() - 2 * HITBOX_MARGIN  // Reduce the height by twice the margin
        );
    }

    /**
     * Optional: Render the hitbox for debugging purposes.
     * Adds a rectangle to the game scene to visualize the reduced hitbox.
     *
     * @param root The Group object representing the game scene's root node.
     */
    public void renderHitbox(Group root) {
        if (!DEBUG_HITBOXES) return; // Skip rendering if debugging is disabled

        // Get the reduced hitbox bounds
        Bounds bounds = getReducedBounds();
        
        // Create a rectangle to represent the hitbox
        Rectangle hitbox = new Rectangle(
            bounds.getMinX(),
            bounds.getMinY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
        hitbox.setFill(Color.TRANSPARENT); // Transparent fill for the hitbox
        hitbox.setStroke(Color.RED);       // Red outline for visibility
        root.getChildren().add(hitbox);    // Add the hitbox rectangle to the game scene
    }
}
