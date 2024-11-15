package com.example.demo.levels;

import com.example.demo.actors.ActiveActorDestructible;
import java.util.List;

/**
 * Handles collisions between different types of actors in the game.
 */
public class CollisionHandler {

    /**
     * Handles collisions between friendly units and enemy units.
     *
     * @param friendlyUnits the list of friendly units.
     * @param enemyUnits the list of enemy units.
     */
    public void handlePlaneCollisions(List<ActiveActorDestructible> friendlyUnits, List<ActiveActorDestructible> enemyUnits) {
        handleCollisions(friendlyUnits, enemyUnits);
    }

    /**
     * Handles collisions between user projectiles and enemy units.
     *
     * @param userProjectiles the list of user projectiles.
     * @param enemyUnits the list of enemy units.
     */
    public void handleUserProjectileCollisions(List<ActiveActorDestructible> userProjectiles, List<ActiveActorDestructible> enemyUnits) {
        handleCollisions(userProjectiles, enemyUnits);
    }

    /**
     * Handles collisions between enemy projectiles and friendly units.
     *
     * @param enemyProjectiles the list of enemy projectiles.
     * @param friendlyUnits the list of friendly units.
     */
    public void handleEnemyProjectileCollisions(List<ActiveActorDestructible> enemyProjectiles, List<ActiveActorDestructible> friendlyUnits) {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    /**
     * Handles collisions between two lists of actors.
     *
     * @param actors1 the first list of actors.
     * @param actors2 the second list of actors.
     */
    private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors2) {
            for (ActiveActorDestructible otherActor : actors1) {
                if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                    actor.takeDamage();
                    otherActor.takeDamage();
                }
            }
        }
    }
}