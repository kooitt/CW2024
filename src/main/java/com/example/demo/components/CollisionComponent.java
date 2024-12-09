package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CollisionComponent implements Hitbox {
    private final Actor owner;
    private double hitboxWidth;
    private double hitboxHeight;
    public double offsetX;
    public double offsetY;
    private Rectangle hitboxVisualization;
    public boolean collisionEnabled = true;

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

    public void updateHitBoxPosition() {
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            hitboxVisualization.setTranslateX(offsetX);
            hitboxVisualization.setTranslateY(offsetY);
        }
    }

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

    @Override
    public double getHitboxX() {
        return owner.getLayoutX() + owner.getTranslateX() + offsetX;
    }

    @Override
    public double getHitboxY() {
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

    public void SetActorCollisionEnable(boolean enabled) {
        this.collisionEnabled = enabled;
    }
}