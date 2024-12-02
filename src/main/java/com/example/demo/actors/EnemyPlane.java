package com.example.demo.actors;

import com.example.demo.projectiles.EnemyProjectile;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * EnemyPlane represents an enemy aircraft in the game.
 * It extends FighterPlane and includes logic for movement, firing projectiles, and hitbox visualization.
 */
public class EnemyPlane extends FighterPlane {

    // Image file name for the enemy plane
    private static final String IMAGE_NAME = "enemyplane.png";

    // Visual and gameplay constants for the enemy plane
    private static final int IMAGE_HEIGHT = 150; // Height of the enemy plane's image
    private static final int HORIZONTAL_VELOCITY = -6; // Speed of horizontal movement
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0; // X offset for projectile spawn
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;   // Y offset for projectile spawn
    private static final int INITIAL_HEALTH = 1; // Default health of the enemy plane
    private static final double FIRE_RATE = 0.04; // Probability of firing a projectile per frame

    // Debugging toggle for hitbox visualization
    private static final boolean DEBUG_HITBOXES = true;

    /**
     * Constructor for EnemyPlane.
     *
     * @param initialXPos The initial X position of the enemy plane.
     * @param initialYPos The initial Y position of the enemy plane.
     */
    public EnemyPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    /**
     * Updates the position of the enemy plane by moving it horizontally.
     * Called in each game loop iteration to simulate the plane's movement.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY); // Move the enemy plane to the left
    }

    /**
     * Fires a projectile from the enemy plane's current position with a certain probability.
     *
     * @return A new EnemyProjectile instance if fired, otherwise null.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) { // Random chance to fire a projectile
            // Calculate the projectile's spawn position
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPosition); // Create and return a new projectile
        }
        return null; // No projectile fired
    }

    /**
     * Updates the state of the enemy plane. This method is called in each game loop iteration.
     * Handles movement and other logic updates for the enemy plane.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Update the position of the enemy plane
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * This reduces the effective hitbox of the enemy plane for better gameplay experience.
     *
     * @return A BoundingBox representing the reduced hitbox.
     */
    @Override
    public Bounds getReducedBounds() {
        // Get the original bounds of the enemy plane
        Bounds originalBounds = this.getBoundsInLocal();
        double margin = 25; // Adjust the margin to shrink the hitbox
        return new BoundingBox(
            originalBounds.getMinX() + margin,  // Adjust left margin
            originalBounds.getMinY() + margin,  // Adjust top margin
            originalBounds.getWidth() - 2 * margin, // Reduce width by margin
            originalBounds.getHeight() - 2 * margin // Reduce height by margin
        );
    }

    /**
     * Renders the hitbox for debugging purposes.
     * Adds a rectangle to the game scene to visualize the reduced hitbox.
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

        root.getChildren().add(hitbox); // Add the hitbox rectangle to the game scene
    }
}
