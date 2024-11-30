package com.example.demo.levels;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.views.LevelView;

/**
 * Represents the first level of the game.
 */
public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20;
	private static final double POWER_UP_SPAWN_PROBABILITY = 0.01; // 新增道具生成概率
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * Constructs LevelOne with specified screen dimensions.
	 *
	 * @param screenHeight screen height.
	 * @param screenWidth  screen width.
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget()) {
			goToNextLevel(NEXT_LEVEL);
		}
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double yPos = Math.random() * getEnemyMaximumYPosition();
				ActiveActor enemy = new EnemyPlane(getScreenWidth(), yPos, getRoot());
				addEnemyUnit(enemy);
			}
		}

		// 随机生成 ActorLevelUp
		if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
			double x = getScreenWidth(); // 从屏幕右侧生成
			double y = Math.random() * getEnemyMaximumYPosition(); // 随机Y坐标
			ActorLevelUp powerUp = new ActorLevelUp(x, y);
			powerUps.add(powerUp);
			getRoot().getChildren().add(powerUp);
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}
}
