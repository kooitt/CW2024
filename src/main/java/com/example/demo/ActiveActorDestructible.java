package com.example.demo;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	@Override
	public abstract void updatePosition();

	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	@Override
	public void destroy() {
		setDestroyed(true);
	}

	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public Rectangle getHitboxRectangle() {
		Bounds bounds = this.getBoundsInParent();
		Rectangle hitbox = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		hitbox.setStroke(Color.RED);
		hitbox.setFill(Color.TRANSPARENT);
		return hitbox;
	}
}
