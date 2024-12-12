package com.example.demo.actors.player;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.FighterPlane;
import com.example.demo.controller.SoundManager;
import com.example.demo.projectile.UserProjectile;


public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "Player/userplaneALT.gif";
	private static final double Y_UPPER_BOUND = 0;
	private static final double Y_LOWER_BOUND = 700.0;
	private static final double X_UPPER_BOUND = 5.0;
	private static final double X_LOWER_BOUND = 1200.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 70;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HORIZONTAL_VELOCITY = 8; //Added a horizontal constant
	private static final int PROJECTILE_X_POSITION_OFFSET = 100;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 30;
	private double HorizontalvelocityMultiplier;
	private double VerticalvelocityMultiplier;
	private int numberOfKills;
	//iframe
	private boolean isIFramed;
	private double iframeTimer;
	private static final double INVINCIBILITY_DURATION = 1;

	//sounds
	private SoundManager soundManager;
	private static final String PLAYER_HIT_SFX = "/com/example/demo/sfx/level_sfx/damageTaken.mp3";

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		HorizontalvelocityMultiplier = 0;
		VerticalvelocityMultiplier = 0;
		isIFramed = false; // Initially not invulnerable
		iframeTimer = 0.0; // No invincibility when the game starts
		//sounds
		soundManager = SoundManager.getInstance(); // Initialize SoundManager instance
		soundManager.loadSFX("damage_taken", PLAYER_HIT_SFX);
	}
	public void stopX() {
		HorizontalvelocityMultiplier = 0;
	}
	public void stopY() {
		VerticalvelocityMultiplier = 0;
	}

	// Invulnerability logic
	@Override
	public void takeDamage() {
		// Only take damage if the player is not invulnerable
		if (!isIFramed) {
			super.takeDamage();
			soundManager.playSFX("damage_taken");
			activateIFrames();
		}
	}

	// Activate invulnerability frames (iFrames)
	private void activateIFrames() {
		System.out.println("The plane is now invulnerable!");
		isIFramed = true;
		iframeTimer = INVINCIBILITY_DURATION; // Start the timer for invulnerability
	}

	public boolean isinVulnerable() {
		return isIFramed;
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

	// Update iFrame timer and reset invincibility when it expires
	private void updateIFrames() {
		if (isIFramed) {
			iframeTimer -= 1 / 30.0; // Decrement the timer by 1/30th of a second each frame
			this.setOpacity(0.5);
			if (iframeTimer <= 0) {
				isIFramed = false; // End invulnerability
				System.out.println("Player no longer invincible!");
				this.setOpacity(1.0);
			}
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateIFrames();
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

	//public void stop() {
		//VerticalvelocityMultiplier = 0;
	//}

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
