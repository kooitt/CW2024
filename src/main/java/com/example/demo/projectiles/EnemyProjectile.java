// EnemyProjectile.java

package com.example.demo.projectiles;

public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "enemyFire.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -10;

	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

		setHitboxSize(80, 25);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);

		// 设置水平速度
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
