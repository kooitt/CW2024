// HeartItem.java
package com.example.demo.actors.Actor;

import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;

/**
 * The HeartItem class represents a heart-shaped item in the game that restores health to the player when picked up.
 * It moves horizontally with a sinusoidal vertical motion and disappears when it goes off-screen.
 */
public class HeartItem extends Actor {

    /**
     * The file name of the image representing the heart item.
     */
    private static final String IMAGE_NAME = "heartitem.png"; // Ensure "heartitem.png" exists in the resources folder.

    /**
     * The height of the heart item image.
     */
    private static final int IMAGE_HEIGHT = 50;

    /**
     * The horizontal speed of the heart item, moving from right to left.
     */
    private static final double SPEED_X = -10;

    /**
     * The amplitude of the sinusoidal vertical movement of the heart item.
     */
    private static final double AMPLITUDE = 30;

    /**
     * The frequency of the sinusoidal vertical movement of the heart item.
     */
    private static final double FREQUENCY = 0.5;

    /**
     * The maximum amount of health the heart item restores when picked up.
     */
    private static final int MAX_HEALTH_RESTORE = 1;

    /**
     * The initial Y-coordinate of the heart item, used for calculating its vertical movement.
     */
    private double initialY;

    /**
     * The elapsed time since the heart item was created, used for calculating its vertical movement.
     */
    private double time;

    /**
     * Constructs a HeartItem object at the specified initial position.
     * Sets the collision hitbox to match the size of the heart item image.
     *
     * @param initialXPos the initial X-coordinate of the heart item.
     * @param initialYPos the initial Y-coordinate of the heart item.
     */
    public HeartItem(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, 1);
        this.initialY = initialYPos;
        this.time = 0;
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
    }

    /**
     * Updates the heart item's position based on its horizontal speed and sinusoidal vertical movement.
     * Removes the heart item if it moves off-screen to the left.
     *
     * @param deltaTime the time elapsed since the last update, used for calculating movement.
     * @param level     the current level in which the heart item exists.
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        time += deltaTime;
        double newX = getLayoutX() + SPEED_X;
        double newY = initialY + AMPLITUDE * Math.sin(FREQUENCY * time * 2 * Math.PI);
        setLayoutX(newX);
        setLayoutY(newY);
        getCollisionComponent().updateHitBoxPosition();

        // Destroy the heart item if it goes off-screen.
        if (newX + getCollisionComponent().getHitboxWidth() < 0) {
            destroy();
        }
    }

    /**
     * Handles the logic when the heart item is picked up by the player.
     * Restores health to the player, updates the heart display, and plays a sound effect.
     * The heart item is destroyed after being picked up.
     *
     * @param level the current level in which the heart item exists.
     */
    public void onPickup(LevelParent level) {
        UserPlane user = level.getUser();
        user.getHealthComponent().heal(MAX_HEALTH_RESTORE);
        level.addHearts(MAX_HEALTH_RESTORE);
        destroy();
        SoundComponent.playGethealthSound();
    }
}
