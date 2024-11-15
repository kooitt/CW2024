package com.example.demo.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Represents the game over image displayed when the player loses the game.
 */
public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	/**
	 * Constructs a GameOverImage with the specified position.
	 *
	 * @param xPosition the x-coordinate position of the image.
	 * @param yPosition the y-coordinate position of the image.
	 */
	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		setLayoutX(xPosition);
		setLayoutY(yPosition);
		setFitHeight(480);
		setPreserveRatio(true);
	}
}