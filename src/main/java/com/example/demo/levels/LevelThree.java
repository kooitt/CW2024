package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.BossTwo;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import com.example.demo.ui.HeartDisplay;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

/**
 * Represents the third level in the game.
 * This level introduces a challenging boss enemy (BossTwo) and features power-ups and heart items
 * to enhance the gameplay. The level ends when the boss is defeated or the player is destroyed.
 * It extends the {@link LevelParent} class to inherit common game-level functionalities.
 */
public class LevelThree extends LevelParent {

    /**
     * Path to the background image file for this level.
     */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";

    /**
     * Probability of spawning a power-up during a single frame update.
     */
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.01;

    /**
     * Probability of spawning a heart item during a single frame update.
     */
    private static final double HEART_SPAWN_PROBABILITY = 0.005;

    /**
     * Maximum health of the player in this level.
     */
    private static final int MAX_HEALTH = 2;

    /**
     * The boss enemy of this level. Represents a powerful challenge for the player.
     */
    private final BossTwo boss;

    /**
     * Constructs the third level with the specified screen dimensions and controller.
     *
     * @param screenHeight the height of the game screen.
     * @param screenWidth  the width of the game screen.
     * @param controller   the game controller responsible for managing game interactions.
     */
    public LevelThree(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, controller);
        boss = new BossTwo(getRoot(), this);
        SoundComponent.stopAllSound(); // Stops any ongoing sounds from previous levels
        SoundComponent.playLevel3Sound(); // Starts playing the background sound for Level 3
        getUser().getHealthComponent().setMaxHealth(MAX_HEALTH);
    }

    /**
     * Initializes the friendly units for this level.
     * Adds the player-controlled character to the game root, making it visible and active.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks the conditions for game-over scenarios.
     * If the player-controlled character is destroyed, the game is lost.
     * If the boss is destroyed, the player wins the level.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // Triggers the game-over sequence
        } else if (boss.isDestroyed()) {
            winGame(); // Triggers the win sequence
        }
    }

    /**
     * Animates the boss's entry into the level.
     * The boss moves from off-screen on the right to its starting position within the game area.
     */
    private void animateBossTwoEntry() {
        double bossFinalX = boss.getLayoutX(); // The boss's target X position
        double offScreenStartX = getScreenWidth() + 100; // Start position off-screen
        boss.setLayoutX(offScreenStartX);

        // Create and configure the animation timeline for the boss's entry
        Timeline bossTwoIntroTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                new KeyFrame(
                        Duration.millis(1000), // Duration of the animation in milliseconds
                        new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN) // Moves boss to its target position
                )
        );

        bossTwoIntroTimeline.play(); // Starts the animation
    }

    /**
     * Spawns enemy units for this level.
     * Ensures the boss enemy is added to the game root and initializes its entry animation.
     * Periodically spawns power-ups and heart items.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss); // Adds the boss to the game
            animateBossTwoEntry(); // Triggers the boss's entry animation
        }
        spawnPowerUps(); // Spawns power-ups and heart items
    }

    /**
     * Spawns power-ups and heart items at random intervals based on their respective probabilities.
     * - Power-ups enhance the player's abilities temporarily.
     * - Heart items restore health to the player.
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
     * Adds a power-up to the game and makes it visible on the screen.
     *
     * @param powerUp the power-up actor to be added.
     */
    private void addPowerUp(Actor powerUp) {
        powerUps.add(powerUp); // Adds the power-up to the internal tracking list
        getRoot().getChildren().add(powerUp); // Adds the power-up to the game root for rendering
    }

    /**
     * Creates a new HeartDisplay for this level with a fixed number of hearts.
     *
     * @param root            the root group to which the HeartDisplay belongs.
     * @param xPosition       the x-coordinate for the HeartDisplay.
     * @param yPosition       the y-coordinate for the HeartDisplay.
     * @param heartsToDisplay the number of hearts to display.
     * @return a new {@link HeartDisplay} instance.
     */
    @Override
    protected HeartDisplay NewHeartDisplay(Group root, double xPosition, double yPosition, int heartsToDisplay) {
        return super.NewHeartDisplay(root, xPosition, yPosition, 2);
    }

    /**
     * Provides the objective text for this level.
     *
     * @return the objective text for this level.
     */
    @Override
    protected String getObjectiveText() {
        return "Objective: Defeat the Boss with a maximum health of 2";
    }
}
