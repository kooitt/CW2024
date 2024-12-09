package com.example.demo.actors.Actor;

import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;

/**
 * The {@code ActorLevelUp} class represents a power-up actor that grants the player increased firepower
 * upon collection. It moves in a sinusoidal pattern across the screen and is destroyed when collected
 * or when it moves off the screen.
 */
public class ActorLevelUp extends Actor {

    /** Name of the image file representing the actor. */
    private static final String IMAGE_NAME = "ActorLevelUp.png";

    /** Height of the actor's image in pixels. */
    private static final int IMAGE_HEIGHT = 50;

    /** Horizontal speed of the actor (negative for leftward motion). */
    private static final double SPEED_X = -10;

    /** Amplitude of the sinusoidal vertical movement. */
    private static final double AMPLITUDE = 30;

    /** Frequency of the sinusoidal vertical movement in cycles per second. */
    private static final double FREQUENCY = 0.5;

    /** Initial Y-coordinate of the actor. */
    private final double initialY;

    /** Time tracker for sinusoidal movement calculations. */
    private double time;

    /**
     * Constructs an {@code ActorLevelUp} at the specified position.
     *
     * @param initialXPos the initial X-coordinate of the actor
     * @param initialYPos the initial Y-coordinate of the actor
     */
    public ActorLevelUp(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, 1);
        this.initialY = initialYPos;
        this.time = 0;

        // Configure the hitbox to match the image dimensions
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
//        getCollisionComponent().updateHitBoxPosition();
    }

    /**
     * Updates the actor's position and behavior.
     *
     * @param deltaTime the time elapsed since the last update, in seconds
     * @param level     the current level in which the actor exists
     */
    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        // Increment the timer by the elapsed time
        time += deltaTime;

        // Calculate new X and Y positions based on speed and sinusoidal function
        double newX = getLayoutX() + SPEED_X;
        double newY = initialY + AMPLITUDE * Math.sin(FREQUENCY * time * 2 * Math.PI);

        // Update the actor's position
        setLayoutX(newX);
        setLayoutY(newY);

        // Update the collision hitbox position
//        getCollisionComponent().updateHitBoxPosition();

        // If the actor moves off the left side of the screen, destroy it
        if (newX + getCollisionComponent().getHitboxWidth() < 0) {
            destroy();
        }
    }

    /**
     * Handles the event when the actor is collected by the player.
     *
     * @param level the current level in which the actor exists
     */
    public void onPickup(LevelParent level) {
        // Retrieve the player from the current level
        UserPlane user = level.getUser();

        // Increment the player's power-up count
        user.incrementPowerUpCount();

        // Double the player's fire rate
        user.doubleFireRate();

        // Destroy this power-up actor
        destroy();

        // Play the sound effect for picking up a bullet power-up
        SoundComponent.playGetbulletSound();
    }
}
