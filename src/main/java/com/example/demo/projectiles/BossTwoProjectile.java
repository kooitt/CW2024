// BossTwoProjectile.java
package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;

/**
 * Represents a projectile fired by BossTwo.
 */
public class BossTwoProjectile extends Projectile {

    private static final String IMAGE_NAME = "basketball.png";
    private static final int IMAGE_HEIGHT = 80; // 根据图片大小调整
    private static final int HORIZONTAL_VELOCITY = -12; // 子弹速度，可以根据需要调整

    /**
     * Constructs a BossTwoProjectile with specified Y position.
     *
     * @param initialYPos initial Y position.
     */
    public BossTwoProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
    }

    @Override
    public void resetPosition(double x, double y) {
        super.resetPosition(x, y);
        getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
    }
}
