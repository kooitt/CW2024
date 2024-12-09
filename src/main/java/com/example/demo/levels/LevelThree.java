package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.BossTwo;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Represents the third level in the game.
 * This level features a boss enemy (BossTwo), power-ups, and heart items.
 * It extends the {@link LevelParent} class and defines specific behavior for this level.
 */
public class LevelThree extends LevelParent {

    /**
     * The path to the background image for this level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";

    /**
     * The probability of spawning a power-up during each frame update.
     */
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01;

    /**
     * The probability of spawning a heart item during each frame update.
     */
    private static final double HEART_SPAWN_PROBABILITY = 0.005;

    /**
     * The boss enemy for this level.
     */
    private final BossTwo boss;

    /**
     * Constructs the third level.
     *
     * @param screenHeight the height of the game screen.
     * @param screenWidth  the width of the game screen.
     * @param controller   the game controller managing the level.
     */
    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        boss = new BossTwo(getRoot(), this);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel3Sound();
    }

    /**
     * Initializes friendly units for this level.
     * Adds the user-controlled player character to the game root.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over based on the state of the user and the boss.
     * If the user is destroyed, the game is lost.
     * If the boss is destroyed, the game is won.
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
     * Animates the entry of the BossTwo enemy from outside the right edge of the screen.
     */
    private void animateBossTwoEntry() {
        // Set the initial X coordinate of the boss to be off-screen on the right
        double bossFinalX = boss.getLayoutX();
        double offScreenStartX = getScreenWidth() + 100;
        boss.setLayoutX(offScreenStartX);

        // Create a timeline for the entry animation
        Timeline bossTwoIntroTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                new KeyFrame(
                        Duration.millis(1000), // Duration of the animation
                        new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN) // Move to the target position
                )
        );

        bossTwoIntroTimeline.play();
    }

    /**
     * Spawns enemy units for this level.
     * Ensures the boss is added to the game root with an entry animation.
     * Spawns power-ups periodically.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            animateBossTwoEntry(); // Trigger the boss entry animation
        }
        spawnPowerUps();
    }

    /**
     * Spawns power-ups and heart items based on predefined probabilities.
     * Power-ups enhance the player's abilities.
     * Heart items restore the player's health.
     */
    private void spawnPowerUps() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            addPowerUp(new ActorLevelUp(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
        if (getUser().getCurrentHealth() < getUser().getMaxHealth() && Math.random() < HEART_SPAWN_PROBABILITY) {
            addPowerUp(new HeartItem(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
    }

    /**
     * Adds a power-up to the game.
     *
     * @param powerUp the power-up to be added.
     */
    private void addPowerUp(Actor powerUp) {
        powerUps.add(powerUp);
        getRoot().getChildren().add(powerUp);
    }
}
