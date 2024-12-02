package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * Projectile is an abstract class representing a general projectile in the game.
 * It provides basic functionality for updating position, collision handling, and defining hitboxes.
 */
public abstract class Projectile extends ActiveActorDestructible {

    /**
     * Constructor for Projectile.
     *
     * @param imageName     The file name of the image representing the projectile.
     * @param imageHeight   The height of the projectile image.
     * @param initialXPos   The initial X position of the projectile.
     * @param initialYPos   The initial Y position of the projectile.
     */
    public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
    }

    /**
     * Abstract method for updating the projectile's position.
     * Subclasses must implement this to define specific movement behavior.
     */
    @Override
    public abstract void updatePosition();

    /**
     * Handles the projectile taking damage (e.g., when it collides with a target).
     * Projectiles are destroyed immediately upon taking damage.
     */
    @Override
    public void takeDamage() {
        this.destroy(); // Mark the projectile as destroyed
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
            originalBounds.getMinX() + 10,    // Add a margin to the left
            originalBounds.getMinY() + 10,    // Add a margin to the top
            originalBounds.getWidth() - 20,  // Reduce the width by 20 units
            originalBounds.getHeight() - 20  // Reduce the height by 20 units
        );
    }
}
