package com.example.demo;

import java.util.Random;
import com.example.demo.LevelView;
import com.example.demo.EnemyPlane;
import com.example.demo.ActiveActorDestructible;

/**
 * LevelOne is the first level in the game, extending the LevelParent class.
 * It initializes the background, sets the number of enemies, and defines conditions for advancing to the next level.
 */
public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * Constructor for LevelOne.
	 *
	 * @param screenHeight the height of the screen
	 * @param screenWidth the width of the screen
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Checks if the game is over or if the player has reached the kill target to advance to the next level.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget()) {
			goToNextLevel(NEXT_LEVEL);
		}
	}

	/**
	 * Initializes the player's unit on the screen by adding it to the root.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Spawns enemy units on the screen based on the total allowed enemies and spawn probability.
	 */
	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		Random random = new Random();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (random.nextDouble() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = random.nextDouble() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
		}
	}

	/**
	 * Creates and returns the LevelView specific to LevelOne.
	 *
	 * @return the LevelView for this level
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Checks if the user has achieved the required kills to advance to the next level.
	 *
	 * @return true if the user has reached the kill target, false otherwise
	 */
	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}
}
