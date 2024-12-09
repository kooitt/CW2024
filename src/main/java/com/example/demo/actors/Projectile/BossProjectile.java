package com.example.demo.actors.Projectile;

/**
 * Represents a projectile used by the boss in the game, typically a fireball.
 * This class extends the generic {@link Projectile} class to include specific
 * properties and behaviors for a boss projectile.
 */
public class BossProjectile extends Projectile {

 /**
  * The name of the image file used to represent the projectile visually.
  */
 private static final String IMAGE_NAME = "fireball.png";

 /**
  * The height of the projectile image, used for display and collision purposes.
  */
 private static final int IMAGE_HEIGHT = 50;

 /**
  * The constant horizontal velocity of the projectile, moving from right to left.
  */
 private static final int HORIZONTAL_VELOCITY = -15;

 /**
  * Constructs a {@code BossProjectile} with an initial vertical position.
  * The projectile starts at a fixed horizontal position of 950 and moves
  * horizontally at a constant velocity.
  *
  * @param initialYPos the initial vertical position of the projectile
  */
 public BossProjectile(double initialYPos) {
  super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);
  getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2.5, IMAGE_HEIGHT);
 }

 /**
  * Resets the position of the projectile to a new specified location.
  * After resetting the position, the projectile's velocity is set to a constant
  * horizontal velocity to simulate continuous leftward movement.
  *
  * @param x the new horizontal position of the projectile
  * @param y the new vertical position of the projectile
  */
 @Override
 public void resetPosition(double x, double y) {
  super.resetPosition(x, y);
  getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
 }
}
