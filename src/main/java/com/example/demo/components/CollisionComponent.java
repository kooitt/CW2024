package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.actors.Actor.ActorLevelUp;
import com.example.demo.actors.Actor.HeartItem;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CollisionComponent implements Hitbox {
    private Actor owner;
    private double hitboxWidth;
    private double hitboxHeight;
    private double offsetX;
    private double offsetY;
    private Rectangle hitboxVisualization;
    private boolean collisionEnabled = true; // 新增字段，默认为启用状态


    public CollisionComponent(Actor owner, double hitboxWidth, double hitboxHeight, double offsetX, double offsetY) {
        this.owner = owner;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        if (GameSettings.SHOW_HITBOXES) {
            hitboxVisualization = new Rectangle(hitboxWidth, hitboxHeight);
            hitboxVisualization.setStroke(Color.RED);
            hitboxVisualization.setFill(Color.TRANSPARENT);
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
            double visualX = offsetX;
            double visualY = offsetY;
            hitboxVisualization.setTranslateX(visualX);
            hitboxVisualization.setTranslateY(visualY);
        }
    }

    public boolean checkCollision(CollisionComponent other) {
        // 判断this和other是否为增益道具
        boolean thisIsBeneficial = (this.owner instanceof ActorLevelUp) || (this.owner instanceof HeartItem);
        boolean otherIsBeneficial = (other.owner instanceof ActorLevelUp) || (other.owner instanceof HeartItem);

        // 如果两者都不是增益道具，那么就按正常逻辑检查collisionEnabled
        if (!thisIsBeneficial && !otherIsBeneficial) {
            // 至少有一方collisionEnabled为false就不碰撞
            if (!this.collisionEnabled || !other.collisionEnabled) {
                return false;
            }
        }
        // 正常进行Hitbox范围碰撞检测
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

    public void setCollisionEnabled(boolean enabled) {
        this.collisionEnabled = enabled;
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

}
