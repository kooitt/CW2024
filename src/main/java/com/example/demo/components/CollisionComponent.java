package com.example.demo.components;

import com.example.demo.actors.ActiveActor;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 处理碰撞检测和碰撞箱可视化。
 */
public class CollisionComponent implements Hitbox {
    private ActiveActor owner;
    private double hitboxWidth;
    private double hitboxHeight;
    private double offsetX;
    private double offsetY;
    private Rectangle hitboxVisualization;

    /**
     * 使用指定的尺寸和偏移量构造一个 CollisionComponent。
     *
     * @param owner        拥有该组件的 Actor。
     * @param hitboxWidth  碰撞箱的宽度。
     * @param hitboxHeight 碰撞箱的高度。
     * @param offsetX      碰撞箱相对于 Actor 的水平偏移量。
     * @param offsetY      碰撞箱相对于 Actor 的垂直偏移量。
     */
    public CollisionComponent(ActiveActor owner, double hitboxWidth, double hitboxHeight, double offsetX, double offsetY) {
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

    /**
     * 设置碰撞箱的尺寸。
     *
     * @param width  新的宽度。
     * @param height 新的高度。
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
     * 更新碰撞箱的位置。
     * 应该在每帧调用，确保碰撞箱与 Actor 的位置同步。
     */
    public void updateHitBoxPosition() {
        if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
            double visualX = offsetX;
            double visualY = offsetY;
            hitboxVisualization.setTranslateX(visualX);
            hitboxVisualization.setTranslateY(visualY);
        }
    }

    /**
     * 检查与另一个 CollisionComponent 的碰撞。
     *
     * @param other 另一个 CollisionComponent。
     * @return 如果发生碰撞，返回 true，否则返回 false。
     */
    public boolean checkCollision(CollisionComponent other) {
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
}
