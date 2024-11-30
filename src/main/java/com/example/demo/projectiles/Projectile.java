// Projectile.java
package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.utils.GameSettings;

/**
 * Abstract class representing a projectile in the game.
 */
public abstract class Projectile extends ActiveActor {

	/**
	 * Constructs a Projectile with specified image and position.
	 *
	 * @param imageName   image file name.
	 * @param imageHeight image height.
	 * @param initialXPos initial X position.
	 * @param initialYPos initial Y position.
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos, 1);
		getMovementComponent().setVelocity(0, 0);
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		CollisionComponent collision = new CollisionComponent(this, hitboxWidth, hitboxHeight);
		setCollisionComponent(collision);
	}

	/**
	 * Resets the projectile's position and state.
	 *
	 * @param x X position.
	 * @param y Y position.
	 */
	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0);
		setTranslateY(0);
		isDestroyed = false;
		setVisible(true);
	}

	/**
	 * Resets the projectile to inactive state.
	 */
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
