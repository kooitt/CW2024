package com.example.demo.actors.planes.bosses;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.planes.FighterPlane;
import com.example.demo.actors.shield.ShieldImage;
import com.example.demo.actors.shield.ShieldManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BossManager extends FighterPlane {

    private final List<Integer> movePattern;
    private boolean isShielded;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;
    //private int framesWithShieldActivated;
    private final ShieldImage shieldImage;
    private final ShieldManager shieldManager;

    public BossManager(String imageName, int imageHeight, double initialX, double initialY, int health) {
        super(imageName, imageHeight, initialX, initialY, health);
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        // shield stuff here
        isShielded = false;
        this.shieldImage = new ShieldImage(initialX, initialY);
        this.shieldManager = new ShieldManager(shieldImage, this);
        initializeMovePattern();
    }

    @Override
    public void updateActor() {
        updatePosition();
        shieldManager.updateShieldPosition();
        shieldManager.updateShield();
    }

    // positioning logic
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove()); //change vertical position
        double currentPosition = getLayoutY() + getTranslateY();

        if (currentPosition < getY_POSITION_UPPER_BOUND() || currentPosition > getY_POSITION_LOWER_BOUND()) {
            setTranslateY(initialTranslateY);
        }
        updateHitboxPosition();
    }

    private void initializeMovePattern() {
        for (int i = 0; i < get_MOVE_FREQUENCY_PER_CYCLE(); i++) {
            //movePattern list stores different vertical velocity values
            movePattern.add(get_VERTICAL_VELOCITY()); //downward movement
            movePattern.add(-get_VERTICAL_VELOCITY()); //upward movement
            movePattern.add(0); //stationary
        }
        Collections.shuffle(movePattern);
    }

    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove); //fetches current move from movePattern list
        consecutiveMovesInSameDirection++; //increments counter to track how long Boss moving in same direction
        if (consecutiveMovesInSameDirection == get_MAX_FRAMES_WITH_SAME_MOVE()) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
    }

    // Projectile Logic
    @Override
    public ActiveActorDestructible fireProjectile() {
        return bossFiresInCurrentFrame() ? createProjectile() : null;
    }

    // Shield Logic
    public ShieldImage getShieldImage(){
        return shieldImage;
    }

    @Override
    public void takeDamage() {
        //only takes damage if not shielded
        if (!shieldManager.isShielded) {
            super.takeDamage();
        }
    }

    @Override
    public double getImageHeight(){
        return getImage().getHeight();
    }

    public double getImageWidth(){
        return getImage().getWidth();
    }

    protected abstract int getY_POSITION_UPPER_BOUND();

    protected abstract int getY_POSITION_LOWER_BOUND();

    protected abstract int get_MOVE_FREQUENCY_PER_CYCLE();

    protected abstract int get_VERTICAL_VELOCITY();

    protected abstract int get_MAX_FRAMES_WITH_SAME_MOVE();

    protected abstract boolean bossFiresInCurrentFrame();

    protected abstract ActiveActorDestructible createProjectile();
}
