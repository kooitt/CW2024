package com.example.demo.actors.planes;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.projectiles.ProjectileFiring;

public abstract class FighterPlane extends ActiveActorDestructible implements ProjectileFiring {

	private int health;

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}
	
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	protected double getProjectileYPosition() {
		return getLayoutY() + getTranslateY() + getImageHeight() / 4.0;
	}

	protected abstract double getImageHeight();

	private boolean healthAtZero() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}
		
}
