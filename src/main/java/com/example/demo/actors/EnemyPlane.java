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

    private static final String IMAGE_NAME = "enemyplane.png";
    private static final int IMAGE_HEIGHT = 150; // Height of the enemy plane's image
    private static final int HORIZONTAL_VELOCITY = -6; // Speed of horizontal movement
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0; // X offset for projectile spawn
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0; // Y offset for projectile spawn
    private static final int INITIAL_HEALTH = 1; // Default health of the enemy plane
    private static final double FIRE_RATE = 0.04; // Probability of firing a projectile per frame
    private static final boolean DEBUG_HITBOXES = true; // Enable or disable hitbox visualization

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
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Fires a projectile from the enemy plane's current position with a certain probability.
     *
     * @return A new EnemyProjectile instance if fired, otherwise null.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    /**
     * Updates the state of the enemy plane. Called in each game loop iteration.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * This reduces the effective hitbox of the enemy plane for better gameplay experience.
     *
     * @return A BoundingBox representing the reduced hitbox.
     */
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        double margin = 25; // Adjust the margin to shrink the hitbox
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
