package com.example.demo.actors.Actor;

import com.example.demo.levels.LevelParent;

/**
 * Represents a Shield actor in the game, which serves as a protective entity.
 * The Shield has a defined image, size, and health, and interacts with other
 * game components through its collision component.
 */
public class Shield extends Actor {

    /** The name of the image file representing the shield. */
    private static final String IMAGE_NAME = "shield.png";

    /** The height of the shield's image in pixels. */
    private static final int IMAGE_HEIGHT = 200;

    /** The maximum health of the shield. */
    private static final int MAX_HEALTH = 10;

    /**
     * Constructs a Shield instance with specified initial positions.
     *
     * @param initialXPos the initial x-coordinate position of the shield
     * @param initialYPos the initial y-coordinate position of the shield
     */
    public Shield(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, MAX_HEALTH);

        // Set the collision hitbox size to 30% of the image height for width
        // and full height for height.
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.3, IMAGE_HEIGHT);

        // Update the hitbox position to align with the shield's position.
        getCollisionComponent().updateHitBoxPosition();
    }

    /**
     * Updates the Shield's actor state, specifically ensuring that the
     * collision component's hitbox position is synchronized with the shield's position.
     *
     * @param deltaTime the time elapsed since the last update, in seconds
     * @param level the current level in which the shield exists
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        getCollisionComponent().updateHitBoxPosition();
    }
}
