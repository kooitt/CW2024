package com.example.demo.actors.Projectile;

public class BossProjectile extends Projectile {

 private static final String IMAGE_NAME = "fireball.png";
 private static final int IMAGE_HEIGHT = 50;
 private static final int HORIZONTAL_VELOCITY = -15;

 public BossProjectile(double initialYPos) {
  super(IMAGE_NAME, IMAGE_HEIGHT, 950, initialYPos);
  getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 2.5, IMAGE_HEIGHT);
 }

 @Override
 public void resetPosition(double x, double y) {
  super.resetPosition(x, y);
  getMovementComponent().setVelocity(HORIZONTAL_VELOCITY, 0);
 }
}