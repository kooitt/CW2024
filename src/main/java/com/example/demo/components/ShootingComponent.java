package com.example.demo.components;

import com.example.demo.actors.ActiveActor;
import com.example.demo.levels.LevelParent;
import com.example.demo.projectiles.Projectile;
import com.example.demo.utils.ObjectPool;
import javafx.scene.Group;

/**
 * Handles shooting logic for actors, supporting multiple bullet rows.
 */
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

    /**
     * Constructs a ShootingComponent.
     *
     * @param owner             the owning actor.
     * @param fireRate          shots per second.
     * @param projectilePool    pool of projectiles.
     * @param projectileXOffset X offset for projectile spawning.
     * @param projectileYOffset Y offset for projectile spawning.
     */
    public ShootingComponent(ActiveActor owner, double fireRate, ObjectPool<Projectile> projectilePool, double projectileXOffset, double projectileYOffset) {
        this.owner = owner;
        this.fireRate = fireRate;
        this.projectilePool = projectilePool;
        this.projectileXOffset = projectileXOffset;
        this.projectileYOffset = projectileYOffset;
        this.timeSinceLastShot = 0;
        this.isFiring = false;
        this.bulletRows = 1; // 默认一排
        this.rowSpacing = 20; // 每排之间的间距，可以根据需要调整
    }

    /**
     * Updates shooting based on deltaTime.
     *
     * @param deltaTime time since last update.
     * @param level     current game level.
     */
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

    /**
     * 增加一排子弹
     */
    public void addBulletRow() {
        bulletRows++;
        System.out.println("Added a bullet row! Total rows: " + bulletRows);
    }

    /**
     * 设置子弹排数
     *
     * @param rows 要设置的子弹排数
     */
    public void setBulletRows(int rows) {
        if (rows > 0) {
            bulletRows = rows;
            System.out.println("Bullet rows set to: " + bulletRows);
        }
    }

    /**
     * 获取当前子弹排数
     *
     * @return 子弹排数
     */
    public int getBulletRows() {
        return bulletRows;
    }

    /**
     * 设置每排子弹之间的间距
     *
     * @param spacing 间距值
     */
    public void setRowSpacing(double spacing) {
        if (spacing >= 0) {
            rowSpacing = spacing;
        }
    }

    /**
     * 获取当前每排子弹之间的间距
     *
     * @return 间距值
     */
    public double getRowSpacing() {
        return rowSpacing;
    }
}
