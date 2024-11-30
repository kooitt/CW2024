// EnemyProjectile.java
package com.example.demo.projectiles;

/**
 * Represents a projectile fired by enemies.
 */
public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "enemyFire.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -10;

	/**
	 * Constructs an EnemyProjectile with specified position.
	 *
	 * @param initialXPos initial X position.
	 * @param initialYPos initial Y position.
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		getCollisionComponent().setHitboxSize(80.0, 25.0);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
