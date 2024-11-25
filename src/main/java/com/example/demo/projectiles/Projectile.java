// Projectile.java

package com.example.demo.projectiles;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.utils.GameSettings;

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

		// If hitboxVisualization exists, set it to visible
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setVisible(true);
		}

		// In subclasses, reset the speed
	}

	public void reset() {
		setVisible(false);
		setDestroyed(true);

		// Stop movement
		getMovementComponent().setVelocity(0, 0);

		// Hide the hitboxVisualization
		if (GameSettings.SHOW_HITBOXES && hitboxVisualization != null) {
			hitboxVisualization.setVisible(false);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
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
