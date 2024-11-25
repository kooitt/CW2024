package com.example.demo.projectiles;

public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png";
	private static final int IMAGE_HEIGHT = 125;
	private static final int HORIZONTAL_VELOCITY = 15;

	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);

		// 使用 CollisionComponent 来设置碰撞盒大小
		getCollisionComponent().setHitboxSize(30.0, 30.0);
	}

	@Override
	public void resetPosition(double x, double y) {
		super.resetPosition(x, y);

		// 设置水平速度
		getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
	}
}
