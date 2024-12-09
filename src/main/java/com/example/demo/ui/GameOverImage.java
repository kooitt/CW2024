package com.example.demo.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * The {@code GameOverImage} class represents the game over screen image.
 * This class is responsible for displaying the "Game Over" image on the game screen
 * when the game ends. The image is positioned and sized according to the defined
 * constants and is managed within a specified {@link Group} root node.
 */
public class GameOverImage extends ImageView {

	/**
	 * Path to the "Game Over" image resource.
	 */
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	/**
	 * Base size for the "Game Over" image. This value is multiplied to determine
	 * the final width and height of the image.
	 */
	private static final int IMAGE_SIZE = 100;

	/**
	 * X-coordinate of the "Game Over" image position on the screen.
	 */
	private static final int LOSS_X = 450;

	/**
	 * Y-coordinate of the "Game Over" image position on the screen.
	 */
	private static final int LOSS_Y = 150;

	/**
	 * The root {@link Group} where the "Game Over" image will be displayed.
	 */
	private final Group root;

	/**
	 * Constructs a {@code GameOverImage} instance.
	 *
	 * @param root the {@link Group} that serves as the parent for the "Game Over" image.
	 *             This is where the image will be added to the scene graph when displayed.
	 */
	public GameOverImage(Group root) {
		this.root = root;
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		setFitWidth(IMAGE_SIZE * 4);
		setFitHeight(IMAGE_SIZE * 3);
		setLayoutX(LOSS_X);
		setLayoutY(LOSS_Y);
		setVisible(false);
	}

	/**
	 * Displays the "Game Over" image on the screen.
	 *
	 * This method adds the "Game Over" image to the {@code root} {@link Group} if it is
	 * not already present and makes it visible. It ensures that the image is only added
	 * once to avoid duplication in the scene graph.
	 */
	public void showGameOverImage() {
		if (!root.getChildren().contains(this)) {
			root.getChildren().add(this);
		}
		setVisible(true);
	}
}
