// Projectile.java
package com.example.demo.actors.Projectile;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;

public abstract class Projectile extends Actor {

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos, 1);
		getMovementComponent().setVelocity(0, 0);
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		double offsetX = 0;
		double offsetY = 0;
		CollisionComponent collision = new CollisionComponent(this, hitboxWidth, hitboxHeight, offsetX, offsetY);
		setCollisionComponent(collision);
	}

	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0);
		setTranslateY(0);
		isDestroyed = false;
		setVisible(true);
	}

	public void reset() {
		setVisible(false);
		isDestroyed = true;
		getMovementComponent().setVelocity(0, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void takeDamage(int damage) {
		destroy();
	}

	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		getCollisionComponent().updateHitBoxPosition();
	}
}
