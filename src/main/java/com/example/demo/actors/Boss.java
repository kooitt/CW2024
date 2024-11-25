package com.example.demo.actors;

import java.util.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.beans.binding.Bindings;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double FIRE_RATE = 1.0; // 每秒发射1次
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int MAX_HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;

	private final List<Integer> movePattern;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;

	private ProgressBar healthBar;
	private static final int HEALTH_BAR_WIDTH = 300;
	private static final int HEALTH_BAR_HEIGHT = 20;

	private ShootingComponent shootingComponent;

	public Boss(Group root) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		initializeMovePattern();

		// 设置碰撞盒大小
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT, IMAGE_HEIGHT * 0.2);

		Platform.runLater(() -> {
			initializeHealthBar(root);
		});

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);

		// 初始化 ShootingComponent
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, 0, PROJECTILE_Y_POSITION_OFFSET);

		// 开始射击
		shootingComponent.startFiring();
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

		if (getCurrentHealth() <= 0) {
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
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		getCollisionComponent().updateHitBoxPosition();

		if (shootingComponent != null && shootingComponent.getProjectilePool() == null) {
			shootingComponent.setProjectilePool(level.getBossProjectilePool());
		}

		// 更新射击逻辑
		shootingComponent.update(deltaTime, level);
	}


	@Override
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		updateHealthBar();
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

	private void updateHealthBar() {
		Platform.runLater(() -> {
			double progress = (double) getCurrentHealth() / MAX_HEALTH;
			if (progress < 0) {
				progress = 0;
			}
			healthBar.setProgress(progress);
			if (getCurrentHealth() <= 0) {
				healthBar.setVisible(false);
			} else {
				healthBar.setVisible(true);
			}
		});
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();

		// 更新 MovementComponent 的速度
		int nextMove = getNextMove();
		getMovementComponent().setVelocity(0, nextMove);

		super.updatePosition();

		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
			getMovementComponent().setVelocity(0, 0);
		}
	}


}
