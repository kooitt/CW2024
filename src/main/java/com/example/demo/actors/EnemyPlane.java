// EnemyPlane.java

package com.example.demo.actors;

import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.projectiles.Projectile;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.5; //

	private ShootingComponent shootingComponent;

	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.35);
		updateHitBoxPosition();

		// 初始化 MovementComponent，设置水平速度
		getMovementComponent().setVelocity(-6, 0);

		// Initialize ShootingComponent, passing 'this' as owner
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_POSITION_OFFSET, PROJECTILE_Y_POSITION_OFFSET);

		// Start firing
		shootingComponent.startFiring();
	}

	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		updateHitBoxPosition();

		if (shootingComponent != null && shootingComponent.projectilePool == null) {
			shootingComponent.projectilePool = level.getEnemyProjectilePool();
		}

		// Update shooting logic
		shootingComponent.update(deltaTime, level);
	}
}
