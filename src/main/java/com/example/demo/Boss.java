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
 * Boss represents the boss plane in the game.
 * It extends FighterPlane and includes logic for movement, firing projectiles, shields, and hitbox visualization.
 */
public class Boss extends FighterPlane {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0; // Starting X position
    private static final double INITIAL_Y_POSITION = 400.0; // Starting Y position
    private static final double BOSS_FIRE_RATE = 0.04; // Probability of firing a projectile per frame
    private static final double BOSS_SHIELD_PROBABILITY = 0.002; // Probability of activating shield per frame
    private static final int IMAGE_HEIGHT = 300; // Height of the boss plane's image
    private static final int VERTICAL_VELOCITY = 8; // Speed of vertical movement
    private static final int HEALTH = 100; // Initial health of the boss plane
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5; // Frequency of movement changes
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10; // Max frames for a single move direction
    private static final int Y_POSITION_UPPER_BOUND = -100; // Upper Y boundary
    private static final int Y_POSITION_LOWER_BOUND = 475; // Lower Y boundary
    private static final int MAX_FRAMES_WITH_SHIELD = 500; // Max duration for shield
    private static final boolean DEBUG_HITBOXES = true; // Enable or disable hitbox visualization

    private final List<Integer> movePattern; // Movement pattern for vertical motion
    private boolean isShielded; // Whether the boss has an active shield
    private int consecutiveMovesInSameDirection; // Tracks consecutive frames with the same move
    private int indexOfCurrentMove; // Current move index in the move pattern
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
     * Updates the position of the boss plane based on its movement pattern.
     */
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove());
        double currentPosition = getLayoutY() + getTranslateY();
        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY); // Reset position if out of bounds
        }
    }

    /**
     * Updates the state of the boss plane, including movement and shield status.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateShield();
    }

    /**
     * Fires a projectile from the boss plane's current position with a certain probability.
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
     * Handles damage taken by the boss plane. Shield blocks damage when active.
     */
    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
            levelBoss.updateBossHealthDisplay(getHealth()); // Notify LevelBoss about health changes
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
     * Updates the shield status of the boss plane.
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
     * Retrieves the next move in the movement pattern.
     *
     * @return The vertical velocity for the next move.
     */
    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
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
    }

    /**
     * Deactivates the boss's shield.
     */
    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
    }

    /**
     * Checks if the boss currently has an active shield.
     *
     * @return True if shielded, otherwise false.
     */
    public boolean isShielded() {
        return isShielded;
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * This reduces the effective hitbox of the boss plane for better gameplay experience.
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
     * Renders the hitbox for debugging purposes. Adds a rectangle to the game scene to visualize the reduced hitbox.
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
