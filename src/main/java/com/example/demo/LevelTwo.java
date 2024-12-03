package com.example.demo;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private LevelTwoView levelView;

	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
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
			getController().winGame();
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
