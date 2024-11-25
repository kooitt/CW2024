// FighterPlane.java

package com.example.demo.actors;

import com.example.demo.components.CollisionComponent;
import com.example.demo.components.HealthComponent;
import com.example.demo.levels.LevelParent;

public abstract class FighterPlane extends ActiveActor {

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
		super(imageName, imageHeight, initialXPos, initialYPos);

		// 初始化 HealthComponent
		HealthComponent healthComponent = new HealthComponent(this, maxHealth);
		setHealthComponent(healthComponent);

		// 初始化 CollisionComponent，使用默认的碰撞盒大小
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		CollisionComponent collisionComponent = new CollisionComponent(this, hitboxWidth, hitboxHeight);
		setCollisionComponent(collisionComponent);
	}

	public void updateActor(double deltaTime, LevelParent level) {
		// 具体实现由子类完成
	}

	// 保持原有方法
	public double getProjectileXPosition(double offset) {
		return getLayoutX() + getTranslateX() + offset;
	}

	public double getProjectileYPosition(double offset) {
		return getLayoutY() + getTranslateY() + offset;
	}
}
