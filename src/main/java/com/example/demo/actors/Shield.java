// Shield.java
package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

/**
 * Represents a shield in the game.
 * Acts as a protective barrier.
 */
public class Shield extends ActiveActor {

    private static final String IMAGE_NAME = "shield.png";
    private static final int IMAGE_HEIGHT = 200;
    private static final int MAX_HEALTH = 10;

    /**
     * Constructs a Shield with specified position.
     *
     * @param initialXPos initial X position.
     * @param initialYPos initial Y position.
     */
    public Shield(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, MAX_HEALTH);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.3, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        getCollisionComponent().updateHitBoxPosition();
    }
}
