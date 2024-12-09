package com.example.demo.levels;

import com.example.demo.actors.Actor.EnemyPlaneOne;
import com.example.demo.actors.Actor.EnemyPlaneTwo;
import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.animation.Interpolator;

/**
 * Represents the first level of the game.
 * LevelOne initializes the gameplay for the first stage, handling the spawning of enemies,
 * transition to the next level, and game-over conditions.
 */
public class LevelOne extends LevelParent {

    /**
     * Path to the background image for this level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";

    /**
     * The fully qualified name of the next level class to transition to.
     */
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";

    /**
     * The total number of enemies to be spawned in this level.
     */
    private static final int TOTAL_ENEMIES = 20;

    /**
     * The number of enemies spawned at a time.
     */
    private static final int ENEMIES_PER_SPAWN = 5;

    /**
     * Probability of spawning an enemy of type {@link EnemyPlaneOne}.
     */
    private static final double ENEMY_SPAWN_PROBABILITY_ONE = 0.30;

    /**
     * Probability of spawning an enemy of type {@link EnemyPlaneTwo}.
     */
    private static final double ENEMY_SPAWN_PROBABILITY_TWO = 0.20;

    /**
     * Tracks the total number of enemies that have been spawned so far.
     */
    private int spawnedEnemies = 0;

    /**
     * Indicates whether the level is transitioning to the next level.
     */
    private boolean transitioningToNextLevel = false;

    /**
     * Constructs a new instance of LevelOne.
     *
     * @param screenHeight the height of the screen.
     * @param screenWidth the width of the screen.
     * @param controller the controller managing the game state and transitions.
     */
    public LevelOne(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel1Sound();
    }

    /**
     * Checks if the game is over, either due to the player's destruction
     * or the successful completion of the level.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
            return;
        }

        if (spawnedEnemies == TOTAL_ENEMIES && getCurrentNumberOfEnemies() == 0 && !transitioningToNextLevel) {
            transitioningToNextLevel = true;
            isInputEnabled = false;
            getUser().stopShooting();
            SoundComponent.stopLevel1Sound();
            proceedToNextLevel();
        }
    }

    /**
     * Proceeds to the next level by transitioning the player's character off the screen.
     */
    private void proceedToNextLevel() {
        Platform.runLater(() -> {
            getRoot().getChildren().remove(pauseButton);
            clearAllProjectiles();
            double offScreenX = getScreenWidth() + 100;
            Timeline exitTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(getUser().layoutXProperty(), getUser().layoutXProperty().getValue())),
                    new KeyFrame(Duration.millis(1000), event -> goToNextLevel(NEXT_LEVEL), new KeyValue(getUser().layoutXProperty(), offScreenX, Interpolator.EASE_IN))
            );
            exitTimeline.play();
        });
    }

    /**
     * Initializes the friendly units, such as the player's character, for the level.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units for the level based on defined probabilities.
     * The enemies are spawned in batches, and the total number of enemies is capped.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled || spawnedEnemies >= TOTAL_ENEMIES || getCurrentNumberOfEnemies() > 0) return;

        int enemiesToSpawn = Math.min(ENEMIES_PER_SPAWN, TOTAL_ENEMIES - spawnedEnemies);

        for (int i = 0; i < enemiesToSpawn; i++) {
            double rand = Math.random();
            if (rand < ENEMY_SPAWN_PROBABILITY_ONE) {
                addEnemyUnit(new EnemyPlaneOne(getScreenWidth(), Math.random() * getEnemyMaximumYPosition(), getRoot()));
            } else if (rand < ENEMY_SPAWN_PROBABILITY_ONE + ENEMY_SPAWN_PROBABILITY_TWO) {
                addEnemyUnit(new EnemyPlaneTwo(getScreenWidth(), Math.random() * getEnemyMaximumYPosition(), getRoot()));
            } else {
                addEnemyUnit(new EnemyPlaneOne(getScreenWidth(), Math.random() * getEnemyMaximumYPosition(), getRoot()));
            }
            spawnedEnemies++;
            if (spawnedEnemies >= TOTAL_ENEMIES) break;
        }
    }
}
