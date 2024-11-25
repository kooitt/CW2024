// BulletFactory.java

package com.example.demo.projectiles;

import com.example.demo.utils.ObjectFactory;

public class BulletFactory implements ObjectFactory<Projectile> {

    private String type;

    public BulletFactory(String type) {
        this.type = type;
    }

    @Override
    public Projectile create() {
        switch (type) {
            case "user":
                return new UserProjectile(0, 0);
            case "enemy":
                return new EnemyProjectile(0, 0);
            case "boss":
                return new BossProjectile(0);
            default:
                return null;
        }
    }

    @Override
    public void reset(Projectile projectile) {
        projectile.reset();
    }
}
