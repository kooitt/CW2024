package com.example.demo.actors;

import com.example.demo.projectiles.UserProjectile;
import com.example.demo.projectiles.Projectile;
import com.example.demo.levels.LevelParent;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;

		setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.3);
		updateHitBoxPosition();

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateHitBoxPosition();
	}

	@Override
	public Projectile fireProjectile(LevelParent level) {
		Projectile projectile = level.getUserProjectilePool().acquire();
		if (projectile != null) {
			projectile.resetPosition(getProjectileXPosition(PROJECTILE_X_POSITION), getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
			return projectile;
		}
		return null;
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	public void moveUp() {
		velocityMultiplier = -1;
		getMovementComponent().setVelocity(0, VERTICAL_VELOCITY * velocityMultiplier);
	}

	public void moveDown() {
		velocityMultiplier = 1;
		getMovementComponent().setVelocity(0, VERTICAL_VELOCITY * velocityMultiplier);
	}

	public void stop() {
		velocityMultiplier = 0;
		getMovementComponent().setVelocity(0, 0);
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		super.updatePosition();
		double newPosition = getLayoutY() + getTranslateY();
		if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}
}
