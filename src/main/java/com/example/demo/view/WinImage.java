package com.example.demo.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Represents an image displayed when the player wins the game.
 */
public class WinImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int HEIGHT = 500; // Height of the win image
	private static final int WIDTH = 600;  // Width of the win image

	/**
	 * Constructs a WinImage with the specified position.
	 *
	 * @param xPosition the x-coordinate position of the win image.
	 * @param yPosition the y-coordinate position of the win image.
	 */
	public WinImage(double xPosition, double yPosition) {
		this.setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		this.setVisible(false);
		this.setFitHeight(HEIGHT);
		this.setFitWidth(WIDTH);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
	}

	/**
	 * Shows the win image by making it visible.
	 */
	public void showWinImage() {
		this.setVisible(true);
	}
}