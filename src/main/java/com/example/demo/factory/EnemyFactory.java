// src/main/java/com/example/demo/factory/EnemyFactory.java
package com.example.demo.factory;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.planes.EnemyPlane;
import com.example.demo.actors.planes.EnemyPlaneTwo;
import com.example.demo.actors.planes.Boss;

public class EnemyFactory extends AbstractActorFactory {
    public enum EnemyType {
        ENEMYPLANEONE,      // Regular EnemyPlane
        ENEMYPLANETWO,   // EnemyPlaneTwo
        BOSS        // Boss
    }

    private final EnemyType type;

    public EnemyFactory(EnemyType type) {
        super(switch(type) {
            case ENEMYPLANEONE -> 80;     // EnemyPlane.IMAGE_HEIGHT
            case ENEMYPLANETWO -> 60;   // EnemyPlaneTwo.IMAGE_HEIGHT
            case BOSS -> 60;      // Boss.IMAGE_HEIGHT
        });
        this.type = type;
    }

    @Override
    public ActiveActorDestructible createActor(double x, double y) {
        return switch(type) {
            case ENEMYPLANEONE -> new EnemyPlane(x, y);
            case ENEMYPLANETWO -> new EnemyPlaneTwo(x, y);
            case BOSS -> new Boss();  // Boss has fixed initial position
        };
    }

    // Special method for creating boss since it doesn't need coordinates
    public Boss createBoss() {
        if (type != EnemyType.BOSS) {
            throw new IllegalStateException("This factory is not configured for boss creation");
        }
        return new Boss();
    }
}