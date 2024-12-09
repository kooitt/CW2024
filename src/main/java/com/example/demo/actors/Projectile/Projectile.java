package com.example.demo.actors.Projectile;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;

/**
 * Represents a projectile in the game. This abstract class provides a foundation
 * for specific projectile types, handling movement, collision, and destruction behavior.
 * It extends the {@link Actor} class to utilize shared actor functionality.
 */
public abstract class Projectile extends Actor {

	/**
	 * Constructs a new Projectile with the specified parameters.
	 *
	 * @param imageName   the path to the image file representing the projectile.
	 * @param imageHeight the height of the projectile's image.
	 * @param initialXPos the initial X-coordinate of the projectile.
	 * @param initialYPos the initial Y-coordinate of the projectile.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos, 1); // Initializes with health of 1.
		getMovementComponent().setVelocity(0, 0); // Default velocity set to zero.

		// Configures the collision component with the projectile's dimensions.
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		double offsetX = 0;
		double offsetY = 0;
		CollisionComponent collision = new CollisionComponent(this, hitboxWidth, hitboxHeight, offsetX, offsetY);
		setCollisionComponent(collision);
	}

	/**
	 * Resets the projectile's position and visibility.
	 *
	 * @param x the X-coordinate to reset the projectile to.
	 * @param y the Y-coordinate to reset the projectile to.
	 */
	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0); // Resets translation to default.
		setTranslateY(0);
		isDestroyed = false; // Marks the projectile as not destroyed.
		setVisible(true); // Makes the projectile visible again.
	}

	/**
	 * Fully resets the projectile, marking it as destroyed and invisible.
	 */
	public void reset() {
		setVisible(false); // Makes the projectile invisible.
		isDestroyed = true; // Marks the projectile as destroyed.
		getMovementComponent().setVelocity(0, 0); // Stops movement by resetting velocity.
	}

	/**
	 * Destroys the projectile. Overrides the {@link Actor#destroy()} method
	 * to perform specific destruction logic for projectiles.
	 */
	@Override
	public void destroy() {
		super.destroy();
	}

	/**
	 * Handles damage logic for the projectile. Any damage taken results in destruction.
	 *
	 * @param damage the amount of damage taken.
	 */
	@Override
	public void takeDamage(int damage) {
		destroy(); // A projectile is destroyed when it takes any damage.
	}

	/**
	 * Updates the projectile's position and collision box each frame.
	 *
	 * @param deltaTime the time elapsed since the last frame, in seconds.
	 * @param level     the current level context where the projectile exists.
	 */
	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition(); // Updates the position based on movement velocity.
		getCollisionComponent().updateHitBoxPosition(); // Updates the collision box position.
	}
}
