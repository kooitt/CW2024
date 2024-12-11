package com.example.demo;

import java.util.List;


public class CollisionHandler {
    private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final UserPlane user;
	private final double screenWidth;

	public CollisionHandler(List<ActiveActorDestructible> friendlyUnits, 
	List<ActiveActorDestructible> enemyUnits, 
	List<ActiveActorDestructible> userProjectiles,
	List<ActiveActorDestructible> enemyProjectiles, 
	UserPlane user, double screenWidth){

		this.friendlyUnits = friendlyUnits;
		this.enemyUnits = enemyUnits;
		this.userProjectiles = userProjectiles;
		this.enemyProjectiles = enemyProjectiles;
		this.user = user;
		this.screenWidth = screenWidth;
	}


	public void handleCollisions() {
		handlePlaneCollisions();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handleEnemyPenetration();
	}


	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1,
			List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}



}
