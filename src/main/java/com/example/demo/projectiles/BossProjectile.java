package com.example.demo.projectiles;

public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;
	private static final int INITIAL_X_POSITION = 950;

	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);

		setHitboxSize(IMAGE_HEIGHT*2.5, IMAGE_HEIGHT*0.9); // 设置 Boss 子弹的 hitbox 尺寸
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitBoxPosition();
	}

	@Override
	public void updateActor() {
		updatePosition();
	}
}
