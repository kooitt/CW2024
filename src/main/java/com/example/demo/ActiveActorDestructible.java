package com.example.demo;

import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible, ActorBehaviour {

	private boolean isDestroyed;
	private final Hitbox hitbox;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
		int imageWidth = (int) this.getImage().getWidth();
		hitbox = new Hitbox(initialXPos, initialYPos, imageWidth, imageHeight);
	}

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

	// get the hitbox
	public Rectangle getHitbox() {
		System.out.println("width:" + this.getImage().getWidth() + "height:" + this.getImage().getWidth());
		return hitbox.getHitbox();
	}

	// update the hitbox position to match the plane position
	public void updateHitboxPosition() {
		double newX = getLayoutX() + getTranslateX();
		double newY = getLayoutY() + getTranslateY();
		hitbox.updatePosition(newX, newY);
	}
}
