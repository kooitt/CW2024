package com.example.demo.projectile;

public class EliteEnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "Projectiles/enemyFirealt.gif";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -15;

	public EliteEnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	@Override
	public void updateActor() {
		updatePosition();
	}


}
