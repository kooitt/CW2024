package com.example.demo.levels;

import com.example.demo.actors.planes.Boss;
import com.example.demo.factory.EnemyFactory;
import com.example.demo.view.BossHealthBar;
import com.example.demo.view.LevelBossView;
import com.example.demo.view.LevelView;
import com.example.demo.view.ShieldImage;

/**
 * Represents the boss level in the game.
 */
public class LevelBoss extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int KILLS_TO_ADVANCE = 1;
	private final Boss boss;
	private LevelBossView levelView;
	private final ShieldImage shieldImage;
	private static final int SHIELD_X_POSITION = 850; // X-coordinate position of the shield
	private static final int SHIELD_Y_POSITION = 0; // Y-coordinate position of the shield
	private static final int HEALTH_BAR_X_POSITION = SHIELD_X_POSITION + 100;
	private static  final int HEALTH_BAR_Y_POSITION = 30;
	private final EnemyFactory enemyFactory;
	private BossHealthBar bossHealthBar;

	/**
	 * Constructs a LevelBoss with the specified screen dimensions.
	 */
	public LevelBoss() {
		super(BACKGROUND_IMAGE_NAME,  PLAYER_INITIAL_HEALTH);
		this.enemyFactory = new EnemyFactory(EnemyFactory.EnemyType.BOSS);
		this.boss = (Boss) enemyFactory.createActor(0,0);
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		this.bossHealthBar = new BossHealthBar(HEALTH_BAR_X_POSITION, HEALTH_BAR_Y_POSITION, boss.getHealth());
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
			addShieldImage();
			addHealthBar();
		}
	}

	/**
	 * Adds the shield image to the root and sets its visibility to false.
	 */
	private void addShieldImage() {
		shieldImage.setVisible(false);
		getRoot().getChildren().add(shieldImage);
	}

	private void addHealthBar() {
		getRoot().getChildren().add(bossHealthBar);
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

	/**
	 * Updates the level view and shows or hides the shield image based on the boss's shield status.
	 */
	@Override
	protected void updateLevelView() {
		super.updateLevelView();
		// Update the boss health bar
		if (bossHealthBar != null && boss != null) {
			bossHealthBar.updateHealth(boss.getHealth());
		}

		if (boss.isShielded()) {
			shieldImage.showShield();
		} else {
			shieldImage.hideShield();
		}
	}
}