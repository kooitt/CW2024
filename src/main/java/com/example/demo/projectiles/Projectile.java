package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.levels.LevelParent;
import com.example.demo.utils.GameSettings;

public abstract class Projectile extends ActiveActorDestructible {

	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos, 1); // 子弹的生命值为1

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

		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setVisible(true);
		}
	}

	public void reset() {
		setVisible(false);
		setDestroyed(true);

		getMovementComponent().setVelocity(0, 0);

		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setVisible(false);
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
		super.updateActor(); // 更新位置等
		updateHitBoxPosition();
	}
}
