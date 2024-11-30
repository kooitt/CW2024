// ActiveActor.java
package com.example.demo.actors;

import com.example.demo.components.CollisionComponent;
import com.example.demo.components.HealthComponent;
import com.example.demo.components.MovementComponent;
import com.example.demo.interfaces.Destructible;
import com.example.demo.levels.LevelParent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract class representing an active actor in the game.
 * Handles image display, movement, collision, and health.
 */
public abstract class ActiveActor extends Actor implements Destructible {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	protected ImageView imageView;
	protected MovementComponent movementComponent;
	private CollisionComponent collisionComponent;
	private HealthComponent healthComponent;
	protected boolean isDestroyed;

	/**
	 * Constructs an ActiveActor with specified image, position, and health.
	 *
	 * @param imageName   the name of the image file.
	 * @param imageHeight the height of the image.
	 * @param initialXPos initial X position.
	 * @param initialYPos initial Y position.
	 * @param maxHealth   maximum health.
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
		super();
		imageView = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		imageView.setFitHeight(imageHeight);
		imageView.setPreserveRatio(true);
		setLayoutX(initialXPos);
		setLayoutY(initialYPos);
		getChildren().add(imageView);

		movementComponent = new MovementComponent(0, 0);
		healthComponent = new HealthComponent(this, maxHealth);
		setHealthComponent(healthComponent);

		double hitboxWidth = imageView.getFitWidth();
		double hitboxHeight = imageView.getFitHeight();
		collisionComponent = new CollisionComponent(this, hitboxWidth, hitboxHeight);
		setCollisionComponent(collisionComponent);

		isDestroyed = false;
	}

	/**
	 * Updates the position based on movement component.
	 */
	public void updatePosition() {
		movementComponent.update(this);
	}

	/**
	 * Abstract method to update actor's state.
	 *
	 * @param deltaTime time since last update.
	 * @param level     current game level.
	 */
	public abstract void updateActor(double deltaTime, LevelParent level);

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

	public ImageView getImageView() {
		return imageView;
	}

	public void setHealthComponent(HealthComponent healthComponent) {
		this.healthComponent = healthComponent;
	}

	public HealthComponent getHealthComponent() {
		return healthComponent;
	}

	@Override
	public void takeDamage(int damage) {
		if (healthComponent != null) {
			healthComponent.takeDamage(damage);
		}
	}

	public int getCurrentHealth() {
		return healthComponent != null ? healthComponent.getCurrentHealth() : 0;
	}

	public int getMaxHealth() {
		return healthComponent != null ? healthComponent.getMaxHealth() : 0;
	}

	public CollisionComponent getCollisionComponent() {
		return collisionComponent;
	}

	public void setCollisionComponent(CollisionComponent collisionComponent) {
		this.collisionComponent = collisionComponent;
	}

	/**
	 * Gets the X position for projectile spawning.
	 *
	 * @param offset offset from the actor's position.
	 * @return X position.
	 */
	public double getProjectileXPosition(double offset) {
		return getLayoutX() + getTranslateX() + offset;
	}

	/**
	 * Gets the Y position for projectile spawning.
	 *
	 * @param offset offset from the actor's position.
	 * @return Y position.
	 */
	public double getProjectileYPosition(double offset) {
		return getLayoutY() + getTranslateY() + offset;
	}
}
