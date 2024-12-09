// BulletFactory.java
package com.example.demo.actors.Projectile;

import com.example.demo.interfaces.ObjectFactory;

public class BulletFactory implements ObjectFactory<Projectile> {

    private final String type;

    public BulletFactory(String type) {
        this.type = type;
    }

    @Override
    public Projectile create() {
        return switch (type) {
            case "user" -> new UserProjectile(0, 0);
            case "enemy" -> new EnemyProjectile(0, 0);
            case "boss" -> new BossProjectile(0);
            case "bossTwo" -> new BossTwoProjectile(0);
            default -> null;
        };
    }

    @Override
    public void reset(Projectile projectile) {
        projectile.reset();
    }
}
