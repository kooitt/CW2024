package com.example.demo.actors;

import java.util.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.beans.binding.Bindings;
import com.example.demo.projectiles.BossProjectile;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = 0.04;
	private static final double BOSS_SHIELD_PROBABILITY = 0.02;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 100;

	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;

	private ProgressBar healthBar;
	private static final int HEALTH_BAR_WIDTH = 300;
	private static final int HEALTH_BAR_HEIGHT = 20;

	public Boss(Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();

		Platform.runLater(() -> {
			initializeHealthBar(root);
		});
	}

	private void initializeHealthBar(Group root) {
		healthBar = new ProgressBar(1.0);
		healthBar.setPrefWidth(HEALTH_BAR_WIDTH);
		healthBar.setPrefHeight(HEALTH_BAR_HEIGHT);
		healthBar.setStyle("-fx-accent: red;");

		root.getChildren().add(healthBar);
		healthBar.toFront();

		Scene currentScene = root.getScene();
		if (currentScene != null) {
			bindHealthBarToScene(currentScene);
		} else {
			root.sceneProperty().addListener((observable, oldScene, newScene) -> {
				if (newScene != null) {
					bindHealthBarToScene(newScene);
				}
			});
		}

		if (getHealth() <= 0) {
			healthBar.setVisible(false);
		} else {
			healthBar.setVisible(true);
		}
	}

	private void bindHealthBarToScene(Scene scene) {
		healthBar.layoutXProperty().bind(
				Bindings.createDoubleBinding(() ->
								(scene.getWidth() - healthBar.getPrefWidth()) / 2,
						scene.widthProperty()
				)
		);

		healthBar.layoutYProperty().bind(
				Bindings.createDoubleBinding(() ->
								scene.getHeight() - healthBar.getPrefHeight() - 10,
						scene.heightProperty()
				)
		);
	}

	@Override
	public void updatePosition() {
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
		updateShield();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			if (getHealth() < 0) {
				setHealth(0);
			}
			updateHealthBar();
		}
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			if (shieldExhausted()) {
				deactivateShield();
			}
		} else if (shieldShouldBeActivated()) {
			activateShield();
		}
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

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}

	private void updateHealthBar() {
		Platform.runLater(() -> {
			double progress = (double) getHealth() / HEALTH;
			if (progress < 0) {
				progress = 0;
			}
			healthBar.setProgress(progress);
			if (getHealth() <= 0) {
				healthBar.setVisible(false);
			} else {
				healthBar.setVisible(true);
			}
		});
	}
}
