package com.example.demo.levels;

import com.example.demo.actors.planes.PlaneFactory;
import com.example.demo.actors.planes.bosses.Boss;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "background2.jpg";
	//private static final String BGM_NAME = "/com/example/demo/audio/httyd.mp4";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private LevelTwoView levelView;

	public LevelTwo(double screenHeight, double screenWidth) {
		super(
				BACKGROUND_IMAGE_NAME,
				//BGM_NAME,
				screenHeight,
				screenWidth,
				PLAYER_INITIAL_HEALTH,
				() -> PlaneFactory.createUserPlane());
		boss = PlaneFactory.createBoss();
	}

	@Override
	public void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
		getRoot().getChildren().add(boss.getShieldImage());
		getRoot().getChildren().add(boss.getHitbox());
	}

	@Override
	public void checkIfGameOver() {
		if (userIsDestroyed()) {
			getController().loseGame();
		}
		else if (boss.isDestroyed()) {
			//getController().winGame();
			System.out.println("level2");
			goToNextLevel(NEXT_LEVEL);
		}
	}

	@Override
	public void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	@Override
	public LevelParentView instantiateLevelView() {
		levelView = new LevelTwoView(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}

}
