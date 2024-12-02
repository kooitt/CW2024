package com.example.demo.components;

import com.example.demo.actors.ActiveActor;
import com.example.demo.levels.LevelParent;
import com.example.demo.projectiles.Projectile;
import com.example.demo.utils.ObjectPool;

public class ShootingComponent {
    private double fireRate;
    private double timeSinceLastShot;
    private ObjectPool<Projectile> projectilePool;
    private ActiveActor owner;
    private double projectileXOffset;
    private double projectileYOffset;
    private boolean isFiring;

    private int bulletRows; // 当前发射的子弹排数
    private double rowSpacing; // 每排子弹之间的垂直间距

    public ShootingComponent(ActiveActor owner, double fireRate, ObjectPool<Projectile> projectilePool, double projectileXOffset, double projectileYOffset) {
        this.owner = owner;
        this.fireRate = fireRate;
        this.projectilePool = projectilePool;
        this.projectileXOffset = projectileXOffset;
        this.projectileYOffset = projectileYOffset;
        this.timeSinceLastShot = 0;
        this.isFiring = false;
        this.bulletRows = 1; // 默认一排
        this.rowSpacing = 30; // 每排之间的间距，可以根据需要调整
    }

    public void update(double deltaTime, LevelParent level) {
        if (isFiring) {
            timeSinceLastShot += deltaTime;
            if (timeSinceLastShot >= (1.0 / fireRate)) {
                fire(level);
                timeSinceLastShot = 0;
            }
        }
    }

    private void fire(LevelParent level) {
        if (projectilePool == null) return;
        for (int i = 0; i < bulletRows; i++) {
            Projectile projectile = projectilePool.acquire();
            if (projectile != null) {
                double x = owner.getProjectileXPosition(projectileXOffset);
                double y = owner.getProjectileYPosition(projectileYOffset) + (i - bulletRows / 2.0) * rowSpacing;
                projectile.resetPosition(x, y);
                level.getRoot().getChildren().add(projectile);
                level.addProjectile(projectile, owner);
            }
        }
    }

    public void startFiring() {
        isFiring = true;
    }

    public void stopFiring() {
        isFiring = false;
        timeSinceLastShot = 0;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getFireRate() {
        return fireRate;
    }

    public ObjectPool<Projectile> getProjectilePool() {
        return projectilePool;
    }

    public void setProjectilePool(ObjectPool<Projectile> projectilePool) {
        this.projectilePool = projectilePool;
    }

    public void addBulletRow() {
        bulletRows++;
        System.out.println("Added a bullet row! Total rows: " + bulletRows);
    }

    public void setBulletRows(int rows) {
        if (rows > 0) {
            bulletRows = rows;
            System.out.println("Bullet rows set to: " + bulletRows);
        }
    }

    public int getBulletRows() {
        return bulletRows;
    }

    public void setRowSpacing(double spacing) {
        if (spacing >= 0) {
            rowSpacing = spacing;
        }
    }

    public double getRowSpacing() {
        return rowSpacing;
    }
}
