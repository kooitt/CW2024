package com.example.demo.actors;

import com.example.demo.components.AnimationComponent;
import com.example.demo.components.ShootingComponent;
import com.example.demo.levels.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Boss extends ActiveActor {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double FIRE_RATE = 1.0; // 每秒发射1次
	private static final int IMAGE_HEIGHT = 100;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int MAX_HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = 0;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final double SHIELD_CHECK_INTERVAL = 10.0; // 每10秒检查一次

	private final List<Integer> movePattern;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;

	private ProgressBar healthBar;
	private static final int HEALTH_BAR_WIDTH = 300;
	private static final int HEALTH_BAR_HEIGHT = 20;

	private ShootingComponent shootingComponent;
	private AnimationComponent animationComponent;

	private Shield shield;
	private Timeline shieldCheckTimeline;
	private LevelParent level; // 添加 LevelParent 的引用

	private boolean isSummoningShield = false; // 标志位

	public Boss(Group root, LevelParent level) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, MAX_HEALTH);
		this.level = level; // 赋值 LevelParent
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		initializeMovePattern();

		// 设置碰撞盒大小
		getCollisionComponent().setHitboxSize(IMAGE_HEIGHT * 3, IMAGE_HEIGHT);

		Platform.runLater(() -> {
			initializeHealthBar(root);
		});

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);

		// 初始化 ShootingComponent
		shootingComponent = new ShootingComponent(this, FIRE_RATE, null, 0, PROJECTILE_Y_POSITION_OFFSET);

		// 初始化 AnimationComponent
		animationComponent = new AnimationComponent(root);

		// 开始射击
		shootingComponent.startFiring();

		// 初始化盾牌检查逻辑
		initializeShieldCheck();
	}

	private void initializeShieldCheck() {
		shieldCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(SHIELD_CHECK_INTERVAL), e -> {
			if (shield == null || shield.isDestroyed()) {
				summonShield();
			}
		}));
		shieldCheckTimeline.setCycleCount(Timeline.INDEFINITE);
		shieldCheckTimeline.play();
	}

	private void summonShield() {
		if (isSummoningShield) {
			return; // 盾牌正在生成，直接返回
		}
		isSummoningShield = true;

		Platform.runLater(() -> {
			double shieldX = getCollisionComponent().getHitboxX();
			double shieldY = getCollisionComponent().getHitboxY() + (getCollisionComponent().getHitboxHeight() / 2) - (Shield.SHIELD_SIZE / 2);
			shield = new Shield(shieldX, shieldY); // 不传递 root

			// 将盾牌添加到敌人单位列表，并由 LevelParent 添加到 root
			level.addEnemyUnit(shield);

			isSummoningShield = false;
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

		// 更新盾牌的位置，确保始终在Boss右侧
		if (shield != null && !shield.isDestroyed()) {
			double shieldX = getCollisionComponent().getHitboxX(); // 在Boss右侧10px
			double shieldY = getCollisionComponent().getHitboxY() + (getCollisionComponent().getHitboxHeight() / 2) - (Shield.SHIELD_SIZE / 2);
			shield.setLayoutX(shieldX);
			shield.setLayoutY(shieldY);
		}

		// 更新射击逻辑
		shootingComponent.update(deltaTime, level);
	}

	@Override
	public void takeDamage(int damage) {
		if (shield == null || shield.isDestroyed()) { // 修改为检查盾牌是否存在或已销毁
			super.takeDamage(damage);
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
	public void destroy() {
		if (!isDestroyed) {
			super.destroy(); // 设置isDestroyed和隐藏Boss

			// 停止盾牌检查逻辑
			if (shieldCheckTimeline != null) {
				shieldCheckTimeline.stop();
			}

			// 销毁盾牌
			if (shield != null && !shield.isDestroyed()) {
				shield.destroy();
			}

			// 计算中心位置
			double x = getCollisionComponent().getHitboxX() + getCollisionComponent().getHitboxWidth();
			double y = getCollisionComponent().getHitboxY();

			animationComponent.playExplosion(x, y, 2.5); // 中间爆炸稍微大一点
		}
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
