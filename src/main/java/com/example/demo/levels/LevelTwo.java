package com.example.demo.levels;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.Boss;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import com.example.demo.ui.HeartDisplay;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.util.Duration;

/**
 * LevelTwo represents the second level of the game, featuring a boss battle, power-ups,
 * and a transition to the next level. This class extends {@link LevelParent} and defines
 * level-specific logic, including enemy spawning, power-ups, and victory conditions.
 */
public class LevelTwo extends LevelParent {

    /**
     * Path to the background image for Level Two.
     */
    private static final String BACKGROUND_IMAGE = "/com/example/demo/images/background2.jpg";

    /**
     * Fully qualified name of the next level class.
     */
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";

    /**
     * Probability for spawning power-up items in the level.
     */
    private static final double POWER_UP_PROBABILITY = 0.005;

    /**
     * Probability for spawning heart items to restore player health.
     */
    private static final double HEART_SPAWN_PROBABILITY = 0.005;

    /**
     * Maximum health of the player in this level.
     */
    private static final int MAX_HEALTH = 3;

    /**
     * The boss enemy for this level.
     */
    private final Boss boss;

    /**
     * Flag indicating if the game is transitioning to the next level.
     */
    private boolean transitioningToNextLevel = false;

    /**
     * Flag indicating if the boss down sound has finished playing.
     */
    private boolean bossdownSoundFinished = false;

    /**
     * Flag indicating if the boss explosion animation has finished.
     */
    private boolean bossExplosionFinished = false;

    /**
     * Constructs a new LevelTwo instance.
     *
     * @param screenHeight the height of the game screen.
     * @param screenWidth  the width of the game screen.
     * @param controller   the game controller managing the level.
     */
    public LevelTwo(double screenHeight, double screenWidth, Controller controller) {
        super(BACKGROUND_IMAGE, screenHeight, screenWidth, controller);
        SoundComponent.stopAllSound();
        SoundComponent.playLevel2Sound();
        boss = new Boss(getRoot(), this);
        boss.setOnExplosionFinished(() -> {
            bossExplosionFinished = true;
            checkIfReadyToProceed();
        });
        getUser().getHealthComponent().setMaxHealth(MAX_HEALTH);
    }

    /**
     * Initializes the friendly units for this level. This adds the player-controlled unit to the game root.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Checks if the game is over by evaluating the player's or boss's state.
     * If the player is destroyed, the game ends with a loss.
     * If the boss is destroyed, transitions to the next level.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            if (boss != null) {
                boss.stopShieldTimeline();
            }
            loseGame();
        } else if (boss.isDestroyed() && !transitioningToNextLevel) {
            transitioningToNextLevel = true;
            isInputEnabled = false;
            getUser().stopShooting();
            SoundComponent.playBossdownSound(() -> {
                bossdownSoundFinished = true;
                checkIfReadyToProceed();
            });
        }
    }

    /**
     * Checks if all conditions are met to proceed to the next level.
     * If both the boss down sound and explosion animation are finished, transitions to the next level.
     */
    private void checkIfReadyToProceed() {
        if (bossdownSoundFinished && bossExplosionFinished) {
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
    }

    /**
     * Spawns enemy units for this level. If there are no existing enemies, the boss is added to the game.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (!isInputEnabled) return;
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
            animateBossEntry();
        }
        spawnPowerUps();
    }

    /**
     * Animates the boss's entry from off-screen to its final position.
     */
    private void animateBossEntry() {
        double bossFinalX = boss.getLayoutX();
        double offScreenStartX = getScreenWidth() + 100;
        boss.setLayoutX(offScreenStartX);
        Timeline bossIntroTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(boss.layoutXProperty(), offScreenStartX)),
                new KeyFrame(Duration.millis(800), new KeyValue(boss.layoutXProperty(), bossFinalX, Interpolator.EASE_IN))
        );
        bossIntroTimeline.play();
    }

    /**
     * Spawns power-ups during the level. Spawns either an ActorLevelUp or HeartItem based on probabilities.
     */
    private void spawnPowerUps() {
        if (Math.random() < POWER_UP_PROBABILITY) {
            addPowerUp(new ActorLevelUp(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
        if (getUser().getCurrentHealth() < getUser().getMaxHealth() && Math.random() < HEART_SPAWN_PROBABILITY) {
            addPowerUp(new HeartItem(getScreenWidth(), Math.random() * getEnemyMaximumYPosition()));
        }
    }

    /**
     * Adds a power-up to the game root and power-ups list.
     *
     * @param powerUp the power-up to be added.
     */
    private void addPowerUp(Actor powerUp) {
        getRoot().getChildren().add(powerUp);
        powerUps.add(powerUp);
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
        return super.NewHeartDisplay(root, xPosition, yPosition, 3);
    }

    /**
     * Provides the objective text for this level.
     *
     * @return the objective text for this level.
     */
    @Override
    protected String getObjectiveText() {
        return "Objective: Defeat the Boss with a maximum health of 3";
    }
}