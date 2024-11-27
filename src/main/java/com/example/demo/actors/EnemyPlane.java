// EnemyPlane.java

package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

public class EnemyPlane extends ActiveActor {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = 0.5; // 每秒发射0.5次

	private ShootingComponent shootingComponent;
	private AnimationComponent animationComponent;

	public EnemyPlane(double initialXPos, double initialYPos, Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

		// 设置碰撞盒大小
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.35);
		getCollisionComponent().updateHitBoxPosition();

		// 初始化 MovementComponent，设置水平速度
		getMovementComponent().setVelocity(-6, 0);

		// 初始化 ShootingComponent
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_POSITION_OFFSET, PROJECTILE_Y_POSITION_OFFSET);

		// 初始化 AnimationComponent
		animationComponent = new AnimationComponent(root);

		// 开始射击
		shootingComponent.startFiring();
	}

	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		getCollisionComponent().updateHitBoxPosition();

		if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
			shootingComponent.setProjectilePool(level.getEnemyProjectilePool());
		}

		// 更新射击逻辑
		shootingComponent.update(deltaTime, level);
	}

	@Override
	public void destroy() {
		if (!isDestroyed) {
			super.destroy(); // 设置isDestroyed和隐藏EnemyPlane

			// 获取EnemyPlane的当前尺寸
			double planeWidth = getCollisionComponent().getHitboxWidth();
			double planeHeight = getCollisionComponent().getHitboxHeight();

			// 计算中心位置
			double x = getCollisionComponent().getHitboxX();
			double y = getCollisionComponent().getHitboxY();

			// 播放爆炸动画
			animationComponent.playExplosion(x + planeWidth, y + planeHeight, 1.5); // 中心爆炸

			// 根据需要，可以添加更多爆炸点
		}
	}
}
