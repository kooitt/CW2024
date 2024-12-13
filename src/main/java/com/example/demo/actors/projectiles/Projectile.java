package com.example.demo.actors.projectiles;

import com.example.demo.actors.core.ActiveActorDestructible;

public class Projectile extends ActiveActorDestructible {

	private final int horizontalVelocity;

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos, int horizontalVelocity) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.horizontalVelocity = horizontalVelocity;
	}

	@Override
	public void takeDamage() {
		this.destroy();
	}

	@Override
	public void updatePosition() {
		moveHorizontally(horizontalVelocity);
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

}
