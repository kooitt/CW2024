package com.example.demo.levels;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.Boss;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;


public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final double POWER_UP_SPAWN_PROBABILITY = 0.01; // 新增道具生成概率
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private LevelViewLevelTwo levelView;

	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);
		boss = new Boss(getRoot(), this);
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (boss.isDestroyed()) {
			goToNextLevel("com.example.demo.levels.LevelThree");
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
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
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}
}
