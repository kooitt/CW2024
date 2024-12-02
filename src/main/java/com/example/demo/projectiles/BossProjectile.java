package com.example.demo.projectiles;

/**
 * BossProjectile represents the projectiles fired by the boss.
 * It moves horizontally with a zigzag motion vertically.
 */
public class BossProjectile extends Projectile {

    // Constants for projectile properties
    private static final String IMAGE_NAME = "fireball.png";   // Image representing the projectile
    private static final int IMAGE_HEIGHT = 50;               // Height of the projectile image
    private static final int HORIZONTAL_VELOCITY = -30;       // Speed of horizontal movement
    private static final int INITIAL_X_POSITION = 950;        // Starting X position for the projectile
    private static final int ZIGZAG_OFFSET = 5;               // Offset for the zigzag motion

    // Direction of vertical movement in the zigzag pattern
    private int zigzagDirection = 1; // 1 for down, -1 for up

    /**
     * Constructor for BossProjectile.
     *
     * @param initialYPos The initial Y position of the projectile.
     */
    public BossProjectile(double initialYPos) {
        // Initialize the projectile with its image, size, and initial position
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    /**
     * Updates the position of the projectile.
     * Moves it horizontally and vertically in a zigzag pattern.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);           // Move the projectile to the left
        moveVertically(ZIGZAG_OFFSET * zigzagDirection); // Move up or down based on the zigzag direction
        zigzagDirection *= -1;                           // Flip the direction for the zigzag motion
    }

    /**
     * Updates the actor's state. In this case, it updates the projectile's position.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Call the updatePosition method to move the projectile
    }
}
