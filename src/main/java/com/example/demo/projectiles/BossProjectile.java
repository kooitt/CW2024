package com.example.demo.projectiles;

public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;

	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);

		// 使用 CollisionComponent 来设置碰撞盒大小
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2.5, IMAGE_HEIGHT * 0.9);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);

		// 设置水平速度
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
