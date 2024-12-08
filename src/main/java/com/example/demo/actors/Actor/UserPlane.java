package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;
import com.example.demo.actors.Projectile.UserProjectile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class UserPlane extends Actor {

    private static final double Y_UPPER_BOUND = -40, Y_LOWER_BOUND = 600.0;
    private static final double X_LEFT_BOUND = 0.0, X_RIGHT_BOUND = 800.0;
    private static final double INITIAL_X_POSITION = 5.0, INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 100, VERTICAL_VELOCITY = 8, HORIZONTAL_VELOCITY = 8;
    private static final double PROJECTILE_X_OFFSET = 110, PROJECTILE_Y_OFFSET = 45;
    private static final double FIRE_RATE = 5.0;
    private static final int POWER_UP_THRESHOLD = 5;

    private int planeImageIndex = 0; // 初始为0，对应userplane.png
    private static final String[] PLANE_IMAGES = {"userplane.png", "userplane2.png", "userplane3.png", "userplane4.png"};
    private static final String[] BULLET_IMAGES = {"userfire.png", "userfire2.png", "userfire3.png", "userfire4.png"};

    private int verticalVelocityMultiplier = 0, horizontalVelocityMultiplier = 0;
    private int numberOfKills = 0, powerUpCount = 0, extraBulletRows = 0;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;

    // 移除静态 INITIAL_HEALTH，改为实例变量
    private int initialHealth;

    // 修改构造函数以接受初始健康值
    public UserPlane(int initialHealth) {
        super(PLANE_IMAGES[0], IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        this.initialHealth = initialHealth;
        // 重置飞机图片索引
        planeImageIndex = 0;
        UserProjectile.setCurrentImageName(BULLET_IMAGES[0]);

        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 0.8, IMAGE_HEIGHT);
        getMovementComponent().setVelocity(0, 0);
        shootingComponent = new ShootingComponent(this, FIRE_RATE, null, PROJECTILE_X_OFFSET, PROJECTILE_Y_OFFSET);
        shootingComponent.startFiring();

    }

    public void setAnimationComponent(AnimationComponent animationComponent) {
        this.animationComponent = animationComponent;
    }

    public void doubleFireRate() {
        shootingComponent.setFireRate(shootingComponent.getFireRate() + 1);
    }

    public void addExtraBulletRow() {
        shootingComponent.addBulletRow();
        extraBulletRows++;
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        updatePosition();
        getCollisionComponent().updateHitBoxPosition();
        if (shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getUserProjectilePool());
        }
        shootingComponent.update(deltaTime, level);
    }

    public void incrementPowerUpCount() {
        powerUpCount++;
        if (powerUpCount % POWER_UP_THRESHOLD == 0) {
            if (animationComponent != null) {
                double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth() * 1.5;
                double y = getCollisionComponent().getHitboxY() + getCollisionComponent().getHitboxHeight();
                animationComponent.playLevelUp(x, y, 5.0);
            }
            SoundComponent.playUpgradeSound();
            addExtraBulletRow();
            planeImageIndex = Math.min(planeImageIndex + 1, PLANE_IMAGES.length - 1);
            String newImage = PLANE_IMAGES[planeImageIndex];
            setImageViewImage(newImage);
            String newBulletImage = BULLET_IMAGES[planeImageIndex];
            UserProjectile.setCurrentImageName(newBulletImage);
        }
    }

    public void moveUp() {
        verticalVelocityMultiplier = -1;
        updateVelocity();
    }

    public void moveDown() {
        verticalVelocityMultiplier = 1;
        updateVelocity();
    }

    public void stopVerticalMovement() {
        verticalVelocityMultiplier = 0;
        updateVelocity();
    }

    public void moveLeft() {
        horizontalVelocityMultiplier = -1;
        updateVelocity();
    }

    public void moveRight() {
        horizontalVelocityMultiplier = 1;
        updateVelocity();
    }

    public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
        updateVelocity();
    }

    private void updateVelocity() {
        getMovementComponent().setVelocity(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier, VERTICAL_VELOCITY * verticalVelocityMultiplier);
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void incrementKillCount() {
        numberOfKills++;
    }

    public void stopShooting() {
        shootingComponent.stopFiring();
    }

    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX(), initialTranslateY = getTranslateY();
        super.updatePosition();
        double newX = getLayoutX() + getTranslateX(), newY = getLayoutY() + getTranslateY();
        if (newX < X_LEFT_BOUND || newX > X_RIGHT_BOUND) setTranslateX(initialTranslateX);
        if (newY < Y_UPPER_BOUND || newY > Y_LOWER_BOUND) setTranslateY(initialTranslateY);
    }
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getCurrentHealth() > 0) {
            SoundComponent.playBlinkSound();
            startBlinking();
            setActorCollisionEnabled(false, 2000);
        }
    }
    private void startBlinking() {
        // 我们需要闪烁4次, 每次闪烁由2个状态切换(可见->不可见->可见)
        // 每次闪烁周期为500ms，即250ms一次状态切换，两次切换一共500ms
        // 因此共需要8个状态切换（4次闪烁 * 2次切换每闪烁 = 8次）
        // 我们可以用Timeline创建8个KeyFrame，每250ms切换一次可见性

        Timeline blinkTimeline = new Timeline();

        // 总共要闪烁4次，每次2个状态变化，所以一共8个KeyFrame
        // 时间序列：0ms(变不可见), 250ms(变可见), 500ms(变不可见), 750ms(变可见), ... 持续8次
        int totalFlashes = 4;
        int switchesPerFlash = 2;
        int totalSwitches = totalFlashes * switchesPerFlash;
        int interval = 250; // 每次状态切换的间隔 250ms

        for (int i = 0; i < totalSwitches; i++) {
            KeyFrame frame = new KeyFrame(Duration.millis(i * interval), e -> {
                // 每次切换可见性
                setVisible(!isVisible());
            });
            blinkTimeline.getKeyFrames().add(frame);
        }

        // 动画结束后确保飞机最终处于可见状态
        blinkTimeline.setOnFinished(e -> setVisible(true));
        blinkTimeline.play();
    }

    public void setActorCollisionEnabled(boolean enabled, int durationMs) {
        getCollisionComponent().setCollisionEnabled(enabled);
        if (!enabled) {
            Timeline reEnableTimeline = new Timeline(
                    new KeyFrame(Duration.millis(durationMs), event -> {
                        getCollisionComponent().setCollisionEnabled(true);
                    })
            );
            reEnableTimeline.play();
        }
    }


}