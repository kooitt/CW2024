package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;
	private final Rectangle hitbox;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;

		int imageWidth = (int) this.getImage().getWidth();

		// initialize hitbox with same size as the plane's image
		hitbox = new Rectangle(initialXPos, initialYPos, imageWidth, imageHeight); // imageWidth should be declared
		hitbox.setStroke(Color.RED);  // visualize the hitbox
		hitbox.setFill(Color.TRANSPARENT);  // transparent inside
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

	// get the hitbox
	public Rectangle getHitbox() {
		System.out.println("width:" + this.getImage().getWidth() + "height:" + this.getImage().getWidth());
		return hitbox;
	}

	// update the hitbox position to match the plane position
	public void updateHitboxPosition() {
		hitbox.setX(getLayoutX() + getTranslateX());
		hitbox.setY(getLayoutY() + getTranslateY());
	}

}
