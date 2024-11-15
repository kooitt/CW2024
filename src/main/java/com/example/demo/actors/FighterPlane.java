package com.example.demo.actors;

/**
 * Represents a fighter plane in the game, which is a type of ActiveActorDestructible.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;

	/**
	 * Constructs a FighterPlane with the specified parameters.
	 *
	 * @param imageName the name of the image representing the fighter plane.
	 * @param imageHeight the height of the image representing the fighter plane.
	 * @param initialXPos the initial X position of the fighter plane.
	 * @param initialYPos the initial Y position of the fighter plane.
	 * @param health the initial health of the fighter plane.
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Fires a projectile from the fighter plane.
	 *
	 * @return a new ActiveActorDestructible representing the fired projectile.
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Inflicts damage to the fighter plane by decreasing its health.
	 * Destroys the plane if health reaches zero.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Gets the X position for the projectile to be fired from the fighter plane.
	 *
	 * @param xPositionOffset the offset to be added to the current X position.
	 * @return the calculated X position for the projectile.
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Gets the Y position for the projectile to be fired from the fighter plane.
	 *
	 * @param yPositionOffset the offset to be added to the current Y position.
	 * @return the calculated Y position for the projectile.
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the health of the fighter plane is zero.
	 *
	 * @return true if health is zero, false otherwise.
	 */
	private boolean healthAtZero() {
		return health == 0;
	}

	/**
	 * Gets the current health of the fighter plane.
	 *
	 * @return the current health of the fighter plane.
	 */
	public int getHealth() {
		return health;
	}
}