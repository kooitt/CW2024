// BossProjectile.java
package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.utils.GameSettings;

/**
 * Represents a projectile fired by the boss.
 */
public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;

	/**
	 * Constructs a BossProjectile with specified Y position.
	 *
	 * @param initialYPos initial Y position.
	 */
	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2.5, IMAGE_HEIGHT * 0.9);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
