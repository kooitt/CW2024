package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Boss class represents the boss plane in the game.
 * It handles movement, shield activation, firing projectiles,
 * and ensures the shield is positioned correctly relative to the boss.
 */
public class Boss extends FighterPlane {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0; // Starting X position
    private static final double INITIAL_Y_POSITION = 400.0; // Starting Y position
    private static final double BOSS_FIRE_RATE = 0.04; // Probability of firing a projectile per frame
    private static final double BOSS_SHIELD_PROBABILITY = 0.01; // Probability of activating the shield
    private static final int IMAGE_HEIGHT = 300; // Height of the boss plane's image
    private static final int VERTICAL_VELOCITY = 8; // Speed of vertical movement
    private static final int HEALTH = 50; // Initial health of the boss plane
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5; // Frequency of movement changes
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10; // Max frames for a single move direction
    private static final int Y_POSITION_UPPER_BOUND = -100; // Upper Y boundary
    private static final int Y_POSITION_LOWER_BOUND = 475; // Lower Y boundary
    private static final int MAX_FRAMES_WITH_SHIELD = 300; // Max duration for shield in frames
    private static final boolean DEBUG_HITBOXES = true; // Enable or disable hitbox visualization

    private final List<Integer> movePattern; // Movement pattern for vertical motion
    private boolean isShielded; // Whether the boss has an active shield
    private int consecutiveMovesInSameDirection; // Tracks consecutive frames with the same move
    private int indexOfCurrentMove; // Current move index in the movement pattern
    private int framesWithShieldActivated; // Number of frames the shield has been active

    private final LevelBoss levelBoss; // Reference to the LevelBoss instance

    /**
     * Constructor for the Boss class.
     *
     * @param levelBoss Reference to the LevelBoss instance.
     */
    public Boss(LevelBoss levelBoss) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        this.levelBoss = levelBoss;
        this.movePattern = new ArrayList<>();
        this.consecutiveMovesInSameDirection = 0;
        this.indexOfCurrentMove = 0;
        this.framesWithShieldActivated = 0;
        this.isShielded = false;
        initializeMovePattern();
    }

    /**
     * Updates the position of the boss plane and synchronizes the shield's position to stay in front of and slightly above the boss.
     */
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove());
        double currentPosition = getLayoutY() + getTranslateY();

        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY); // Reset position if out of bounds
        }

        // Synchronize the shield's position with the boss
        synchronizeShieldPosition();
    }

    /**
     * Synchronizes the shield's position to always appear in front of and slightly above the boss plane.
     */
    private void synchronizeShieldPosition() {
        if (levelBoss != null && levelBoss.getLevelViewBoss() != null) {
            ShieldImage shieldImage = levelBoss.getLevelViewBoss().getShieldImage();
            
            // Adjust the shield's position based on the visual requirements
            shieldImage.setLayoutX(this.getLayoutX() + 30); // Position shield to the right
            shieldImage.setLayoutY(this.getLayoutY() + this.getTranslateY() - 50); // Position shield slightly above
        }
    }

    /**
     * Updates the actor state, including movement and shield logic.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateShield();
    }

    /**
     * Fires a projectile from the boss plane's current position based on a probability.
     *
     * @return A new BossProjectile instance if fired, otherwise null.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < BOSS_FIRE_RATE) {
            double targetY = getLayoutY() + getTranslateY(); // Aim at the current position
            return new BossProjectile(targetY);
        }
        return null;
    }

    /**
     * Handles damage taken by the boss plane. Damage is blocked if the shield is active.
     */
    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
            levelBoss.updateBossHealthDisplay(getHealth()); // Notify LevelBoss to update health display
        }
    }

    /**
     * Initializes the movement pattern for the boss plane.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(0); // No movement
        }
        Collections.shuffle(movePattern);
    }

    /**
     * Retrieves the next move in the movement pattern.
     *
     * @return The vertical velocity for the next move.
     */
    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;

        // Shuffle the movement pattern if the current move has been repeated too many times
        if (consecutiveMovesInSameDirection >= MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern); // Randomize movement pattern
            consecutiveMovesInSameDirection = 0; // Reset counter
            indexOfCurrentMove++;
        }

        // Reset index if it exceeds the pattern size
        if (indexOfCurrentMove >= movePattern.size()) {
            indexOfCurrentMove = 0;
        }

        return currentMove; // Return the next movement value
    }

    /**
     * Updates the shield's state, including activation and deactivation logic.
     */
    private void updateShield() {
        if (isShielded) {
            framesWithShieldActivated++;
        } else if (shieldShouldBeActivated()) {
            activateShield();
        }
        if (shieldExhausted()) {
            deactivateShield();
        }
    }

    /**
     * Determines if the shield should be activated.
     *
     * @return True if the shield should activate, otherwise false.
     */
    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    /**
     * Checks if the shield's duration has been exhausted.
     *
     * @return True if the shield should deactivate, otherwise false.
     */
    private boolean shieldExhausted() {
        return framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
    }

    /**
     * Activates the boss's shield.
     */
    private void activateShield() {
        isShielded = true;
        levelBoss.updateShieldState(); // Ensure the shield is visually displayed
    }

    /**
     * Deactivates the boss's shield.
     */
    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
        levelBoss.updateShieldState(); // Ensure the shield is visually hidden
    }

    /**
     * Checks if the boss currently has an active shield.
     *
     * @return True if the shield is active, otherwise false.
     */
    public boolean isShielded() {
        return isShielded;
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * The reduced bounds create a smaller hitbox for more precise collisions.
     *
     * @return A BoundingBox representing the reduced hitbox.
     */
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        double margin = 30; // Adjust the margin to shrink the hitbox
        return new BoundingBox(
            originalBounds.getMinX() + margin,
            originalBounds.getMinY() + margin,
            originalBounds.getWidth() - 2 * margin,
            originalBounds.getHeight() - 2 * margin
        );
    }

    /**
     * Renders the hitbox for debugging purposes.
     *
     * @param root The Group object representing the game scene's root node.
     */
    public void renderHitbox(Group root) {
        if (!DEBUG_HITBOXES) return; // Skip rendering if debugging is disabled

        Bounds bounds = getReducedBounds();
        Rectangle hitbox = new Rectangle(
            bounds.getMinX(),
            bounds.getMinY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
        hitbox.setFill(Color.TRANSPARENT); // Transparent fill
        hitbox.setStroke(Color.RED);       // Red outline for the hitbox
        hitbox.setStrokeWidth(2);          // Optional: Adjust stroke width for better visibility

        root.getChildren().add(hitbox);    // Add the hitbox rectangle to the game scene
    }
}
