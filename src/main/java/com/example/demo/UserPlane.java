package com.example.demo;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 800.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

	private int verticalVelocityMultiplier;
	private int horizontalVelocityMultiplier;
	private int numberOfKills;

	// 构造方法
	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
	}

	// 更新位置方法
	@Override
	public void updatePosition() {
		// 保存初始位置
		double initialTranslateX = getTranslateX();
		double initialTranslateY = getTranslateY();

		// 垂直移动
		if (verticalVelocityMultiplier != 0) {
			this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
		}

		// 水平移动
		if (horizontalVelocityMultiplier != 0) {
			this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);
		}

		// 检查边界
		double newXPosition = getLayoutX() + getTranslateX();
		double newYPosition = getLayoutY() + getTranslateY();

		if (newXPosition < X_LEFT_BOUND || newXPosition > X_RIGHT_BOUND) {
			this.setTranslateX(initialTranslateX);
		}
		if (newYPosition < Y_UPPER_BOUND || newYPosition > Y_LOWER_BOUND) {
			this.setTranslateY(initialTranslateY);
		}
	}

	// 更新行为方法
	@Override
	public void updateActor() {
		updatePosition();
	}

	// 发射子弹方法
	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	// 垂直移动方法
	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	// 水平移动方法
	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	// 停止移动方法
	public void stop() {
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
	}

	// 获取击杀数
	public int getNumberOfKills() {
		return numberOfKills;
	}

	// 增加击杀数
	public void incrementKillCount() {
		numberOfKills++;
	}
}
