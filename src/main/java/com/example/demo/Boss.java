package com.example.demo;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.beans.binding.Bindings;
import java.util.*;

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
	private ShieldImage sld;

	// 添加血条
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

		// 初始化 ShieldImage 并添加到 root 中
		sld = new ShieldImage(INITIAL_X_POSITION, INITIAL_Y_POSITION);
		Platform.runLater(() -> {
			root.getChildren().add(sld.getContainer());
			sld.hideShield(); // 初始状态隐藏盾牌
			sld.getContainer().toFront(); // 将盾牌图像置于最前
		});

		// 初始化血条并添加到 root 中
		initializeHealthBar(root);
	}

	/**
	 * 初始化血条
	 *
	 * @param root 游戏场景的根节点
	 */
	private void initializeHealthBar(Group root) {
		healthBar = new ProgressBar(1.0); // 初始健康为满
		healthBar.setPrefWidth(HEALTH_BAR_WIDTH);
		healthBar.setPrefHeight(HEALTH_BAR_HEIGHT);

		// 设置血条颜色为红色
		healthBar.setStyle("-fx-accent: red;");

		// 添加血条到根节点
		Platform.runLater(() -> {
			root.getChildren().add(healthBar);
			healthBar.toFront(); // 确保血条在最前

			Scene currentScene = root.getScene();
			if (currentScene != null) {
				// 如果 Scene 已经存在，立即绑定
				bindHealthBarToScene(currentScene);
			} else {
				// 否则，添加监听器等待 Scene 设置
				root.sceneProperty().addListener((observable, oldScene, newScene) -> {
					if (newScene != null) {
						bindHealthBarToScene(newScene);
					}
				});
			}

			// 初始时根据健康值设置血条的可见性
			if (getHealth() <= 0) {
				healthBar.setVisible(false);
			} else {
				healthBar.setVisible(true);
			}
		});
	}

	/**
	 * 绑定血条的位置到 Scene
	 *
	 * @param scene 当前的 Scene
	 */
	private void bindHealthBarToScene(Scene scene) {
		// 绑定X坐标，使血条居中
		healthBar.layoutXProperty().bind(
				Bindings.createDoubleBinding(() ->
								(scene.getWidth() - healthBar.getPrefWidth()) / 2,
						scene.widthProperty()
				)
		);

		// 绑定Y坐标，使血条靠近底部
		healthBar.layoutYProperty().bind(
				Bindings.createDoubleBinding(() ->
								scene.getHeight() - healthBar.getPrefHeight() - 10, // 10px 的底部间距
						scene.heightProperty()
				)
		);

		// 调试输出，确认绑定已完成
		System.out.println("Health bar bound to scene.");
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
		updateShieldPosition(); // 更新盾牌的位置
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();

			// 确保健康值不为负数
			if (getHealth() < 0) {
				setHealth(0);
			}

			updateHealthBar(); // 更新血条
		} else {
			System.out.println("Shield blocked the damage!");
		}
	}

	/**
	 * 初始化移动模式
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/**
	 * 更新盾牌状态
	 */
	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			System.out.println("Shield is active. Frames with shield: " + framesWithShieldActivated);
			if (shieldExhausted()) {
				deactivateShield();
			}
		} else if (shieldShouldBeActivated()) {
			activateShield();
		}
	}

	/**
	 * 更新盾牌的位置
	 */
	private void updateShieldPosition() {
		Platform.runLater(() -> {
			double bossX = getLayoutX() + getTranslateX();
			double bossY = getLayoutY() + getTranslateY();

			// 计算盾牌的偏移量，确保盾牌居中
			double shieldOffsetX = (IMAGE_HEIGHT - sld.getContainer().getWidth()) / 2;
			double shieldOffsetY = (IMAGE_HEIGHT - sld.getContainer().getHeight()) / 2;

			sld.setPosition(bossX + shieldOffsetX, bossY + shieldOffsetY);
		});
	}

	/**
	 * 获取下一个移动方向
	 *
	 * @return 移动方向的垂直速度
	 */
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

	/**
	 * 判断Boss是否在当前帧发射子弹
	 *
	 * @return 是否发射子弹
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * 获取子弹的初始Y位置
	 *
	 * @return 子弹的初始Y位置
	 */
	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	/**
	 * 判断是否应该激活盾牌
	 *
	 * @return 是否激活盾牌
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	/**
	 * 判断盾牌是否耗尽
	 *
	 * @return 盾牌是否耗尽
	 */
	private boolean shieldExhausted() {
		boolean exhausted = framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
		System.out.println("Checking if shield is exhausted: " + exhausted);
		return exhausted;
	}

	/**
	 * 激活盾牌
	 */
	private void activateShield() {
		isShielded = true;
		sld.showShield(); // 显示盾牌
	}

	/**
	 * 取消激活盾牌
	 */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		sld.hideShield(); // 隐藏盾牌
	}

	/**
	 * 更新血条
	 */
	private void updateHealthBar() {
		Platform.runLater(() -> {
			double progress = (double) getHealth() / HEALTH;

			// 确保进度值不为负数
			if (progress < 0) {
				progress = 0;
				// 如果健康值可以被外部修改，确保将其重置为0
				// 如果没有 setHealth 方法，可以在这里添加逻辑以防止负值
				// 例如： setHealth(0);
			}

			healthBar.setProgress(progress);

			// 如果健康值为0，隐藏血条；否则，确保血条可见
			if (getHealth() <= 0) {
				healthBar.setVisible(false);
			} else {
				healthBar.setVisible(true);
			}
		});
	}
}
