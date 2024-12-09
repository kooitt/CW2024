package com.example.demo.actors.Projectile;

/**
 * The EnemyProjectile class represents a projectile fired by an enemy in the game.
 * It inherits from the Projectile base class and provides specific behavior and properties
 * for projectiles associated with enemy units, such as image appearance, dimensions,
 * and movement direction.
 */
public class EnemyProjectile extends Projectile {

	/**
	 * The name of the image file used to represent the enemy projectile.
	 */
	private static final String IMAGE_NAME = "enemyFire.png";

	/**
	 * The height of the projectile's image, used for both rendering and hitbox dimensions.
	 */
	private static final int IMAGE_HEIGHT = 50;

	/**
	 * The horizontal velocity of the projectile, representing its speed in the x-direction.
	 * A negative value indicates that the projectile moves from right to left on the screen.
	 */
	private static final int HORIZONTAL_VELOCITY = -10;

	/**
	 * Constructs an EnemyProjectile object at the specified initial position.
	 *
	 * @param initialXPos the initial x-coordinate of the projectile.
	 * @param initialYPos the initial y-coordinate of the projectile.
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		// Set the hitbox size for collision detection to match the projectile's dimensions.
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
	}

	/**
	 * Resets the position of the enemy projectile and initializes its velocity.
	 * This method is useful for reusing projectiles from an object pool.
	 *
	 * @param x the new x-coordinate for the projectile.
	 * @param y the new y-coordinate for the projectile.
	 */
	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		// Set the velocity of the projectile to move horizontally from right to left.
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
