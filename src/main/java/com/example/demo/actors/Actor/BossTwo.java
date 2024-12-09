package com.example.demo.actors.Actor;

import com.example.demo.actors.Projectile.Projectile;
import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class BossTwo extends Actor {

    // 原始代码中使用的是 "bosscxk.png"
    // 现在改为使用多帧图片 bosstwo1.png ~ bosstwo14.png 进行循环动画
    private static final String FRAME_PREFIX = "bosstwo";
    private static final int FRAME_COUNT = 14; // 帧数
    private static final int IMAGE_HEIGHT = 200;
    private static final int MAX_HEALTH = 100;
    private static final int HEALTH_BAR_WIDTH = 150;
    private static final int HEALTH_BAR_HEIGHT = 15;
    private double specialShotCooldown = 5.0;
    private double timeSinceLastSpecialShot = 0.0;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;
    private LevelParent level;
    private double screenHeight;
    private double movementTimer = 0;
    private double movementInterval = 1.0;
    private double verticalVelocity = 5;

    private ProgressBar healthBar;

    // 新增字段：动画相关
    private List<Image> frames;
    private int currentFrameIndex = 0;
    private Timeline animationTimeline;

    public BossTwo(Group root, LevelParent level) {
        // 先初始化：由于父类 Actor 构造中需要传入初始图片，这里先用 bosstwo1.png
        super(FRAME_PREFIX + "1.png", IMAGE_HEIGHT, 1000.0, 300.0, MAX_HEALTH);
        this.level = level;
        this.screenHeight = level.getScreenHeight();

        // 初始化碰撞箱和运动组件
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
        getMovementComponent().setVelocity(0, 0);

        // 初始化射击组件和动画组件
        shootingComponent = new ShootingComponent(this, 1.5, level.getBossTwoProjectilePool(), 0, 75.0);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();

        initializeHealthBar();
        loadAnimationFrames();
        startAnimation();
    }

    private void loadAnimationFrames() {
        frames = new ArrayList<>();
        for (int i = 1; i <= FRAME_COUNT; i++) {
            String frameName = FRAME_PREFIX + i + ".png";
            Image img = new Image(getClass().getResource(IMAGE_LOCATION + frameName).toExternalForm());
            frames.add(img);
        }
    }

    private void startAnimation() {
        // 每100毫秒切换下一帧，可根据需要调整帧率
        animationTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
            imageView.setImage(frames.get(currentFrameIndex));
        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    private void initializeHealthBar() {
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setStyle("-fx-accent: red; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 2;");
        this.getChildren().add(healthBar);
        healthBar.toFront();
        double imageWidth = getCollisionComponent().getHitboxWidth();
        healthBar.setLayoutX((imageWidth - HEALTH_BAR_WIDTH) / 2);
        healthBar.setLayoutY(-HEALTH_BAR_HEIGHT - 10);
        healthBar.setVisible(getCurrentHealth() > 0);
    }

    @Override
    public void updateActor(double deltaTime, LevelParent level) {
        movementTimer += deltaTime;
        if (movementTimer >= movementInterval) {
            movementTimer = 0;
            int direction = (Math.random() < 0.5) ? -1 : 1;
            getMovementComponent().setVelocity(0, verticalVelocity * direction);
        }

        updatePosition();
        getCollisionComponent().updateHitBoxPosition();

        double currentY = getLayoutY() + getTranslateY();
        if (currentY < 0) {
            setTranslateY(-getLayoutY());
            getMovementComponent().setVelocity(0, verticalVelocity);
        } else if (currentY + getCollisionComponent().getHitboxHeight() > screenHeight) {
            setTranslateY(screenHeight - getLayoutY() - getCollisionComponent().getHitboxHeight());
            getMovementComponent().setVelocity(0, -verticalVelocity);
        }

        if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
            shootingComponent.setProjectilePool(level.getBossTwoProjectilePool());
        }
        shootingComponent.update(deltaTime, level);

        timeSinceLastSpecialShot += deltaTime;
        if (timeSinceLastSpecialShot >= specialShotCooldown) {
            fireScatterShot(level);
            timeSinceLastSpecialShot = 0.0;
        }
        // 更新血条状态
        updateHealthBar();
    }

    private void fireScatterShot(LevelParent level) {
        if (level.getBossTwoProjectilePool() == null) return;

        // 定义散射角度（前方为负方向，稍微向上或向下）
        double[] angles = {-15, -7.5, 0, 7.5, 15};

        for (double angle : angles) {
            Projectile projectile = level.getBossTwoProjectilePool().acquire();
            if (projectile != null) {
                double rad = Math.toRadians(angle);
                double x = getProjectileXPosition(0);
                double y = getProjectileYPosition(75.0);
                projectile.resetPosition(x, y);

                // 根据角度设置子弹速度矢量，假设原本子弹是向左飞（-12,0），
                // 我们可以根据角度进行分解，让子弹呈扇形发射
                double baseSpeed = 12;
                double vx = Math.cos(rad) * (-baseSpeed);
                double vy = Math.sin(rad) * (-baseSpeed);
                projectile.getMovementComponent().setVelocity(vx, vy);

                level.getRoot().getChildren().add(projectile);
                level.addProjectile(projectile, this);
            }
        }
    }
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        updateHealthBar();
    }

    private void updateHealthBar() {
        Platform.runLater(() -> {
            double progress = (double) getCurrentHealth() / MAX_HEALTH;
            healthBar.setProgress(Math.max(progress, 0));
            healthBar.setVisible(getCurrentHealth() > 0);
        });
    }

    @Override
    public void destroy() {
        if (!isDestroyed) {
            super.destroy();
            double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth();
            double y = getCollisionComponent().getHitboxY();
            animationComponent.playExplosion(x, y, 2.5);
            if (animationTimeline != null) {
                animationTimeline.stop();
            }
        }
    }
}
