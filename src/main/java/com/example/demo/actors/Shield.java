// Shield.java
package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

/**
 * Represents a shield in the game.
 * Acts as a protective barrier.
 */
public class Shield extends ActiveActor {

    public static final int SHIELD_SIZE = 100;
    private static final String IMAGE_NAME = "shield.png";
    private static final int IMAGE_HEIGHT = 100;
    private static final int MAX_HEALTH = 10;

    /**
     * Constructs a Shield with specified position.
     *
     * @param initialXPos initial X position.
     * @param initialYPos initial Y position.
     */
    public Shield(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, MAX_HEALTH);
        getCollisionComponent().setHitboxSize(SHIELD_SIZE, SHIELD_SIZE);
        getCollisionComponent().updateHitBoxPosition();
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        getCollisionComponent().updateHitBoxPosition();
    }
}
