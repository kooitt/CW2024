package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = 75;
	private static final double Y_LOWER_BOUND = 700;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 45;
	private static final int VERTICAL_VELOCITY = 2;

	private static final int HORIZONTAL_VELOCITY = 2;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int verticalVelocityMultiplier; // Separate multiplier for vertical movement
	private int horizontalVelocityMultiplier; // Separate multiplier for horizontal movement
	private int numberOfKills;

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		verticalVelocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
		startAnimation();
	}
	//smoothens animation
	private void startAnimation() {
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				updatePosition();
			}
		};
		timer.start();
	}

	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			double initialTranslateX = getTranslateX(); // Store initial X translation

			this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier); // Use vertical multiplier
			this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier); // Use horizontal multiplier

			double newPositionY = getLayoutY() + getTranslateY();
			double newPositionX = getLayoutX() + getTranslateX(); // Calculate new X position

			if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}

			// Add bounds for X movement (replace with your actual bounds)
			if (newPositionX < 0 || newPositionX > 950) {
				this.setTranslateX(initialTranslateX);
			}
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		double projectileX = getLayoutX() + getTranslateX() + PROJECTILE_X_POSITION;
		double projectileY = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
		return new UserProjectile(projectileX, projectileY);

	}

	private boolean isMoving() {
		return (horizontalVelocityMultiplier != 0 || verticalVelocityMultiplier!=0);
	}

	public void moveUp() {
		verticalVelocityMultiplier = -1;
	}

	public void moveDown() {
		verticalVelocityMultiplier = 1;
	}

	public void moveLeft() {
		horizontalVelocityMultiplier = -1;
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 1;
	}

	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0; // Stop horizontal movement
	}

	public void stopVertical() {
		verticalVelocityMultiplier = 0; // Stop vertical movement
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

}
