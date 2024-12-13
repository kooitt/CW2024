package com.example.demo.actors.planes.bosses;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.projectiles.ProjectileFactory;

public class MonstrousNightmare extends BossManager{
    private static final String IMAGE_NAME = "monstrousnightmare.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double BOSS_FIRE_RATE = .04;
    //private static final double BOSS_SHIELD_PROBABILITY = 0.02;
    private static final int IMAGE_HEIGHT = 200;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HEALTH = 1;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int Y_POSITION_UPPER_BOUND = -30;
    private static final int Y_POSITION_LOWER_BOUND = 475;
    //private static final int MAX_FRAMES_WITH_SHIELD = 500; // active for ~8.33 seconds
    //private final List<Integer> movePattern;
    private boolean isShielded;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;
    //private int framesWithShieldActivated;
    //private final ShieldImage shieldImage;
    //private final ShieldManager shieldManager;
    //private final ShieldFactory shieldFactory;

    public MonstrousNightmare(String IMAGE_NAME, int IMAGE_HEIGHT, double INITIAL_X_POSITION, double INITIAL_Y_POSITION, int HEALTH) {
        super(
                IMAGE_NAME,
                IMAGE_HEIGHT,
                INITIAL_X_POSITION,
                INITIAL_Y_POSITION,
                HEALTH
        );
    }

    protected ActiveActorDestructible createProjectile(){
        double projectileX = getLayoutX() + getTranslateX() + (getFitWidth() / 2.0);
        double projectileY = getLayoutY() + getTranslateY() + (getFitHeight() / 2.0);
        return ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.MONSTROUSNIGHTMARE , projectileX, projectileY);
    };

    protected boolean bossFiresInCurrentFrame() {
        return Math.random() < BOSS_FIRE_RATE;
    }

    protected int getY_POSITION_UPPER_BOUND(){
        return Y_POSITION_UPPER_BOUND;
    };
    protected int getY_POSITION_LOWER_BOUND(){
        return Y_POSITION_LOWER_BOUND;
    };
    protected int get_MOVE_FREQUENCY_PER_CYCLE(){
        return MOVE_FREQUENCY_PER_CYCLE;
    };
    protected int get_VERTICAL_VELOCITY(){
        return VERTICAL_VELOCITY;
    };
    protected int get_MAX_FRAMES_WITH_SAME_MOVE(){
        return MAX_FRAMES_WITH_SAME_MOVE;
    };


}
