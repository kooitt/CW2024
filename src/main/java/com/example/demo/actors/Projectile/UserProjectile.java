package com.example.demo.actors.Projectile;

/**
 * Represents a projectile fired by the user in the game.
 * <p>
 * The UserProjectile class extends the generic Projectile class and
 * defines specific attributes and behaviors for user-fired projectiles,
 * including default image, size, velocity, and collision hitbox dimensions.
 * </p>
 */
public class UserProjectile extends Projectile {

	/**
	 * The name of the image representing the projectile.
	 * This image can be updated dynamically using {@link #setCurrentImageName(String)}.
	 */
	private static String currentImageName = "userfire.png";

	/**
	 * The height of the projectile image.
	 * This value is used to determine the dimensions of the projectile's hitbox.
	 */
	private static final int IMAGE_HEIGHT = 25;

	/**
	 * The horizontal velocity of the projectile.
	 * This value defines the speed at which the projectile moves horizontally across the screen.
	 */
	private static final int HORIZONTAL_VELOCITY = 15;

	/**
	 * Constructs a UserProjectile with a specified initial position.
	 * <p>
	 * Initializes the projectile with its image, size, and hitbox dimensions.
	 * </p>
	 *
	 * @param initialXPos the initial X position of the projectile.
	 * @param initialYPos the initial Y position of the projectile.
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(currentImageName, IMAGE_HEIGHT, initialXPos, initialYPos);
		// Sets the hitbox dimensions to twice the image height in width and equal to the image height in height.
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2, IMAGE_HEIGHT);
	}

	/**
	 * Sets the current image name for all UserProjectile instances.
	 * <p>
	 * This method allows the image representing the projectile to be dynamically updated
	 * for all future projectiles created after this method is called.
	 * </p>
	 *
	 * @param imageName the new image name to be used for the projectile.
	 */
	public static void setCurrentImageName(String imageName) {
		currentImageName = imageName;
	}

	/**
	 * Retrieves the current image name used by the projectile.
	 *
	 * @return the name of the image representing the projectile.
	 */
	public static String getCurrentImageName() {
		return currentImageName;
	}

	/**
	 * Resets the projectile's state to its default values.
	 * <p>
	 * This method resets the projectile's attributes, including its image,
	 * and prepares it for reuse within the game environment.
	 * </p>
	 */
	@Override
	public void reset() {
		super.reset();
		// Sets the projectile's image to the current image name.
		setImageViewImage(currentImageName);
	}

	/**
	 * Resets the position of the projectile and updates its velocity.
	 * <p>
	 * This method is typically used when reusing a projectile from an object pool
	 * to avoid unnecessary instantiation.
	 * </p>
	 *
	 * @param x the new X position of the projectile.
	 * @param y the new Y position of the projectile.
	 */
	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		// Sets the horizontal velocity for the projectile after resetting its position.
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
