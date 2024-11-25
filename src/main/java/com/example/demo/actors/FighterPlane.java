// FighterPlane.java

package com.example.demo.actors;

import com.example.demo.levels.LevelParent;

public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	// 修改 updateActor 方法签名
	public void updateActor(double deltaTime, LevelParent level) {
		// 具体实现由子类完成
	}

	@Override
	public void takeDamage() {
		health--;
		if (health <= 0) {
			this.destroy();
		}
	}

	public void setHealth(int health) {
		this.health = Math.max(health, 0);
	}

	public int getHealth() {
		return health;
	}

	public double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	public double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}
}
