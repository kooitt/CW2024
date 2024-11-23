package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.DestructionType;
import com.example.demo.actors.planes.FighterPlane;

import java.util.List;

public class CollisionManager {

    public void handlePlaneCollisions(List<ActiveActorDestructible> friendlyUnits,
                                      List<ActiveActorDestructible> enemyUnits) {
        for (ActiveActorDestructible friendly : friendlyUnits) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    friendly.takeDamage();
                    enemy.takeDamage();

                    // Check if either plane should be destroyed
                    if (friendly instanceof FighterPlane && ((FighterPlane) friendly).getHealth() <= 0) {
                        friendly.destroy(DestructionType.COLLISION);
                    }
                    if (enemy instanceof FighterPlane && ((FighterPlane) enemy).getHealth() <= 0) {
                        enemy.destroy(DestructionType.COLLISION);
                    }
                }
            }
        }
    }

    public void handleUserProjectileCollisions(List<ActiveActorDestructible> userProjectiles,
                                               List<ActiveActorDestructible> enemyUnits) {
        for (ActiveActorDestructible enemy : enemyUnits) {
            for (ActiveActorDestructible projectile : userProjectiles) {
                if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    enemy.takeDamage();  // Call takeDamage() instead of destroy()
                    projectile.destroy(DestructionType.COLLISION);

                    // If the enemy's health reaches 0, then destroy it
                    if (enemy instanceof FighterPlane && ((FighterPlane) enemy).getHealth() <= 0) {
                        enemy.destroy(DestructionType.PROJECTILE_KILL);
                    }
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