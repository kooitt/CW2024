package com.example.demo.actors;

import com.example.demo.projectile.UserProjectile;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = 0;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_UPPER_BOUND = 5.0;
	private static final double X_LOWER_BOUND = 1290.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 40;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8; //Added a horizontal constant
	private static final int PROJECTILE_X_POSITION_OFFSET = 80;
	private static final int PROJECTILE_Y_POSITION_OFFSET = -30;
	private double HorizontalvelocityMultiplier;
	private double VerticalvelocityMultiplier;
	private int numberOfKills;

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		HorizontalvelocityMultiplier = 0;
		VerticalvelocityMultiplier = 0;
	}
	public void stopX() {
		HorizontalvelocityMultiplier = 0;
	}
	public void stopY() {
		VerticalvelocityMultiplier = 0;
	}

	@Override
	public void updatePosition() {
		if (isMovingX()) {
			double initialTranslateX = getTranslateX();
			this.moveHorizontally(HORIZONTAL_VELOCITY * HorizontalvelocityMultiplier);
			double newPositionX = getLayoutX() + getTranslateX();
			if (newPositionX < X_UPPER_BOUND || newPositionX > X_LOWER_BOUND) {
				this.setTranslateX(initialTranslateX);
			}
		} //Checks if the user is moving the plane HORIZONTALLY

		if (isMovingY()) {
			double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * VerticalvelocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}
		} //Checks if the user is moving the plane VERTICALLY
	}
	
	@Override
	public void updateActor() {
		updatePosition();
	}
	
	@Override
	public ActiveActorDestructible fireProjectile() {
		double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
		double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
		return new UserProjectile(projectileXPosition, projectileYPosition); //Gives the program BOTH X/Y coordinates of user plane
	}

	private boolean isMovingX() {
		return HorizontalvelocityMultiplier != 0;
	}
	private boolean isMovingY() {
		return VerticalvelocityMultiplier != 0;
	}
	public void moveUp() {
		VerticalvelocityMultiplier = -1.5;
	}

	public void moveDown() {
		VerticalvelocityMultiplier = 1.5;
	}

	public void moveLeft() {
		HorizontalvelocityMultiplier = -1.5;
	}

	public void moveRight() {
		HorizontalvelocityMultiplier = 1.5;
	}

	public void stop() {
		VerticalvelocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

	public void decrementKillCount() {
		numberOfKills--;
	}
}
