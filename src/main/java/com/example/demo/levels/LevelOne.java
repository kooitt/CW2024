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
 * <p>
 * This class manages the initialization of gameplay elements for the first stage,
 * including the spawning of enemy units, level progression, and game-over conditions.
 * </p>
 */
public class LevelOne extends LevelParent {

    /**
     * Path to the background image used in this level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";

    /**
     * The fully qualified name of the next level class for transitioning.
     */
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";

    /**
     * The total number of enemies that will be spawned in this level.
     */
    private static final int TOTAL_ENEMIES = 25;

    /**
     * The number of enemies spawned in each wave.
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
     * The total number of enemies that have been spawned so far.
     */
    private int spawnedEnemies = 0;

    /**
     * Indicates whether the level is currently transitioning to the next stage.
     */
    private boolean transitioningToNextLevel = false;

    /**
     * Tracks the current wave of enemies that the player is facing.
     */
    private int currentWave = 0;

    /**
     * Constructs the first level of the game.
     *
     * @param screenHeight the height of the game screen.
     * @param screenWidth  the width of the game screen.
     * @param controller   the controller managing the game's state and transitions.
     */
    public LevelOne(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel1Sound();
    }

    /**
     * Checks if the game is over by evaluating the player's state or completion of the level objectives.
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
     * Transitions to the next level by animating the player's character off the screen and initializing the next stage.
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
     * Initializes friendly units, such as the player's character, for this level.
     * <p>
     * This method ensures that the player's character is added to the game root for interaction and visibility.
     * </p>
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units based on predefined probabilities and caps the total number of enemies.
     * <p>
     * This method spawns enemies in waves, and their types are determined randomly
     * based on the probabilities defined in {@link #ENEMY_SPAWN_PROBABILITY_ONE} and
     * {@link #ENEMY_SPAWN_PROBABILITY_TWO}.
     * </p>
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
        }

        if (spawnedEnemies % ENEMIES_PER_SPAWN == 0 && currentWave < 5) {
            currentWave++;
            updateObjectiveLabel(getObjectiveText());
        }
    }

    /**
     * Retrieves the current objective text for the level.
     * <p>
     * This method provides information about the player's progress in completing the current wave of enemies.
     * </p>
     *
     * @return the objective text as a string.
     */
    @Override
    protected String getObjectiveText() {
        return "Objective: Defeat 5 waves of enemies\nCurrent Wave: " + currentWave;
    }
}
