package com.example.demo.actors;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * Abstract class representing a fighter plane in the game.
 * Extends ActiveActorDestructible to include health and firing capabilities.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

    // Health of the fighter plane
    private int health;

    /**
     * Constructor for FighterPlane.
     * Initializes the fighter plane's image, position, and health.
     *
     * @param imageName    Name of the image file representing the fighter plane
     * @param imageHeight  Height of the fighter plane's image
     * @param initialXPos  Initial X position of the fighter plane
     * @param initialYPos  Initial Y position of the fighter plane
     * @param health       Initial health of the fighter plane
     */
    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.health = health; // Set the initial health
    }

    /**
     * Abstract method for firing projectiles.
     * Subclasses must implement this method to define specific firing behavior.
     *
     * @return An ActiveActorDestructible projectile if fired, otherwise null.
     */
    public abstract ActiveActorDestructible fireProjectile();

    /**
     * Reduces the health of the fighter plane by one when it takes damage.
     * Destroys the plane if health reaches zero.
     */
    @Override
    public void takeDamage() {
        health--; // Decrease health by 1
        if (healthAtZero()) { // Check if health is zero
            this.destroy(); // Destroy the plane
        }
    }

    /**
     * Calculates the X position for firing a projectile.
     *
     * @param xPositionOffset Offset from the fighter plane's current X position
     * @return The calculated X position for the projectile
     */
    protected double getProjectileXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

    /**
     * Calculates the Y position for firing a projectile.
     *
     * @param yPositionOffset Offset from the fighter plane's current Y position
     * @return The calculated Y position for the projectile
     */
    protected double getProjectileYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

    /**
     * Checks if the fighter plane's health has reached zero.
     *
     * @return True if health is zero or less, otherwise false.
     */
    private boolean healthAtZero() {
        return health <= 0;
    }

    /**
     * Retrieves the current health of the fighter plane.
     *
     * @return The current health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Retrieves reduced bounds for collision detection.
     * The reduced bounds create a smaller hitbox for more precise collisions.
     *
     * @return A BoundingBox representing the reduced hitbox
     */
    @Override
    public Bounds getReducedBounds() {
        // Get the original bounds of the plane
        Bounds originalBounds = this.getBoundsInLocal();
        
        // Return a reduced bounding box with a margin
        return new BoundingBox(
            originalBounds.getMinX() + 30, // Margin for X
            originalBounds.getMinY() + 30, // Margin for Y
            originalBounds.getWidth() - 60, // Reduce width
            originalBounds.getHeight() - 60 // Reduce height
        );
    }
}
