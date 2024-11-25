package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActor;
import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;
import com.example.demo.utils.GameSettings;

public abstract class Projectile extends ActiveActor {

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);

		// 初始化 CollisionComponent
		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		CollisionComponent collisionComponent = new CollisionComponent(this, hitboxWidth, hitboxHeight);
		setCollisionComponent(collisionComponent);
	}

	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0);
		setTranslateY(0);
		isDestroyed = false;
		setVisible(true);

		if (GameSettings.SHOW_HITBOXES && getCollisionComponent() != null) {
			// 显示碰撞盒
		}
	}

	public void reset() {
		setVisible(false);
		isDestroyed = true;

		getMovementComponent().setVelocity(0, 0);

		if (GameSettings.SHOW_HITBOXES && getCollisionComponent() != null) {
			// 隐藏碰撞盒
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void takeDamage(int damage) {
		this.destroy();
	}


	@Override
	public void updateActor(double deltaTime, LevelParent level) {
		updatePosition();
		getCollisionComponent().updateHitBoxPosition();
	}

}
