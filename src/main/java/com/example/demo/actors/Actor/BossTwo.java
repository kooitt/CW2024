package com.example.demo.actors.Actor;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;

public class BossTwo extends Actor {

    private static final String IMAGE_NAME = "bosscxk.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double FIRE_RATE = 1.5;
    private static final int IMAGE_HEIGHT = 200;
    private static final int MAX_HEALTH = 100;
    private static final int HEALTH_BAR_WIDTH = 150;
    private static final int HEALTH_BAR_HEIGHT = 15;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;
    private LevelParent level;
    private double screenHeight;
    private double movementTimer = 0;
    private double movementInterval = 1.0;
    private double verticalVelocity = 5;

    private ProgressBar healthBar;

    public BossTwo(Group root, LevelParent level) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
        this.level = level;
        this.screenHeight = level.getScreenHeight();
        getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT);
        getCollisionComponent().updateHitBoxPosition();
        shootingComponent = new ShootingComponent(this, FIRE_RATE, level.getBossTwoProjectilePool(), 0, PROJECTILE_Y_POSITION_OFFSET);
        animationComponent = new AnimationComponent(root);
        shootingComponent.startFiring();
        getMovementComponent().setVelocity(0, 0);

        initializeHealthBar(); // 初始化血条
    }

    private void initializeHealthBar() {
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setStyle("-fx-accent: red; -fx-background-color: black; -fx-border-color: white; -fx-border-width: 2;");
        this.getChildren().add(healthBar);  // 添加到 BossTwo 本身
        healthBar.toFront();
        double imageWidth = getCollisionComponent().getHitboxWidth();
        healthBar.setLayoutX((imageWidth - HEALTH_BAR_WIDTH) / 2);
        healthBar.setLayoutY(-HEALTH_BAR_HEIGHT - 10); // 上面增加10单位的间距
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
        performSpecialAttack(deltaTime);
        updateHealthBar(); // 更新血条
    }

    private void performSpecialAttack(double deltaTime) {
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        updateHealthBar(); // 伤害后更新血条
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
        }
    }
}
