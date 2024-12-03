package com.example.demo.levels;

import com.example.demo.actors.ActorLevelUp;
import com.example.demo.actors.Boss;
import com.example.demo.actors.HeartItem;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;
import com.example.demo.components.SoundComponent;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final double POWER_UP_SPAWN_PROBABILITY = 0.005; // 新增道具生成概率
	private static final double HEART_SPAWN_PROBABILITY = 0.005; // 新增爱心道具生成概率
	private static final int PLAYER_INITIAL_HEALTH = 1; // 初始血量设为1
	private static final int PLAYER_MAX_HEALTH = 2; // 最大血量设为2
	private final Boss boss;
	private LevelViewLevelTwo levelView;

	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth);

		SoundComponent.stopAllSound();
		SoundComponent.playLevel2Sound();;

		// 初始化Boss
		boss = new Boss(getRoot(), this);

		// 设置玩家的最大血量和当前血量
		getUser().getHealthComponent().setMaxHealth(PLAYER_MAX_HEALTH);
		getUser().getHealthComponent().setCurrentHealth(PLAYER_INITIAL_HEALTH);
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

		// 随机生成 HeartItem
		if (Math.random() < HEART_SPAWN_PROBABILITY) {
			double x = getScreenWidth(); // 从屏幕右侧生成
			double y = Math.random() * getEnemyMaximumYPosition(); // 随机Y坐标
			HeartItem heart = new HeartItem(x, y);
			powerUps.add(heart); // 将爱心道具添加到 powerUps 列表
			getRoot().getChildren().add(heart);
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		// 初始化 LevelViewLevelTwo 并传递最大血量
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_MAX_HEALTH);
		return levelView;
	}
}
