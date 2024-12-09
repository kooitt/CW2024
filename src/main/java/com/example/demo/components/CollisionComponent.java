package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the collision component for an {@link Actor}, handling hitbox size, position, and collision checks.
 * It can also visualize the hitbox for debugging purposes if the corresponding setting is enabled.
 */
public class CollisionComponent implements Hitbox {

    /**
     * The {@link Actor} that owns this collision component.
     */
    private final Actor owner;

    /**
     * The width of the collision hitbox.
     */
    private double hitboxWidth;

    /**
     * The height of the collision hitbox.
     */
    private double hitboxHeight;

    /**
     * The X offset of the hitbox relative to the owner's position.
     */
    public double offsetX;

    /**
     * The Y offset of the hitbox relative to the owner's position.
     */
    public double offsetY;

    /**
     * A {@link Rectangle} visualization of the hitbox for debugging purposes.
     * Visible only if {@link GameSettings#SHOW_HITBOXES} is enabled.
     */
    private Rectangle hitboxVisualization;

    /**
     * Indicates whether collisions are enabled for this component.
     */
    public boolean collisionEnabled = true;

    /**
     * Constructs a new {@code CollisionComponent} for the given owner.
     *
     * @param owner       The {@link Actor} that owns this collision component.
     * @param hitboxWidth The initial width of the hitbox.
     * @param hitboxHeight The initial height of the hitbox.
     * @param offsetX     The initial X offset of the hitbox relative to the owner's position.
     * @param offsetY     The initial Y offset of the hitbox relative to the owner's position.
     */
    public CollisionComponent(Actor owner, double hitboxWidth, double hitboxHeight, double offsetX, double offsetY) {
        this.owner = owner;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        if (GameSettings.SHOW_HITBOXES) {
            hitboxVisualization = new Rectangle(hitboxWidth, hitboxHeight, Color.TRANSPARENT);
            hitboxVisualization.setStroke(Color.RED);
            hitboxVisualization.setTranslateX(offsetX);
            hitboxVisualization.setTranslateY(offsetY);
            owner.getChildren().add(hitboxVisualization);
        }
    }

    /**
     * Sets the size of the hitbox and resets the offsets to zero.
     *
     * @param width  The new width of the hitbox.
     * @param height The new height of the hitbox.
     */
    public void setHitboxSize(double width, double height) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
        this.offsetX = 0;
        this.offsetY = 0;
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            hitboxVisualization.setWidth(width);
            hitboxVisualization.setHeight(height);
        }
    }

    /**
     * Updates the position of the hitbox visualization to match the offsets.
     * Only applies if hitbox visualization is enabled.
     */
    public void updateHitBoxPosition() {
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            hitboxVisualization.setTranslateX(offsetX);
            hitboxVisualization.setTranslateY(offsetY);
        }
    }

    /**
     * Checks for a collision with another {@code CollisionComponent}.
     * Beneficial actors (e.g., {@link ActorLevelUp}, {@link HeartItem}) always collide,
     * while other collisions depend on the {@code collisionEnabled} flags.
     *
     * @param other The other {@code CollisionComponent} to check collision with.
     * @return {@code true} if the two components collide; {@code false} otherwise.
     */
    public boolean checkCollision(CollisionComponent other) {
        boolean thisIsBeneficial = (this.owner instanceof ActorLevelUp) || (this.owner instanceof HeartItem);
        boolean otherIsBeneficial = (other.owner instanceof ActorLevelUp) || (other.owner instanceof HeartItem);

        if (!thisIsBeneficial && !otherIsBeneficial && (!this.collisionEnabled || !other.collisionEnabled)) {
            return false;
        }
        return this.getHitboxX() < other.getHitboxX() + other.getHitboxWidth()
                && this.getHitboxX() + this.getHitboxWidth() > other.getHitboxX()
                && this.getHitboxY() < other.getHitboxY() + other.getHitboxHeight()
                && this.getHitboxY() + this.getHitboxHeight() > other.getHitboxY();
    }

    /**
     * Gets the X coordinate of the hitbox, including the owner's position and the X offset.
     *
     * @return The X coordinate of the hitbox.
     */
    @Override
    public double getHitboxX() {
        return owner.getLayoutX() + owner.getTranslateX() + offsetX;
    }

    /**
     * Gets the Y coordinate of the hitbox, including the owner's position and the Y offset.
     *
     * @return The Y coordinate of the hitbox.
     */
    @Override
    public double getHitboxY() {
        return owner.getLayoutY() + owner.getTranslateY() + offsetY;
    }

    /**
     * Gets the width of the hitbox.
     *
     * @return The width of the hitbox.
     */
    @Override
    public double getHitboxWidth() {
        return hitboxWidth;
    }

    /**
     * Gets the height of the hitbox.
     *
     * @return The height of the hitbox.
     */
    @Override
    public double getHitboxHeight() {
        return hitboxHeight;
    }

    /**
     * Enables or disables collision detection for this component.
     *
     * @param enabled {@code true} to enable collisions; {@code false} to disable them.
     */
    public void SetActorCollisionEnable(boolean enabled) {
        this.collisionEnabled = enabled;
    }
}
