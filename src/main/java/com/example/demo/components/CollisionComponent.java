// CollisionComponent.java

package com.example.demo.components;

import com.example.demo.actors.ActiveActor;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CollisionComponent implements Hitbox {
    private ActiveActor owner;

    private double hitboxWidth;
    private double hitboxHeight;
    private Rectangle hitboxVisualization;

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

    public void setHitboxSize(double width, double height) {
        this.hitboxWidth = width;
        this.hitboxHeight = height;
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            hitboxVisualization.setWidth(width);
            hitboxVisualization.setHeight(height);
        }
    }

    public void updateHitBoxPosition() {
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            double offsetX = (owner.getImageView().getBoundsInLocal().getWidth() - hitboxWidth) / 2;
            double offsetY = (owner.getImageView().getBoundsInLocal().getHeight() - hitboxHeight) / 2;

            hitboxVisualization.setWidth(hitboxWidth);
            hitboxVisualization.setHeight(hitboxHeight);
            hitboxVisualization.setTranslateX(offsetX);
            hitboxVisualization.setTranslateY(offsetY);
        }
    }

    public boolean checkCollision(CollisionComponent other) {
        double thisX = this.getHitboxX();
        double thisY = this.getHitboxY();
        double otherX = other.getHitboxX();
        double otherY = other.getHitboxY();

        return thisX < otherX + other.getHitboxWidth() &&
                thisX + this.getHitboxWidth() > otherX &&
                thisY < otherY + other.getHitboxHeight() &&
                thisY + this.getHitboxHeight() > otherY;
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
