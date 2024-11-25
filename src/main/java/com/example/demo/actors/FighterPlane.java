// FighterPlane.java

package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

public abstract class FighterPlane extends ActiveActorDestructible {

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
		super(imageName, imageHeight, initialXPos, initialYPos, maxHealth);
	}

	public void updateActor(double deltaTime, LevelParent level) {
		// 具体实现由子类完成
	}

	// 添加以下方法
	public double getProjectileXPosition(double offset) {
		return getLayoutX() + getTranslateX() + offset;
	}

	public double getProjectileYPosition(double offset) {
		return getLayoutY() + getTranslateY() + offset;
	}
}
