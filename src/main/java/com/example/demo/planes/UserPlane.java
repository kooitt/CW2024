package com.example.demo.planes;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.projectiles.UserProjectile;
import com.example.demo.audio.AudioPlayer;

public class UserPlane extends FighterPlane {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = 20;
    private static final double Y_LOWER_BOUND = 600.0;
    private static final double X_LEFT_BOUND = 0.0;
    private static final double X_RIGHT_BOUND = 800.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 39;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HORIZONTAL_VELOCITY = 8;
    private static final int PROJECTILE_X_POSITION_OFFSET = 110;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
    private final AudioPlayer pewAudio;
    private final AudioPlayer explosionAudio;
    private final AudioPlayer takeDamageAudio;
    private int verticalVelocityMultiplier;
    private int horizontalVelocityMultiplier;
    private int numberOfKills;

    public UserPlane(int initialHealth) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        verticalVelocityMultiplier = 0;
        horizontalVelocityMultiplier = 0;

        pewAudio = new AudioPlayer();
        pewAudio.loadAudio("/com/example/demo/audio/pew.wav");

        explosionAudio = new AudioPlayer();
        explosionAudio.loadAudio("/com/example/demo/audio/explosion.wav");

        takeDamageAudio = new AudioPlayer();
        takeDamageAudio.loadAudio("/com/example/demo/audio/oof.wav");
    }

    @Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            double initialTranslateX = getTranslateX();

            this.moveVertically(VERTICAL_VELOCITY * verticalVelocityMultiplier);
            this.moveHorizontally(HORIZONTAL_VELOCITY * horizontalVelocityMultiplier);

            double newYPosition = getLayoutY() + getTranslateY();
            double newXPosition = getLayoutX() + getTranslateX();

            if (newYPosition < Y_UPPER_BOUND || newYPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY);
            }
            if (newXPosition < X_LEFT_BOUND || newXPosition > X_RIGHT_BOUND) {
                this.setTranslateX(initialTranslateX);
            }
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        pewAudio.play();
        return new UserProjectile(getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET),
                getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
    }

    @Override
    public void destroy() {
        super.destroy();
        explosionAudio.play();
    }

    @Override
    public void takeDamage() {
        takeDamageAudio.play();
        super.takeDamage();
    }

    private boolean isMoving() {
        return verticalVelocityMultiplier != 0 || horizontalVelocityMultiplier != 0;
    }

    public void moveUp() {
        verticalVelocityMultiplier = -1;
    }

    public void moveDown() {
        verticalVelocityMultiplier = 1;
    }

    public void moveLeft() {
        horizontalVelocityMultiplier = -1;
    }

    public void moveRight() {
        horizontalVelocityMultiplier = 1;
    }

    public void stopVerticalMovement() {
        verticalVelocityMultiplier = 0;
    }

    public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void incrementKillCount() {
        numberOfKills++;
    }
}
