// UserProjectile.java
package com.example.demo.projectiles;

/**
 * Represents a projectile fired by the user.
 */
public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 125;
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * Constructs a UserProjectile with specified position.
	 *
	 * @param initialXPos initial X position.
	 * @param initialYPos initial Y position.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		getCollisionComponent().setHitboxSize(30.0, 30.0);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
