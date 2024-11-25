// UserPlane.java

package com.example.demo.actors;

import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 800.0; // 根据屏幕宽度调整
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8;
	private static final double PROJECTILE_X_OFFSET = 110;
	private static final double PROJECTILE_Y_OFFSET = 20;
	private static final double FIRE_RATE = 10.0; // 每秒发射5次
	private static final int INITIAL_HEALTH = 5;

	private int verticalVelocityMultiplier;
	private int horizontalVelocityMultiplier;
	private int numberOfKills;

	private ShootingComponent shootingComponent;

	public UserPlane() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, INITIAL_HEALTH);
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;

		setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.3);
		updateHitBoxPosition();

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);

		// 初始化 ShootingComponent
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);

		// 开始自动射击
		shootingComponent.startFiring();
	}

	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		updateHitBoxPosition();

		if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
			shootingComponent.setProjectilePool(level.getUserProjectilePool());
		}

		// 更新射击逻辑
		shootingComponent.update(deltaTime, level);
	}

	public void moveUp() {
		verticalVelocityMultiplier = -1;
		updateVelocity();
	}

	public void moveDown() {
		verticalVelocityMultiplier = 1;
		updateVelocity();
	}

	public void stopVerticalMovement() {
		verticalVelocityMultiplier = 0;
		updateVelocity();
	}

	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
		updateVelocity();
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
		updateVelocity();
	}

	public void stopHorizontalMovement() {
		horizontalVelocityMultiplier = 0;
		updateVelocity();
	}

	private void updateVelocity() {
		int dx = HORIZONTAL_VELOCITY * horizontalVelocityMultiplier;
		int dy = VERTICAL_VELOCITY * verticalVelocityMultiplier;
		getMovementComponent().setVelocity(dx, dy);
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

	@Override
	public void updatePosition() {
		double initialTranslateX = getTranslateX();
		double initialTranslateY = getTranslateY();
		super.updatePosition();
		double newXPosition = getLayoutX() + getTranslateX();
		double newYPosition = getLayoutY() + getTranslateY();

		// 限制水平移动范围
		if (newXPosition < X_LEFT_BOUND || newXPosition > X_RIGHT_BOUND) {
			setTranslateX(initialTranslateX);
		}

		// 限制垂直移动范围
		if (newYPosition < Y_UPPER_BOUND || newYPosition > Y_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}
}
