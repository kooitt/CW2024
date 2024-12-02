package com.example.demo.projectiles;

/**
 * EnemyProjectile represents projectiles fired by enemy planes.
 * It moves horizontally at a specified velocity.
 */
public class EnemyProjectile extends Projectile {

    // Constants for projectile properties
    private static final String IMAGE_NAME = "enemyFire.png"; // Image representing the projectile
    private static final int IMAGE_HEIGHT = 35;              // Height of the projectile image

    // Horizontal velocity of the projectile
    private int horizontalVelocity;

    /**
     * Constructor for EnemyProjectile with default speed.
     * This constructor is typically used in regular levels.
     *
     * @param initialXPos The initial X position of the projectile.
     * @param initialYPos The initial Y position of the projectile.
     */
    public EnemyProjectile(double initialXPos, double initialYPos) {
        // Call the other constructor with a default horizontal velocity of -11
        this(initialXPos, initialYPos, -11);
    }

    /**
     * Constructor for EnemyProjectile with adjustable speed.
     * This constructor is used in levels where the projectile speed varies.
     *
     * @param initialXPos       The initial X position of the projectile.
     * @param initialYPos       The initial Y position of the projectile.
     * @param horizontalVelocity The speed of the projectile's horizontal movement.
     */
    public EnemyProjectile(double initialXPos, double initialYPos, int horizontalVelocity) {
        // Initialize the projectile with its image, size, and position
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
        this.horizontalVelocity = horizontalVelocity; // Set the projectile's horizontal velocity
    }

    /**
     * Updates the position of the projectile.
     * Moves it horizontally based on its velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(horizontalVelocity); // Move the projectile to the left
    }

    /**
     * Updates the actor's state. In this case, it updates the projectile's position.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Call the updatePosition method to move the projectile
    }
}
