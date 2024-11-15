package com.example.demo.actors;

import javafx.scene.image.*;
import java.util.Objects;

/**
 * Abstract class representing an active actor in the game.
 */
public abstract class ActiveActor extends ImageView {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	/**
	 * Constructs an ActiveActor with the specified parameters.
	 *
	 * @param imageName the name of the image representing the actor.
	 * @param imageHeight the height of the image.
	 * @param initialXPos the initial X position of the actor.
	 * @param initialYPos the initial Y position of the actor.
	 */
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + imageName)).toExternalForm()));
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	/**
	 * Updates the position of the actor.
	 */
	public abstract void updatePosition();

	/**
	 * Moves the actor horizontally by the specified amount.
	 *
	 * @param horizontalMove the amount to move horizontally.
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the actor vertically by the specified amount.
	 *
	 * @param verticalMove the amount to move vertically.
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}