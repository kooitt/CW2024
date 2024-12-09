// UserProjectile.java
package com.example.demo.actors.Projectile;

/**
 * Represents a projectile fired by the user.
 */
public class UserProjectile extends Projectile {

	private static String currentImageName = "userfire.png";
	private static final int IMAGE_HEIGHT = 25;
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * Constructs a UserProjectile with specified position.
	 *
	 * @param initialXPos initial X position.
	 * @param initialYPos initial Y position.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(currentImageName, IMAGE_HEIGHT, initialXPos, initialYPos);
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT*2, IMAGE_HEIGHT);
	}

	public static void setCurrentImageName(String imageName) {
		currentImageName = imageName;
	}

	public static String getCurrentImageName() {
		return currentImageName;
	}

	@Override
	public void reset() {
		super.reset();
		setImageViewImage(currentImageName);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
