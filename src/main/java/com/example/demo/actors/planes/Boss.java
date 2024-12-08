package com.example.demo.actors.planes;

import com.example.demo.actors.core.ActiveActorDestructible;
import com.example.demo.actors.projectiles.BossProjectile;
import com.example.demo.actors.shield.ShieldImage;
import com.example.demo.actors.shield.ShieldManager;

import java.util.*;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .04;
	//private static final double BOSS_SHIELD_PROBABILITY = 0.02;
	private static final int IMAGE_HEIGHT = 100;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 1;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -30;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	//private static final int MAX_FRAMES_WITH_SHIELD = 500; // active for ~8.33 seconds
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	//private int framesWithShieldActivated;
	private final ShieldImage shieldImage;
	private final ShieldManager shieldManager;
	//private final ShieldFactory shieldFactory;

	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		//framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
		shieldImage = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		shieldManager = new ShieldManager(shieldImage, this);
	}

	//applies next movement to Boss's position
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove()); //change vertical position
		double currentPosition = getLayoutY() + getTranslateY();

		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
		shieldManager.updateShieldPosition();
		updateHitboxPosition();
	}
	
	@Override
	public void updateActor() {
		updatePosition();
		shieldManager.updateShield();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileYPosition()) : null;
	}
	
	@Override
	public void takeDamage() {
		//only takes damage if not shielded
		if (!shieldManager.isShielded) {
			super.takeDamage();
		}
	}

	//initializes move pattern for Boss, creates a list of vertical movements Boss can follow
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			//movePattern list stores different vertical velocity values
			movePattern.add(VERTICAL_VELOCITY); //downward movement
			movePattern.add(-VERTICAL_VELOCITY); //upward movement
			movePattern.add(ZERO); //stationary
		}
		Collections.shuffle(movePattern);
	}

	//determines next vertical movement for Boss based on movePattern
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove); //fetches current move from movePattern list
		consecutiveMovesInSameDirection++; //increments counter to track how long Boss moving in same direction
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

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	@Override
	public double getImageHeight(){
		return getImage().getHeight();
	}

	public double getImageWidth(){
		return getImage().getWidth();
	}

	public ShieldImage getShieldImage(){
		return shieldImage;
	}
}
