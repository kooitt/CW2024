package com.example.demo.actors.projectiles;

public class ProjectileFactory {

    public enum ProjectileType {
        USER,
        ENEMY,
        BOSS
    }

    public static Projectile createProjectile(ProjectileType type, double initialX, double initialY) {
        switch (type) {
            case USER:
                return new Projectile("userfire.png", 25, initialX, initialY, 15);
            case ENEMY:
                return new Projectile("enemyfire.png", 25, initialX, initialY, -10);
            case BOSS:
                return new Projectile("fireball.png", 75, 950, initialY, -15);
            default:
                throw new IllegalArgumentException("Unsupported projectile type" + type);
        }
    }
}
