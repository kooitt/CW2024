package com.example.demo.levels;

import com.example.demo.actors.ActiveActor;
import com.example.demo.actors.EnemyPlane;
import com.example.demo.actors.Shield;
import com.example.demo.actors.UserPlane;
import com.example.demo.interfaces.Hitbox;
import com.example.demo.projectiles.Projectile;
import com.example.demo.utils.ObjectPool;
import com.example.demo.projectiles.BulletFactory;
import com.example.demo.utils.KeyBindings;
import com.example.demo.views.LevelView;
import com.example.demo.views.LevelViewLevelTwo;
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
	private static final int MILLISECOND_DELAY = 40;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	protected final List<ActiveActor> friendlyUnits;
	protected final List<ActiveActor> enemyUnits;
	protected final List<ActiveActor> userProjectiles;
	protected final List<ActiveActor> enemyProjectiles;
	protected final List<ActiveActor> shields; // 新增 Shields 列表

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private Set<KeyCode> activeKeys;

	private ObjectPool<Projectile> userProjectilePool;
	private ObjectPool<Projectile> enemyProjectilePool;
	private ObjectPool<Projectile> bossProjectilePool;

	private long lastUpdateTime;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane();
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.shields = new ArrayList<>(); // 初始化 Shields 列表
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

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

		return scene;
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
		handleCollisions(); // 使用优化后的碰撞检测
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
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				activeKeys.add(kc);
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				activeKeys.remove(kc);
			}
		});
		root.getChildren().add(background);
	}

	private void processInput() {
		KeyBindings keyBindings = KeyBindings.getInstance();
		boolean movingUp = activeKeys.contains(keyBindings.getUpKey());
		boolean movingDown = activeKeys.contains(keyBindings.getDownKey());
		boolean movingLeft = activeKeys.contains(keyBindings.getLeftKey());
		boolean movingRight = activeKeys.contains(keyBindings.getRightKey());

		// 处理垂直移动
		if (movingUp && !movingDown) {
			user.moveUp();
		} else if (movingDown && !movingUp) {
			user.moveDown();
		} else {
			user.stopVerticalMovement();
		}

		// 处理水平移动
		if (movingLeft && !movingRight) {
			user.moveLeft();
		} else if (movingRight && !movingLeft) {
			user.moveRight();
		} else {
			user.stopHorizontalMovement();
		}
	}

	private void updateActors(double deltaTime) {
		friendlyUnits.forEach(actor -> {
			actor.updateActor(deltaTime, this);
		});
		enemyUnits.forEach(actor -> {
			actor.updateActor(deltaTime, this);
		});
		shields.forEach(shield -> { // 更新 Shields
			shield.updateActor(deltaTime, this);
		});
		userProjectiles.forEach(projectile -> {
			projectile.updateActor(deltaTime, this);
		});
		enemyProjectiles.forEach(projectile -> {
			projectile.updateActor(deltaTime, this);
		});
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits, enemyProjectilePool, bossProjectilePool);
		removeDestroyedActors(shields);
		removeDestroyedActors(userProjectiles, userProjectilePool);
		removeDestroyedActors(enemyProjectiles, enemyProjectilePool, bossProjectilePool);
	}

	private void removeDestroyedActors(List<ActiveActor> actors, ObjectPool<Projectile>... pools) {
		Iterator<ActiveActor> iterator = actors.iterator();
		while (iterator.hasNext()) {
			ActiveActor actor = iterator.next();
			if (actor.isDestroyed()) {
				root.getChildren().remove(actor);
				iterator.remove();
				if (actor instanceof Projectile) {
					Projectile projectile = (Projectile) actor;
					for (ObjectPool<Projectile> pool : pools) {
						pool.release(projectile);
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

		// 只有当Shield不存在或已销毁时，才处理子弹与敌方单位（包括Boss）的碰撞
		handleCollisions(userProjectiles, enemyUnits);
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActor> actors1, List<ActiveActor> actors2) {
		for (ActiveActor actor1 : actors1) {
			for (ActiveActor actor2 : actors2) {
				if (actor1.isDestroyed() || actor2.isDestroyed()) {
					continue; // 忽略已销毁的演员
				}
				if (actor1.getCollisionComponent().checkCollision(actor2.getCollisionComponent())) {
					System.out.println("Collision detected between " + actor1 + " and " + actor2);
					actor1.takeDamage(1);
					actor2.takeDamage(1);
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActor enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage(1); // 假设敌机穿过玩家造成1点伤害
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
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
	}

	public void cleanUp() {
		timeline.stop();
		root.getChildren().clear();
		friendlyUnits.clear();
		enemyUnits.clear();
		shields.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
	}

	protected UserPlane getUser() {
		return user;
	}

	public Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size() + shields.size(); // 包含Shields
	}

	public void addEnemyUnit(ActiveActor enemy) {
		if (!enemyUnits.contains(enemy) && !(enemy instanceof Shield)) { // 确保不重复添加，且Shield单独管理
			enemyUnits.add(enemy);
			root.getChildren().add(enemy);
		} else if (enemy instanceof Shield && !shields.contains(enemy)) {
			shields.add(enemy);
			root.getChildren().add(enemy);
		}
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected double getScreenHeight() {
		return screenHeight;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size() + shields.size();
	}

	private void removeProjectilesOutOfBounds() {
		double screenWidth = getScreenWidth();
		double screenHeight = getScreenHeight();

		Iterator<ActiveActor> userProjIterator = userProjectiles.iterator();
		while (userProjIterator.hasNext()) {
			ActiveActor projectile = userProjIterator.next();
			double x = projectile.getCollisionComponent().getHitboxX();
			double y = projectile.getCollisionComponent().getHitboxY();
			if (x > screenWidth || x + projectile.getCollisionComponent().getHitboxWidth() < 0 ||
					y > screenHeight || y + projectile.getCollisionComponent().getHitboxHeight() < 0) {
				projectile.destroy();
			}
		}

		Iterator<ActiveActor> enemyProjIterator = enemyProjectiles.iterator();
		while (enemyProjIterator.hasNext()) {
			ActiveActor projectile = enemyProjIterator.next();
			double x = projectile.getCollisionComponent().getHitboxX();
			double y = projectile.getCollisionComponent().getHitboxY();
			if (x > screenWidth || x + projectile.getCollisionComponent().getHitboxWidth() < 0 ||
					y > screenHeight || y + projectile.getCollisionComponent().getHitboxHeight() < 0) {
				projectile.destroy();
			}
		}
	}

	// 添加以下方法，供 UserPlane 和敌人添加子弹
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
}
