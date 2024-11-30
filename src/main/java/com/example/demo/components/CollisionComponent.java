// CollisionComponent.java
package com.example.demo.components;

import com.example.demo.actors.ActiveActor;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles collision detection and hitbox visualization.
 */
public class CollisionComponent implements Hitbox {
    private ActiveActor owner;
    private double hitboxWidth;
    private double hitboxHeight;
    private Rectangle hitboxVisualization;

    /**
     * Constructs a CollisionComponent with specified size.
     *
     * @param owner        the owning actor.
     * @param hitboxWidth  width of the hitbox.
     * @param hitboxHeight height of the hitbox.
     */
    public CollisionComponent(ActiveActor owner, double hitboxWidth, double hitboxHeight) {
        this.owner = owner;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;

        if (GameSettings.SHOW_HITBOXES) {
            hitboxVisualization = new Rectangle(0, 0, hitboxWidth, hitboxHeight);
            hitboxVisualization.setStroke(Color.RED);
            hitboxVisualization.setFill(Color.TRANSPARENT);
            owner.getChildren().add(hitboxVisualization);
        }
    }

    /**
     * Sets the size of the hitbox.
     *
     * @param width  new width.
     * @param height new height.
     */
    public void setHitboxSize(double width, double height) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            hitboxVisualization.setWidth(width);
            hitboxVisualization.setHeight(height);
        }
    }

    /**
     * Updates the position of the hitbox visualization.
     */
    public void updateHitBoxPosition() {
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            double offsetX = (owner.getImageView().getBoundsInLocal().getWidth() - hitboxWidth) / 2;
            double offsetY = (owner.getImageView().getBoundsInLocal().getHeight() - hitboxHeight) / 2;
            hitboxVisualization.setTranslateX(offsetX);
            hitboxVisualization.setTranslateY(offsetY);
        }
    }

    /**
     * Checks collision with another CollisionComponent.
     *
     * @param other the other CollisionComponent.
     * @return true if colliding, false otherwise.
     */
    public boolean checkCollision(CollisionComponent other) {
        return this.getHitboxX() < other.getHitboxX() + other.getHitboxWidth()
                && this.getHitboxX() + this.getHitboxWidth() > other.getHitboxX()
                && this.getHitboxY() < other.getHitboxY() + other.getHitboxHeight()
                && this.getHitboxY() + this.getHitboxHeight() > other.getHitboxY();
    }

    @Override
    public double getHitboxX() {
        double offsetX = (owner.getImageView().getFitWidth() - hitboxWidth) / 2;
        return owner.getLayoutX() + owner.getTranslateX() + offsetX;
    }

    @Override
    public double getHitboxY() {
        double offsetY = (owner.getImageView().getFitHeight() - hitboxHeight) / 2;
        return owner.getLayoutY() + owner.getTranslateY() + offsetY;
    }

    @Override
    public double getHitboxWidth() {
        return hitboxWidth;
    }

    @Override
    public double getHitboxHeight() {
        return hitboxHeight;
    }
}
