package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public abstract class Projectile extends ActiveActorDestructible {


    public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
    }

    // Abstract method for updating projectile position
    @Override
    public abstract void updatePosition();

    // Destroy the projectile when it takes damage (e.g., collides with a target)
    @Override
    public void takeDamage() {
        this.destroy();
    }

    // Custom method to calculate reduced hitbox bounds for projectiles
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        return new BoundingBox(
        		originalBounds.getMinX() + 10, // Smaller X margin
                originalBounds.getMinY() + 10, // Smaller Y margin
                originalBounds.getWidth() - 20,
                originalBounds.getHeight() - 20
        );
    }
}
