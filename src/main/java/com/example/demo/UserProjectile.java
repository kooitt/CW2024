package com.example.demo;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UserProjectile extends Projectile {

    private static final String IMAGE_NAME = "userfire.png";
    private static final int IMAGE_HEIGHT = 125;
    private static final int HORIZONTAL_VELOCITY = 15;
    private static final int HITBOX_MARGIN = 30; // Smaller hitbox for better precision
    private static final boolean DEBUG_HITBOXES = true; // Enable/disable hitbox visualization

    public UserProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        return new BoundingBox(
            originalBounds.getMinX() + HITBOX_MARGIN,
            originalBounds.getMinY() + HITBOX_MARGIN,
            originalBounds.getWidth() - 2 * HITBOX_MARGIN,
            originalBounds.getHeight() - 2 * HITBOX_MARGIN
        );
    }

    /**
     * Optional: Render the hitbox for debugging purposes.
     *
     * @param root The Group object representing the game scene's root node.
     */
    public void renderHitbox(Group root) {
        if (!DEBUG_HITBOXES) return; // Skip rendering if debugging is disabled

        Bounds bounds = getReducedBounds();
        Rectangle hitbox = new Rectangle(
            bounds.getMinX(),
            bounds.getMinY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
        hitbox.setFill(Color.TRANSPARENT); // Transparent fill
        hitbox.setStroke(Color.RED);       // Red outline for hitbox
        root.getChildren().add(hitbox);    // Add the hitbox to the game scene
    }
}
