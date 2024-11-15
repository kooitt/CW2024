package com.example.demo.actors;

/**
 * Represents a projectile in the game, which is a type of ActiveActorDestructible.
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a Projectile with the specified parameters.
	 *
	 * @param imageName the name of the image representing the projectile.
	 * @param imageHeight the height of the image representing the projectile.
	 * @param initialXPos the initial X position of the projectile.
	 * @param initialYPos the initial Y position of the projectile.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Inflicts damage to the projectile, destroying it.
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Updates the position of the projectile.
	 */
	@Override
	public abstract void updatePosition();
}