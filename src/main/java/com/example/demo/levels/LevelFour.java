package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.enemies.Boss;
import com.example.demo.actors.obstacles.Asteroid;
import com.example.demo.actors.obstacles.Satellite;
import com.example.demo.levelparent.LevelParent;
import com.example.demo.controller.SoundManager;
import javafx.stage.Stage;

public class LevelFour extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/Backgrounds/level4alt.png";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int MAX_OBSTACLES = 3;
	private static final double OBSTACLE_SPAWN_PROBABILITY = .5;
	private final Boss boss;

	//sounds
	private SoundManager soundManager;
	private static final String LEVEL_BG_MUSIC = "/com/example/demo/sfx/level_music/bossMusic.mp3";

    public LevelFour(double screenHeight, double screenWidth, Stage stage) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, stage);
		soundManager = SoundManager.getInstance(); // Initialize SoundManager instance
		soundManager.playBackgroundMusic(LEVEL_BG_MUSIC); // Play background music for the level
		boss = new Boss();
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed() && !(boss.isDestroyed())) {
			loseGame();
		} //Added additional condition so both would not be met
		else if (boss.isDestroyed()) {
			soundManager.stopBackgroundMusic();
			winGame();
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
			getRoot().getChildren().add(boss.getshieldImage());
		}
	}

	@Override
	protected ActiveActorDestructible createObstacle() {
		double newObstacleInitialYPosition = Math.random() * getEnemyMaximumYPosition();
		if (Math.random() < 0.1) {
			return new Satellite(getScreenWidth(), newObstacleInitialYPosition);
		} else {
			return new Asteroid(getScreenWidth(), newObstacleInitialYPosition);
		}
	}//Environmental hazards do not count as enemies, so a different logic is used.

	@Override
	protected double getObstacleSpawnProbability() {
		return OBSTACLE_SPAWN_PROBABILITY;
	}

	@Override
	protected int getTotalObstacles() {
		return MAX_OBSTACLES;
	}

	@Override
	protected LevelView instantiateLevelView() {
        return new LevelViewLevelFour(getRoot(), PLAYER_INITIAL_HEALTH);
	}

}
