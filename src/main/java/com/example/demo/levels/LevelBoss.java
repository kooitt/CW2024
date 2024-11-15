package com.example.demo.levels;

import com.example.demo.actors.Boss;
import com.example.demo.view.LevelBossView;
import com.example.demo.view.LevelView;

/**
 * Represents the boss level in the game.
 */
public class LevelBoss extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int KILLS_TO_ADVANCE = 1;
	private final Boss boss;
	private LevelBossView levelView;

	/**
	 * Constructs a LevelBoss with the specified screen dimensions.
	 *
	 * @param screenHeight the height of the screen.
	 * @param screenWidth the width of the screen.
	 */
	public LevelBoss(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
		//levelView.showShield();
	}

	/**
	 * Initializes the friendly units in the level.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Checks if the game is over by evaluating the state of the user and the boss.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (boss.isDestroyed()) {
			winGame();
		}
	}

	/**
	 * Spawns enemy units in the level.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	/**
	 * Instantiates the view for the boss level.
	 *
	 * @return the view for the boss level.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelBossView(getRoot(), PLAYER_INITIAL_HEALTH, KILLS_TO_ADVANCE);
		return levelView;
	}
}