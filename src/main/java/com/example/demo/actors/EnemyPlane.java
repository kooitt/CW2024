// EnemyPlane.java

package com.example.demo.actors;

import com.example.demo.projectiles.EnemyProjectile;
import com.example.demo.projectiles.Projectile;
import com.example.demo.levels.LevelParent;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.01;

	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.35);
		updateHitBoxPosition();

		// 初始化 MovementComponent，设置水平速度
		getMovementComponent().setVelocity(-6, 0);
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateHitBoxPosition();
	}

	@Override
	public Projectile fireProjectile(LevelParent level) {
		if (Math.random() < FIRE_RATE) {
			Projectile projectile = level.getEnemyProjectilePool().acquire();
			if (projectile != null) {
				double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
				double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
				projectile.resetPosition(projectileXPosition, projectileYPosition);
				return projectile;
			}
		}
		return null;
	}
}
