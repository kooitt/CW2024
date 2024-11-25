package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

public abstract class Projectile extends ActiveActorDestructible {

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0);
		setTranslateY(0);
		setDestroyed(false);
		setVisible(true);
	}

	public void reset() {
		setVisible(false);
		setDestroyed(true);
	}

	@Override
	public void takeDamage() {
		this.destroy();
	}

	@Override
	public abstract void updatePosition();

	@Override
	public void updateActor() {
		updatePosition();
	}
}
