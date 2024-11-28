package com.example.demo.planes;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.projectiles.EnemyProjectile;
import com.example.demo.audio.AudioPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdvancedEnemyPlane extends FighterPlane {

    private static final String IMAGE_NAME = "advancedenemyplane.png";
    private static final int IMAGE_HEIGHT = 54;
    private static final int HEALTH = 1;
    private static final int HORIZONTAL_SPEED = -8;
    private static final int VERTICAL_VELOCITY = 5;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int Y_POSITION_UPPER_BOUND = 20;
    private static final int Y_POSITION_LOWER_BOUND = 475;

    private final List<Integer> movePattern;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;

    private final AudioPlayer explosionAudio;
    private final AudioPlayer missileAudio;

    public AdvancedEnemyPlane(double screenWidth, double initialYPosition) {
        super(IMAGE_NAME, IMAGE_HEIGHT, screenWidth, initialYPosition, HEALTH);

        movePattern = new ArrayList<>();
        initializeMovePattern();

        explosionAudio = new AudioPlayer();
        explosionAudio.loadAudio("/com/example/demo/audio/explosion.wav");

        missileAudio = new AudioPlayer();
        missileAudio.loadAudio("/com/example/demo/audio/missile.wav");
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_SPEED);

        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove());
        double currentPosition = getLayoutY() + getTranslateY();

        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY);
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < 0.01) {
            double projectileXPosition = getProjectileXPosition(-100.0);
            double projectileYPosition = getProjectileYPosition(50.0);

            missileAudio.play();

            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        explosionAudio.play();
    }

    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(ZERO);
        }
        Collections.shuffle(movePattern);
    }

    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
    }
}
