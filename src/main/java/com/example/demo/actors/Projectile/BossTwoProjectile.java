package com.example.demo.actors.Projectile;

/**
 * Represents a projectile specifically designed for the second boss (BossTwo) in the game.
 * This class extends the base {@link Projectile} class and customizes its appearance,
 * hitbox size, and movement behavior.
 */
public class BossTwoProjectile extends Projectile {

    /**
     * The name of the image file used to represent the projectile visually.
     * This image is located in the resources folder.
     */
    private static final String IMAGE_NAME = "basketball.png";

    /**
     * The height of the projectile image, in pixels. The width is scaled proportionally.
     * This value is also used to determine the size of the collision hitbox.
     */
    private static final int IMAGE_HEIGHT = 50;

    /**
     * The horizontal velocity of the projectile, moving leftwards across the screen.
     * Negative value indicates movement from right to left.
     */
    private static final int HORIZONTAL_VELOCITY = -12;

    /**
     * Constructs a new {@link BossTwoProjectile} instance at the specified vertical position.
     * The projectile is initialized with a custom image, size, and a hitbox configuration.
     *
     * @param initialYPos The initial Y-coordinate of the projectile's position.
     */
    public BossTwoProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2, IMAGE_HEIGHT);
    }

    /**
     * Resets the position of the projectile to the specified coordinates and updates its movement.
     * This method overrides the base {@link Projectile#resetPosition} to set a custom velocity
     * for the projectile's horizontal movement.
     *
     * @param x The new X-coordinate to reset the projectile's position.
     * @param y The new Y-coordinate to reset the projectile's position.
     */
    @Override
    public void resetPosition(double x, double y) {
        super.resetPosition(x, y);
        getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
    }
}
