package com.example.demo.handlers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.DestructionType;

import java.util.List;

public class CollisionHandler {

    public void handlePlaneCollisions(List<ActiveActorDestructible> friendlyUnits,
                                      List<ActiveActorDestructible> enemyUnits) {
        for (ActiveActorDestructible friendly : friendlyUnits) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    friendly.takeDamage();
                    enemy.destroy(DestructionType.COLLISION);
                }
            }
        }
    }

    public void handleUserProjectileCollisions(List<ActiveActorDestructible> userProjectiles,
                                               List<ActiveActorDestructible> enemyUnits) {
        for (ActiveActorDestructible enemy : enemyUnits) {
            for (ActiveActorDestructible projectile : userProjectiles) {
                if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    enemy.destroy(DestructionType.PROJECTILE_KILL);
                    projectile.destroy(DestructionType.COLLISION);
                }
            }
        }
    }

    public void handleEnemyProjectileCollisions(List<ActiveActorDestructible> enemyProjectiles,
                                                List<ActiveActorDestructible> friendlyUnits) {
        for (ActiveActorDestructible friendly : friendlyUnits) {
            for (ActiveActorDestructible projectile : enemyProjectiles) {
                if (friendly.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    friendly.takeDamage();
                    projectile.destroy(DestructionType.COLLISION);
                }
            }
        }
    }
}