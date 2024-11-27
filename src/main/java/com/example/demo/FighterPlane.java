package com.example.demo;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public abstract class FighterPlane extends ActiveActorDestructible {

    private int health;

    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.health = health;
    }

    // Abstract method for firing projectiles
    public abstract ActiveActorDestructible fireProjectile();

    @Override
    public void takeDamage() {
        health--;
        if (healthAtZero()) {
            this.destroy();
        }
    }

    // Get the X position for projectile firing
    protected double getProjectileXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

    // Get the Y position for projectile firing
    protected double getProjectileYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

    // Check if the plane's health has reached zero
    private boolean healthAtZero() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    // Custom method to calculate reduced hitbox bounds
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        return new BoundingBox(
        		originalBounds.getMinX() + 30, // Margin for X
                originalBounds.getMinY() + 30, // Margin for Y
                originalBounds.getWidth() - 60, // Reduce width
                originalBounds.getHeight() - 60 // Reduce height
        );
    }
}
