package com.example.demo.actors.planes;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.projectiles.BossProjectile;
import com.example.demo.factory.ProjectileFactory;
import com.example.demo.handlers.SoundManager;

import java.util.*;

/**
 * Represents a boss enemy in the game, which is a type of FighterPlane.
 */
public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 20.0;
	private static final double BOSS_FIRE_RATE = .05;
	private static final double BOSS_SHIELD_PROBABILITY = .002;
	private static final int IMAGE_HEIGHT = 60;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 100; //change the health later
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = 75;
	private static final int Y_POSITION_LOWER_BOUND = 680;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private final ProjectileFactory projectileFactory;

	/**
	 * Constructs a Boss instance with predefined attributes.
	 */
	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		this.projectileFactory = new ProjectileFactory(ProjectileFactory.ProjectileType.BOSS);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
	}

	/**
	 * Updates the position of the boss based on its move pattern.
	 */
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}

	/**
	 * Updates the state of the boss, including its position and shield status.
	 */
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		System.out.println("Boss health: " + getHealth());
	}

	/**
	 * Fires a projectile if the boss decides to fire in the current frame.
	 *
	 * @return a new BossProjectile if the boss fires, otherwise null.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (bossFiresInCurrentFrame()) {
			return projectileFactory.createActor(getLayoutX(), getProjectileInitialPosition());
		}
		return null;
	}

	/**
	 * Inflicts damage to the boss if it is not shielded.
	 */
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
		}
	}

	/**
	 * Initializes the move pattern for the boss.
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/**
	 * Updates the shield status of the boss.
	 */
	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			if (shieldExhausted()) {
				deactivateShield();
			}
		} else if (shieldShouldBeActivated()) {
			activateShield();
		}
	}

	/**
	 * Gets the next move for the boss based on its move pattern.
	 *
	 * @return the next move value.
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	/**
	 * Determines if the boss should fire a projectile in the current frame.
	 *
	 * @return true if the boss fires, false otherwise.
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * Gets the initial position for the boss's projectile.
	 *
	 * @return the Y position for the projectile.
	 */
	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	/**
	 * Determines if the shield should be activated.
	 *
	 * @return true if the shield should be activated, false otherwise.
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	/**
	 * Determines if the shield has been exhausted.
	 *
	 * @return true if the shield is exhausted, false otherwise.
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	/**
	 * Activates the shield for the boss.
	 */
	private void activateShield() {
		isShielded = true;
	}

	/**
	 * Deactivates the shield for the boss.
	 */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}


	public boolean isShielded() {
		return isShielded;
	}

	public int getHealth() {
		return super.getHealth();
	}

}