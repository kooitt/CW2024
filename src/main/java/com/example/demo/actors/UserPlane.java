package com.example.demo.actors;

import com.example.demo.projectiles.UserProjectile;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * UserPlane represents the player's aircraft in the game.
 * It extends FighterPlane and includes logic for movement, firing projectiles, and hitbox visualization.
 */
public class UserPlane extends FighterPlane {

    // Image file name for the user plane
    private static final String IMAGE_NAME = "userplane.png";

    // Movement boundaries for the user plane
    private static final double Y_UPPER_BOUND = -40; // Upper boundary for movement
    private static final double Y_LOWER_BOUND = 600.0; // Lower boundary for movement

    // Initial position and size parameters for the user plane
    private static final double INITIAL_X_POSITION = 5.0; // Starting X position
    private static final double INITIAL_Y_POSITION = 300.0; // Starting Y position
    private static final int IMAGE_HEIGHT = 150; // Height of the plane's image

    // Movement and projectile parameters
    private static final int VERTICAL_VELOCITY = 11; // Speed of vertical movement
    private static final int PROJECTILE_X_POSITION = 110; // X offset for projectile spawn
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20; // Y offset for projectile spawn

    // Debugging toggle for hitbox visualization
    private static final boolean DEBUG_HITBOXES = true;

    // Current movement direction: -1 for up, 1 for down, 0 for no movement
    private int velocityMultiplier;

    // Tracks the number of kills made by the user plane
    private int numberOfKills;

    /**
     * Constructor for UserPlane.
     *
     * @param initialHealth The starting health of the user plane.
     */
    public UserPlane(int initialHealth) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        this.velocityMultiplier = 0; // Initialize as stationary
        this.numberOfKills = 0; // Initialize kill count to zero
    }

    /**
     * Updates the position of the user plane based on its current velocityMultiplier.
     * Ensures the plane stays within the defined movement boundaries.
     */
    @Override
    public void updatePosition() {
        if (isMoving()) { // Check if the plane is currently moving
            double initialTranslateY = getTranslateY(); // Store the initial position
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier); // Update position based on velocity
            double newPosition = getLayoutY() + getTranslateY(); // Calculate new position

            // Reset position if it goes out of bounds
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY);
            }
        }
    }

    /**
     * Updates the state of the user plane. Called in each game loop iteration.
     * Currently handles movement logic.
     */
    @Override
    public void updateActor() {
        updatePosition(); // Update the position of the user plane
    }

    /**
     * Fires a projectile from the user plane's current position.
     *
     * @return A new UserProjectile instance.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        // Create and return a new projectile at the calculated position
        return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
    }

    /**
     * Checks if the user plane is currently moving.
     *
     * @return True if the plane is moving, otherwise false.
     */
    private boolean isMoving() {
        return velocityMultiplier != 0; // Movement occurs if velocityMultiplier is not zero
    }

    /**
     * Initiates upward movement for the user plane.
     */
    public void moveUp() {
        velocityMultiplier = -1; // Set velocityMultiplier for upward movement
    }

    /**
     * Initiates downward movement for the user plane.
     */
    public void moveDown() {
        velocityMultiplier = 1; // Set velocityMultiplier for downward movement
    }

    /**
     * Stops the user plane's movement.
     */
    public void stop() {
        velocityMultiplier = 0; // Set velocityMultiplier to zero to stop movement
    }

    /**
     * Gets the number of kills made by the user plane.
     *
     * @return The kill count.
     */
    public int getNumberOfKills() {
        return numberOfKills;
    }

    /**
     * Increments the kill count by 1.
     */
    public void incrementKillCount() {
        numberOfKills++; // Increase the kill count by 1
    }

    /**
     * Resets the kill count to zero.
     */
    public void resetKillCount() {
        this.numberOfKills = 0; // Reset the kill count
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * This reduces the effective hitbox of the user plane for better gameplay experience.
     *
     * @return A BoundingBox representing the reduced hitbox.
     */
    @Override
    public Bounds getReducedBounds() {
        // Get the original bounds of the user plane
        Bounds originalBounds = this.getBoundsInLocal();
        double margin = 20; // Adjust the margin to shrink the hitbox

        // Return a reduced bounding box with the calculated margin
        return new BoundingBox(
            originalBounds.getMinX() + margin,
            originalBounds.getMinY() + margin,
            originalBounds.getWidth() - 2 * margin,
            originalBounds.getHeight() - 2 * margin
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
