package com.example.demo.levels;

import com.example.demo.actors.*;
import com.example.demo.components.AnimationComponent;
import com.example.demo.components.CollisionComponent;
import com.example.demo.components.SoundComponent;
import com.example.demo.projectiles.Projectile;
import com.example.demo.utils.ObjectPool;
import com.example.demo.projectiles.BulletFactory;
import com.example.demo.utils.KeyBindings;
import com.example.demo.views.LevelView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

import java.util.*;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 30;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	protected final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	protected final List<ActiveActor> friendlyUnits;
	protected final List<ActiveActor> enemyUnits;
	protected final List<ActiveActor> userProjectiles;
	protected final List<ActiveActor> enemyProjectiles;
	protected final List<ActiveActor> shields; // 新增 Shields 列表
	protected final List<ActiveActor> powerUps;
	protected boolean isInputEnabled = true; // 添加此变量

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private Set<KeyCode> activeKeys;

	private ObjectPool<Projectile> userProjectilePool;
	private ObjectPool<Projectile> enemyProjectilePool;
	private ObjectPool<Projectile> bossProjectilePool;
	private ObjectPool<Projectile> bossTwoProjectilePool;

	private AnimationComponent animationComponent; // 新增字段

	private long lastUpdateTime;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.animationComponent = new AnimationComponent(root); // 初始化 AnimationComponent
		this.user = new UserPlane(5);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.shields = new ArrayList<>(); // 初始化 Shields 列表
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.powerUps = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		this.activeKeys = new HashSet<>();

		initializeTimeline();
		friendlyUnits.add(user);

		userProjectilePool = new ObjectPool<>(new BulletFactory("user"));
		enemyProjectilePool = new ObjectPool<>(new BulletFactory("enemy"));
		bossProjectilePool = new ObjectPool<>(new BulletFactory("boss"));
		bossTwoProjectilePool = new ObjectPool<>(new BulletFactory("bossTwo"));

		lastUpdateTime = System.nanoTime();
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		user.setAnimationComponent(animationComponent); // 传递 AnimationComponent 给 UserPlane
		return scene;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}
	protected double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}
	public void addEnemyUnit(ActiveActor enemy) {
		if (enemy instanceof Shield && !shields.contains(enemy)) {
			shields.add(enemy);
		} else if (!(enemy instanceof Shield) && !enemyUnits.contains(enemy)) {
			enemyUnits.add(enemy);
		}
		root.getChildren().add(enemy);
	}
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		long currentTime = System.nanoTime();
		double deltaTime = (currentTime - lastUpdateTime) / 1e9; // 将纳秒转换为秒
		lastUpdateTime = currentTime;

		processInput();
		spawnEnemyUnits();
		updateActors(deltaTime);
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleCollisions();
		removeProjectilesOutOfBounds();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitWidth(screenWidth);
		background.setFitHeight(screenHeight);
		background.setOnKeyPressed((EventHandler<KeyEvent>) e -> activeKeys.add(e.getCode()));
		background.setOnKeyReleased((EventHandler<KeyEvent>) e -> activeKeys.remove(e.getCode()));
		root.getChildren().add(background);
	}

	private void processInput() {
		if (!isInputEnabled) {
			// 如果输入被禁用，不处理任何输入
			user.stopVerticalMovement();
			user.stopHorizontalMovement();
			return;
		}

		KeyBindings keys = KeyBindings.getInstance();
		boolean up = activeKeys.contains(keys.getUpKey());
		boolean down = activeKeys.contains(keys.getDownKey());
		boolean left = activeKeys.contains(keys.getLeftKey());
		boolean right = activeKeys.contains(keys.getRightKey());

		if (up && !down) user.moveUp();
		else if (down && !up) user.moveDown();
		else user.stopVerticalMovement();

		if (left && !right) user.moveLeft();
		else if (right && !left) user.moveRight();
		else user.stopHorizontalMovement();
	}


	private void updateActors(double deltaTime) {
		friendlyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
		if (!isInputEnabled) {
			// 如果输入被禁用，只更新友军单位（例如玩家飞机的动画）
			return;
		}
		enemyUnits.forEach(actor -> actor.updateActor(deltaTime, this));
		shields.forEach(shield -> shield.updateActor(deltaTime, this));
		userProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
		enemyProjectiles.forEach(projectile -> projectile.updateActor(deltaTime, this));
		powerUps.forEach(powerUp -> powerUp.updateActor(deltaTime, this));
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits, enemyProjectilePool, bossProjectilePool);
		removeDestroyedActors(shields);
		removeDestroyedActors(userProjectiles, userProjectilePool);
		removeDestroyedActors(enemyProjectiles, enemyProjectilePool, bossProjectilePool);
		removeDestroyedActors(powerUps);
	}

	private void removeDestroyedActors(List<ActiveActor> actors, ObjectPool<Projectile>... pools) {
		Iterator<ActiveActor> iterator = actors.iterator();
		while (iterator.hasNext()) {
			ActiveActor actor = iterator.next();
			if (actor.isDestroyed()) {
				// 检查是否是 Boss 对象
				if (actor instanceof Boss) {
					Boss bossActor = (Boss) actor;
					if (!bossActor.isReadyToRemove) {
						continue; // 如果爆炸动画未播放完毕，跳过移除
					}
				}

				root.getChildren().remove(actor);
				iterator.remove();

				if (actor instanceof Projectile) {
					for (ObjectPool<Projectile> pool : pools) {
						pool.release((Projectile) actor);
					}
				}
			}
		}
	}


	private void removeDestroyedActors(List<ActiveActor> actors) {
		Iterator<ActiveActor> iterator = actors.iterator();
		while (iterator.hasNext()) {
			ActiveActor actor = iterator.next();
			if (actor.isDestroyed()) {
				root.getChildren().remove(actor);
				iterator.remove();
			}
		}
	}

	private void handleCollisions() {
		// 先处理子弹与盾牌的碰撞
		handleCollisions(userProjectiles, shields);
		handleCollisions(enemyProjectiles, friendlyUnits);

		// 处理敌机与用户飞机的碰撞
		handleCollisions(enemyUnits, Collections.singletonList(getUser()));

		// 处理用户飞机与 `ActorLevelUp` 的碰撞
		handlePickupCollisions(Collections.singletonList(getUser()), powerUps);

		// 只有当Shield不存在或已销毁时，才处理子弹与敌方单位（包括Boss）的碰撞
		handleCollisions(userProjectiles, enemyUnits);
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handlePickupCollisions(List<ActiveActor> actors1, List<ActiveActor> powerUps) {
		for (ActiveActor actor1 : actors1) {
			for (ActiveActor powerUp : powerUps) {
				if (actor1.isDestroyed() || powerUp.isDestroyed()) {
					continue; // 忽略已销毁的演员
				}
				if (actor1.getCollisionComponent().checkCollision(powerUp.getCollisionComponent())) {
					if (powerUp instanceof ActorLevelUp) {
						((ActorLevelUp) powerUp).onPickup(this); // 处理等级提升道具
					} else if (powerUp instanceof HeartItem) {
						((HeartItem) powerUp).onPickup(this); // 处理爱心道具
					}
				}
			}
		}
	}


	private void handleCollisions(List<ActiveActor> actors1, List<ActiveActor> actors2) {
		for (ActiveActor actor1 : actors1) {
			for (ActiveActor actor2 : actors2) {
				if (actor1.isDestroyed() || actor2.isDestroyed()) {
					continue; // 忽略已销毁的演员
				}
				if (actor1.getCollisionComponent().checkCollision(actor2.getCollisionComponent())) {
					actor1.takeDamage(1);
					actor2.takeDamage(1);
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActor enemy : enemyUnits) {
			if (Math.abs(enemy.getTranslateX()) > screenWidth) {
				user.takeDamage(1);
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getCurrentHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActor enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		// 停止处理玩家输入
		isInputEnabled = false;

		// 停止游戏循环
		timeline.stop();

		levelView.showWinImage();
	}


	protected void loseGame() {
		// 停止处理玩家输入
		isInputEnabled = false;

		// 停止游戏循环
		timeline.stop();

		levelView.showGameOverImage();
		SoundComponent.stopAllSound();
		SoundComponent.playGameoverSound();
	}



	public void cleanUp() {
		timeline.stop();
		root.getChildren().clear();
		friendlyUnits.clear();
		enemyUnits.clear();
		shields.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
		powerUps.clear();
	}

	public UserPlane getUser() {
		return user;
	}

	public Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size() + shields.size();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size() + shields.size();
	}

	private void removeProjectilesOutOfBounds() {
		removeOutOfBounds(userProjectiles, screenWidth, screenHeight);
		removeOutOfBounds(enemyProjectiles, screenWidth, screenHeight);
	}

	private void removeOutOfBounds(List<ActiveActor> projectiles, double width, double height) {
		Iterator<ActiveActor> iterator = projectiles.iterator();
		while (iterator.hasNext()) {
			ActiveActor proj = iterator.next();
			CollisionComponent cc = proj.getCollisionComponent();
			double x = cc.getHitboxX();
			double y = cc.getHitboxY();
			if (x > width || x + cc.getHitboxWidth() < 0 ||
					y > height || y + cc.getHitboxHeight() < 0) {
				proj.destroy();
			}
		}
	}

	public void addProjectile(Projectile projectile, ActiveActor owner) {
		if (owner instanceof UserPlane) {
			userProjectiles.add(projectile);
		} else {
			enemyProjectiles.add(projectile);
		}
	}

	public ObjectPool<Projectile> getUserProjectilePool() {
		return userProjectilePool;
	}

	public ObjectPool<Projectile> getEnemyProjectilePool() {
		return enemyProjectilePool;
	}

	public ObjectPool<Projectile> getBossProjectilePool() {
		return bossProjectilePool;
	}

	public ObjectPool<Projectile> getBossTwoProjectilePool() {
		return bossTwoProjectilePool;
	}

	public LevelView getLevelView() {
		return levelView;
	}
}
