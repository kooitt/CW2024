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
    private static final int IMAGE_HEIGHT = 100, VELOCITY = 8;
    private static final double PROJECTILE_X_OFFSET = 110, PROJECTILE_Y_OFFSET = 45;
    private static final double FIRE_RATE = 5.0;
    private static final int POWER_UP_THRESHOLD = 3;

    private int planeImageIndex = 0;
    private static final String[] PLANE_IMAGES = {"userplane.png", "userplane2.png", "userplane3.png", "userplane4.png"};
    private static final String[] BULLET_IMAGES = {"userfire.png", "userfire2.png", "userfire3.png", "userfire4.png"};

    private int verticalVelocityMultiplier = 0, horizontalVelocityMultiplier = 0;
    private int numberOfKills = 0, powerUpCount = 0, extraBulletRows = 0;

    private ShootingComponent shootingComponent;
    private AnimationComponent animationComponent;
    private int initialHealth;

    public UserPlane(int initialHealth) {
        super(PLANE_IMAGES[0], IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        this.initialHealth = initialHealth;
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
        if (powerUpCount % POWER_UP_THRESHOLD == 0 && animationComponent != null) {
            SoundComponent.playUpgradeSound();
            addExtraBulletRow();
            planeImageIndex = Math.min(planeImageIndex + 1, PLANE_IMAGES.length - 1);
            setImageViewImage(PLANE_IMAGES[planeImageIndex]);
            UserProjectile.setCurrentImageName(BULLET_IMAGES[planeImageIndex]);

            animationComponent.playLevelUpRelative(this,
                    getCollisionComponent().getHitboxWidth() * 1.3,
                    getCollisionComponent().getHitboxHeight(),
                    5.0);
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
        getMovementComponent().setVelocity(VELOCITY * horizontalVelocityMultiplier, VELOCITY * verticalVelocityMultiplier);
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
        Timeline blinkTimeline = new Timeline();
        int totalFlashes = 4, interval = 250;
        for (int i = 0; i < totalFlashes * 2; i++) {
            blinkTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * interval), e -> setVisible(!isVisible())));
        }
        blinkTimeline.setOnFinished(e -> setVisible(true));
        blinkTimeline.play();
    }

    public void setActorCollisionEnabled(boolean enabled, int durationMs) {
        getCollisionComponent().setCollisionEnabled(enabled);
        if (!enabled) {
            new Timeline(new KeyFrame(Duration.millis(durationMs), event -> getCollisionComponent().setCollisionEnabled(true))).play();
        }
    }
}