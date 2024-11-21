package com.example.demo.actors;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Abstract class representing an active actor that can be destroyed.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;
	private DestructionType destructionType;

	/**
	 * Constructs an ActiveActorDestructible with the specified parameters.
	 *
	 * @param imageName the name of the image representing the actor.
	 * @param imageHeight the height of the image.
	 * @param initialXPos the initial X position of the actor.
	 * @param initialYPos the initial Y position of the actor.
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	/**
	 * Updates the position of the actor.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the state of the actor.
	 */
	public abstract void updateActor();

	/**
	 * Inflicts damage to the actor.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Destroys the actor.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}

	public void destroy(DestructionType type) {
		this.destructionType = type;
		this.isDestroyed = true;
	}

	public DestructionType getDestructionType() {
		return destructionType;
	}

	/**
	 * Sets the destroyed state of the actor.
	 *
	 * @param isDestroyed true if the actor is destroyed, false otherwise.
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Checks if the actor is destroyed.
	 *
	 * @return true if the actor is destroyed, false otherwise.
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

	/**
	 * Returns the hitbox rectangle of the actor.
	 *
	 * @return the hitbox rectangle.
	 */
	public Rectangle getHitboxRectangle() {
		Bounds bounds = this.getBoundsInParent();
		Rectangle hitbox = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		hitbox.setStroke(Color.RED);
		hitbox.setFill(Color.TRANSPARENT);
		return hitbox;
	}
}