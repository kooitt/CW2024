package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.enemies.EnemyPlane;
import com.example.demo.levelparent.LevelParent;
import com.example.demo.controller.SoundManager;
import javafx.stage.Stage;

public class LevelOne extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/Backgrounds/level1alt.png";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
	private static final int TOTAL_ENEMIES = 4;
	private static final int KILLS_TO_ADVANCE = 15;
	private static final double ENEMY_SPAWN_PROBABILITY = .15;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	//sounds
	private SoundManager soundManager;
	private static final String LEVEL_BG_MUSIC = "/com/example/demo/sfx/level_music/level1Music.mp3";

	public LevelOne(double screenHeight, double screenWidth, Stage stage) {

		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage);
		soundManager = SoundManager.getInstance(); // Initialize SoundManager instance
		soundManager.playBackgroundMusic(LEVEL_BG_MUSIC); // Play background music for the level
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		}
		else if (userHasReachedKillTarget()) {
			soundManager.stopBackgroundMusic();
			goToNextLevel(NEXT_LEVEL);
		}
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected ActiveActorDestructible createEnemy() {
		double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
		return new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
	}

	@Override
	protected ActiveActorDestructible createObstacle() {
		return null; //Since LevelOne only has basic enemies.
	}

	@Override
	protected double getEnemySpawnProbability() {
		return ENEMY_SPAWN_PROBABILITY;
	}

	@Override
	protected int getTotalEnemies() {
		return TOTAL_ENEMIES;
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

}
