package com.example.demo.actors;

import com.example.demo.interfaces.Destructible;
import com.example.demo.physics.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible, Hitbox {

	private boolean isDestroyed;
	private Rectangle hitboxVisualization;
	private double hitboxWidth;
	private double hitboxHeight;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;

		this.hitboxWidth = imageView.getFitWidth();
		this.hitboxHeight = imageView.getFitHeight();

		if (GameSettings.SHOW_HITBOXES) {
			hitboxVisualization = new Rectangle(0, 0, hitboxWidth, hitboxHeight);
			hitboxVisualization.setStroke(Color.RED);
			hitboxVisualization.setFill(Color.TRANSPARENT);
			this.getChildren().add(hitboxVisualization);
		}
	}

	public void setDestroyed(boolean value) {
		this.isDestroyed = value;
	}

	public void setHitboxSize(double width, double height) {
		this.hitboxWidth = width;
		this.hitboxHeight = height;
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setWidth(width);
			hitboxVisualization.setHeight(height);
		}
	}

	@Override
	public abstract void updatePosition();

	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	@Override
	public void destroy() {
		setDestroyed(true);
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			this.getChildren().remove(hitboxVisualization);
		}
		setVisible(false);
	}

	protected void setDestroyed() {
		this.isDestroyed = true;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public double getHitboxX() {
		double offsetX = (imageView.getFitWidth() - hitboxWidth) / 2;
		return getLayoutX() + getTranslateX() + offsetX;
	}

	@Override
	public double getHitboxY() {
		double offsetY = (imageView.getFitHeight() - hitboxHeight) / 2;
		return getLayoutY() + getTranslateY() + offsetY;
	}

	@Override
	public double getHitboxWidth() {
		return hitboxWidth;
	}

	@Override
	public double getHitboxHeight() {
		return hitboxHeight;
	}

	public void updateHitBoxPosition() {
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			double offsetX = (imageView.getBoundsInLocal().getWidth() - hitboxWidth) / 2;
			double offsetY = (imageView.getBoundsInLocal().getHeight() - hitboxHeight) / 2;

			hitboxVisualization.setWidth(hitboxWidth);
			hitboxVisualization.setHeight(hitboxHeight);
			hitboxVisualization.setTranslateX(imageView.getTranslateX() + offsetX);
			hitboxVisualization.setTranslateY(imageView.getTranslateY() + offsetY);
		}
	}
}
