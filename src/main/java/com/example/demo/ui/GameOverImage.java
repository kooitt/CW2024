// GameOverImage.java
package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Displays the game over image.
 */
public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	/**
	 * Constructs a GameOverImage at specified position.
	 *
	 * @param xPosition X position.
	 * @param yPosition Y position.
	 */
	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}
}
