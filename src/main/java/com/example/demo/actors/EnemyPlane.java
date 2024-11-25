// EnemyPlane.java

package com.example.demo.actors;

import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.5; // 每秒发射0.5次

	private ShootingComponent shootingComponent;

	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.35);
		updateHitBoxPosition();

		// 初始化 MovementComponent，设置水平速度
		getMovementComponent().setVelocity(-6, 0);

		// 初始化 ShootingComponent
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_POSITION_OFFSET, PROJECTILE_Y_POSITION_OFFSET);

		// 开始射击
		shootingComponent.startFiring();
	}

	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		updateHitBoxPosition();

		if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
			shootingComponent.setProjectilePool(level.getEnemyProjectilePool());
		}

		// 更新射击逻辑
		shootingComponent.update(deltaTime, level);
	}
}
