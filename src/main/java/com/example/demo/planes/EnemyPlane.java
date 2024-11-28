package com.example.demo.planes;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.projectiles.EnemyProjectile;
import com.example.demo.audio.AudioPlayer;

public class EnemyPlane extends FighterPlane {

    private static final String IMAGE_NAME = "enemyplane.png";
    private static final int IMAGE_HEIGHT = 54;
    private static final int HORIZONTAL_VELOCITY = -6;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
    private static final int INITIAL_HEALTH = 1;
    private static final double FIRE_RATE = .01;

    private final AudioPlayer explosionAudio;
    private final AudioPlayer missileAudio;

    public EnemyPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);

        explosionAudio = new AudioPlayer();
        explosionAudio.loadAudio("/com/example/demo/audio/explosion.wav");

        missileAudio = new AudioPlayer();
        missileAudio.loadAudio("/com/example/demo/audio/missile.wav");
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);

            missileAudio.play();

            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public void destroy() {
        super.destroy();
        explosionAudio.play();
    }
}
