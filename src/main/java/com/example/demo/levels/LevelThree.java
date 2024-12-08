package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.EliteEnemyPlane;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.actors.Asteroid;
import com.example.demo.levelparent.LevelParent;

public class LevelThree extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelFour";
	private static final int TOTAL_ENEMIES = 10;
	private static final int TOTAL_OBSTACLES = 5;
	private static final int KILLS_TO_ADVANCE = 3;
	private static final double ENEMY_SPAWN_PROBABILITY = .15;
	private static final double ASTEROID_SPAWN_PROBABILITY = .3;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelThree(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		}
		else if (userHasReachedKillTarget())
			goToNextLevel(NEXT_LEVEL);
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy;
				if (Math.random() < 0.3) { // 30% chance for elite enemy
					newEnemy = new EliteEnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				} else { // 70% chance for normal enemy
					newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				}
				addEnemyUnit(newEnemy);
			}
		}
	}

	@Override
	protected void spawnObstacles() {
		int currentNumberOfObstacles = getCurrentNumberOfObstacles();
		for (int i = 0; i < TOTAL_OBSTACLES - currentNumberOfObstacles; i++) {
			if (Math.random() < ASTEROID_SPAWN_PROBABILITY) {
				double newAsteroidInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				Asteroid asteroid = new Asteroid(getScreenWidth(), newAsteroidInitialYPosition);
				addObstacle(asteroid); // Use a new method to handle asteroids spawning
				System.out.println("Asteroid spawned!");
			}
		}
	}//Environmental hazards do not count as enemies, so a different logic is used.

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

}