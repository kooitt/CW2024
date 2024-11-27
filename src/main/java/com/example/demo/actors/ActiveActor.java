package com.example.demo.actors;

import com.example.demo.components.CollisionComponent;
import com.example.demo.components.HealthComponent;
import com.example.demo.components.MovementComponent;
import com.example.demo.interfaces.Destructible;
import com.example.demo.levels.LevelParent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ActiveActor extends Actor implements Destructible {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	protected ImageView imageView;
	protected MovementComponent movementComponent;
	private CollisionComponent collisionComponent;
	private HealthComponent healthComponent;
	protected boolean isDestroyed;

	/**
	 * 修改后的构造函数，添加了 maxHealth 参数
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
		super();
		imageView = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		imageView.setFitHeight(imageHeight);
		imageView.setPreserveRatio(true);
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.getChildren().add(imageView);

		// 初始化 MovementComponent，初始速度为 0
		movementComponent = new MovementComponent(0, 0);

		// 初始化 HealthComponent
		healthComponent = new HealthComponent(this, maxHealth);
		setHealthComponent(healthComponent);

		// 初始化 CollisionComponent，使用默认的碰撞盒大小
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		collisionComponent = new CollisionComponent(this, hitboxWidth, hitboxHeight);
		setCollisionComponent(collisionComponent);

		// 初始化 isDestroyed
		isDestroyed = false;
	}

	/**
	 * 更新位置的方法
	 */
	public void updatePosition() {
		// 使用 MovementComponent 更新位置
		movementComponent.update(this);
	}

	/**
	 * 抽象的 updateActor 方法，子类需要实现具体逻辑
	 */
	public abstract void updateActor(double deltaTime, LevelParent level);

	// 提供对 MovementComponent 的访问方法
	public MovementComponent getMovementComponent() {
		return movementComponent;
	}

	public void setMovementComponent(MovementComponent movementComponent) {
		this.movementComponent = movementComponent;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
		setVisible(false);
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	// 提供对 ImageView 的访问方法
	public ImageView getImageView() {
		return imageView;
	}

	// HealthComponent 方法
	public void setHealthComponent(HealthComponent healthComponent) {
		this.healthComponent = healthComponent;
	}

	public HealthComponent getHealthComponent() {
		return healthComponent;
	}

	public void takeDamage(int damage) {
		if (healthComponent != null) {
			healthComponent.takeDamage(damage);
		}
	}

	public int getCurrentHealth() {
		if (healthComponent != null) {
			return healthComponent.getCurrentHealth();
		}
		return 0;
	}

	public int getMaxHealth() {
		if (healthComponent != null) {
			return healthComponent.getMaxHealth();
		}
		return 0;
	}

	// CollisionComponent 方法
	public CollisionComponent getCollisionComponent() {
		return collisionComponent;
	}

	public void setCollisionComponent(CollisionComponent collisionComponent) {
		this.collisionComponent = collisionComponent;
	}

	/**
	 * 新增方法：获取子弹的发射位置
	 */
	public double getProjectileXPosition(double offset) {
		return getLayoutX() + getTranslateX() + offset;
	}

	public double getProjectileYPosition(double offset) {
		return getLayoutY() + getTranslateY() + offset;
	}
}
