package com.example.demo.actors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.demo.levels.LevelBoss;
import com.example.demo.projectiles.BossProjectile;
import com.example.demo.ui.ShieldImage;

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

    // Image file name for the boss plane
    private static final String IMAGE_NAME = "bossplane.png";
    
    // Initial position of the boss plane
    private static final double INITIAL_X_POSITION = 1000.0; // Starting X position
    private static final double INITIAL_Y_POSITION = 400.0;  // Starting Y position

    // Probability values for boss actions
    private static final double BOSS_FIRE_RATE = 0.04; // Probability of firing a projectile per frame
    private static final double BOSS_SHIELD_PROBABILITY = 0.01; // Probability of activating the shield

    // Visual and gameplay parameters for the boss plane
    private static final int IMAGE_HEIGHT = 300; // Height of the boss plane's image
    private static final int VERTICAL_VELOCITY = 8; // Speed of vertical movement
    private static final int HEALTH = 50; // Initial health of the boss plane
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5; // Frequency of movement changes
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10; // Max frames for a single move direction
    private static final int Y_POSITION_UPPER_BOUND = -100; // Upper Y boundary
    private static final int Y_POSITION_LOWER_BOUND = 475;  // Lower Y boundary
    private static final int MAX_FRAMES_WITH_SHIELD = 300;  // Max duration for shield in frames

    // Enable or disable hitbox visualization (debugging tool)
    private static final boolean DEBUG_HITBOXES = true;

    // Movement pattern and state tracking for the boss
    private final List<Integer> movePattern; // Movement pattern for vertical motion
    private boolean isShielded;              // Whether the boss has an active shield
    private int consecutiveMovesInSameDirection; // Tracks consecutive frames with the same move
    private int indexOfCurrentMove;          // Current move index in the movement pattern
    private int framesWithShieldActivated;   // Number of frames the shield has been active

    // Reference to the LevelBoss instance
    private final LevelBoss levelBoss;

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
        initializeMovePattern(); // Create the movement pattern for the boss
    }

    /**
     * Updates the position of the boss plane and synchronizes the shield's position.
     * Ensures the boss stays within the game boundaries.
     */
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove()); // Move based on the next movement pattern value

        // Ensure the boss does not go out of bounds
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

            // Adjust the shield's position based on the boss's current position
            shieldImage.setLayoutX(this.getLayoutX() + 30); // Position shield to the right
            shieldImage.setLayoutY(this.getLayoutY() + this.getTranslateY() - 50); // Position shield slightly above
        }
    }

    /**
     * Updates the actor state, including movement and shield logic.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Update the position of the boss
        updateShield();   // Handle shield activation and deactivation logic
    }

    /**
     * Fires a projectile from the boss plane's current position based on a probability.
     *
     * @return A new BossProjectile instance if fired, otherwise null.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < BOSS_FIRE_RATE) { // Random chance to fire a projectile
            double targetY = getLayoutY() + getTranslateY(); // Aim at the current position
            return new BossProjectile(targetY); // Create and return the projectile
        }
        return null;
    }

    /**
     * Handles damage taken by the boss plane. Damage is blocked if the shield is active.
     */
    @Override
    public void takeDamage() {
        if (!isShielded) { // Only take damage if the shield is inactive
            super.takeDamage(); // Call the parent method to reduce health
            levelBoss.updateBossHealthDisplay(getHealth()); // Notify LevelBoss to update health display
        }
    }

    /**
     * Initializes the movement pattern for the boss plane.
     * The movement alternates between upward, downward, and stationary.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);  // Move up
            movePattern.add(-VERTICAL_VELOCITY); // Move down
            movePattern.add(0);                 // No movement
        }
        Collections.shuffle(movePattern); // Randomize the movement pattern
    }

    /**
     * Retrieves the next move in the movement pattern.
     *
     * @return The vertical velocity for the next move.
     */
    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove); // Get the current move
        consecutiveMovesInSameDirection++;

        // Shuffle the movement pattern if the current move is repeated too many times
        if (consecutiveMovesInSameDirection >= MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0; // Reset the counter
            indexOfCurrentMove++;
        }

        // Reset the index if it exceeds the pattern size
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
            framesWithShieldActivated++; // Track how long the shield has been active
        } else if (shieldShouldBeActivated()) {
            activateShield(); // Activate the shield
        }
        if (shieldExhausted()) {
            deactivateShield(); // Deactivate the shield if the duration is exceeded
        }
    }

    /**
     * Determines if the shield should be activated.
     *
     * @return True if the shield should activate, otherwise false.
     */
    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY; // Random chance for shield activation
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
        isShielded = true; // Set shielded state to true
        levelBoss.updateShieldState(); // Notify the level to visually display the shield
    }

    /**
     * Deactivates the boss's shield.
     */
    private void deactivateShield() {
        isShielded = false; // Set shielded state to false
        framesWithShieldActivated = 0; // Reset the shield activation counter
        levelBoss.updateShieldState(); // Notify the level to visually hide the shield
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

        // Get the reduced hitbox bounds
        Bounds bounds = getReducedBounds();
        
        // Create a rectangle to represent the hitbox
        Rectangle hitbox = new Rectangle(
            bounds.getMinX(),
            bounds.getMinY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
        hitbox.setFill(Color.TRANSPARENT); // Transparent fill for the hitbox
        hitbox.setStroke(Color.RED);       // Red outline for visibility
        hitbox.setStrokeWidth(2);          // Optional: Adjust stroke width

        root.getChildren().add(hitbox); // Add the hitbox to the scene for debugging
    }
}
