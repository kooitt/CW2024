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

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40; // Upper boundary for movement
    private static final double Y_LOWER_BOUND = 600.0; // Lower boundary for movement
    private static final double INITIAL_X_POSITION = 5.0; // Starting X position
    private static final double INITIAL_Y_POSITION = 300.0; // Starting Y position
    private static final int IMAGE_HEIGHT = 150; // Height of the plane's image
    private static final int VERTICAL_VELOCITY = 11; // Speed of vertical movement
    private static final int PROJECTILE_X_POSITION = 110; // X offset for projectile spawn
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20; // Y offset for projectile spawn
    private static final boolean DEBUG_HITBOXES = true; // Enable or disable hitbox visualization

    private int velocityMultiplier; // -1 for up, 1 for down, 0 for no movement
    private int numberOfKills; // Tracks the number of enemies destroyed by the user

    /**
     * Constructor for UserPlane.
     *
     * @param initialHealth The starting health of the user plane.
     */
    public UserPlane(int initialHealth) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        this.velocityMultiplier = 0;
        this.numberOfKills = 0;
    }

    /**
     * Updates the position of the user plane based on its current velocityMultiplier.
     */
    @Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
            double newPosition = getLayoutY() + getTranslateY();
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY); // Reset position if out of bounds
            }
        }
    }

    /**
     * Updates the state of the user plane. Called in each game loop iteration.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Fires a projectile from the user plane's current position.
     *
     * @return A new UserProjectile instance.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
    }

    /**
     * Checks if the user plane is currently moving.
     *
     * @return True if the plane is moving, otherwise false.
     */
    private boolean isMoving() {
        return velocityMultiplier != 0;
    }

    /**
     * Initiates upward movement for the user plane.
     */
    public void moveUp() {
        velocityMultiplier = -1;
    }

    /**
     * Initiates downward movement for the user plane.
     */
    public void moveDown() {
        velocityMultiplier = 1;
    }

    /**
     * Stops the user plane's movement.
     */
    public void stop() {
        velocityMultiplier = 0;
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
        numberOfKills++;
    }

    /**
     * Retrieves the reduced bounds for collision detection.
     * This reduces the effective hitbox of the user plane for better gameplay experience.
     *
     * @return A BoundingBox representing the reduced hitbox.
     */
    @Override
    public Bounds getReducedBounds() {
        Bounds originalBounds = this.getBoundsInLocal();
        double margin = 20; // Adjust the margin to shrink the hitbox
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
    
    public void resetKillCount() {
        this.numberOfKills = 0;
    }

}
