package com.example.demo.actors;

import com.example.demo.components.HealthComponent;
import com.example.demo.interfaces.Destructible;
import com.example.demo.levels.LevelParent;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.utils.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible, Hitbox {

	private boolean isDestroyed;
	protected Rectangle hitboxVisualization;
	private double hitboxWidth;
	private double hitboxHeight;

	protected HealthComponent healthComponent;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;

		// 初始化 HealthComponent
		healthComponent = new HealthComponent(this, maxHealth);

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
	public void updateActor() {
		super.updateActor(); // 确保位置更新
	}

	// 添加抽象方法
	public abstract void updateActor(double deltaTime, LevelParent level);

	@Override
	public void destroy() {
		setDestroyed(true);
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setVisible(false);
		}
		setVisible(false);
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void takeDamage(int damage) {
		healthComponent.takeDamage(damage);
	}

	public int getCurrentHealth() {
		return healthComponent.getCurrentHealth();
	}

	public int getMaxHealth() {
		return healthComponent.getMaxHealth();
	}

	public void setCurrentHealth(int health) {
		healthComponent.setCurrentHealth(health);
	}

	public void setMaxHealth(int maxHealth) {
		healthComponent.setMaxHealth(maxHealth);
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
			hitboxVisualization.setTranslateX(offsetX);
			hitboxVisualization.setTranslateY(offsetY);
		}
	}
}
