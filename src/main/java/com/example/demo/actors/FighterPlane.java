package com.example.demo.actors;

public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	public abstract ActiveActorDestructible fireProjectile();

	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	public void setHealth(int health) {
		this.health = Math.max(health, 0);
	}

	public double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	public double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	private boolean healthAtZero() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}
}
