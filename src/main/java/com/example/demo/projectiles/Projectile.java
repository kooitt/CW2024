package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;

public abstract class Projectile extends ActiveActorDestructible {

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);

		// 初始化 MovementComponent，初始速度为 (0, 0)
		getMovementComponent().setVelocity(0, 0);
	}

	public void resetPosition(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
		setTranslateX(0);
		setTranslateY(0);
		setDestroyed(false);
		setVisible(true);

		// 在子类中重新设置速度
	}

	public void reset() {
		setVisible(false);
		setDestroyed(true);

		// 停止移动
		getMovementComponent().setVelocity(0, 0);
	}

	@Override
	public void takeDamage() {
		this.destroy();
	}

	@Override
	public void updateActor() {
		super.updateActor(); // 确保调用父类的 updateActor()
		updateHitBoxPosition();
	}
}
