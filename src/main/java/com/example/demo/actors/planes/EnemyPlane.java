package com.example.demo.actors.planes;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.projectiles.ProjectileFactory;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 80;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -30.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = .01;

	public EnemyPlane(String IMAGE_NAME, int IMAGE_HEIGHT, double initialXPos, double initialYPos, int INITIAL_HEALTH) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitboxPosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition();
			return ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.ENEMY, projectileXPosition, projectileYPosition);
		}
		return null;
	}

	@Override
	public double getImageHeight() {
		return IMAGE_HEIGHT;
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

}
