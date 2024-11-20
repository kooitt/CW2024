// src/main/java/com/example/demo/factory/ProjectileFactory.java
package com.example.demo.factory;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.planes.Boss;
import com.example.demo.actors.planes.EnemyPlane;
import com.example.demo.actors.planes.EnemyPlaneTwo;
import com.example.demo.actors.projectiles.BossProjectile;
import com.example.demo.actors.projectiles.UserProjectile;
import com.example.demo.actors.projectiles.EnemyProjectile;

public class ProjectileFactory extends AbstractActorFactory {
    public enum ProjectileType {
        USER,
        ENEMY,
        BOSS
    }

    private final ProjectileType type;

    public ProjectileFactory(ProjectileType type) {
        super(switch (type){
            case USER -> 10;
            case ENEMY -> 50;
            case BOSS -> 75;
        }); // Different heights for user/enemy projectiles
        this.type = type;
    }

    @Override
    public ActiveActorDestructible createActor(double x, double y) {
        return switch(type) {
            case USER -> new UserProjectile(x, y);
            case ENEMY -> new EnemyProjectile(x, y);
            case BOSS -> new BossProjectile(y);  // Boss has fixed initial position
        };
    }
}