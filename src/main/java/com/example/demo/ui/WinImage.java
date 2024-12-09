package com.example.demo.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Represents the "You Win" image that is displayed when the player achieves a victory in the game.
 * This class extends {@link ImageView} and provides functionality to display the victory image
 * within a specified {@link Group}.
 */
public class WinImage extends ImageView {

	/**
	 * Path to the image file representing the "You Win" screen.
	 */
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	/**
	 * Base size of the image (used for scaling).
	 */
	private static final int IMAGE_SIZE = 100;

	/**
	 * X-coordinate for positioning the "You Win" image.
	 */
	private static final int WIN_X = 450;

	/**
	 * Y-coordinate for positioning the "You Win" image.
	 */
	private static final int WIN_Y = 150;

	/**
	 * The root {@link Group} where the "You Win" image will be added and displayed.
	 */
	private final Group root;

	/**
	 * Constructs a new {@code WinImage} object and initializes it with the specified {@link Group}.
	 * The image is set to be initially invisible and positioned based on the predefined constants.
	 *
	 * @param root the {@link Group} where the "You Win" image will be displayed.
	 */
	public WinImage(Group root) {
		this.root = root;
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		setFitWidth(IMAGE_SIZE * 4); // Scales the image width by 4 times the base size.
		setFitHeight(IMAGE_SIZE * 3); // Scales the image height by 3 times the base size.
		setLayoutX(WIN_X); // Sets the horizontal position of the image.
		setLayoutY(WIN_Y); // Sets the vertical position of the image.
		setVisible(false); // Initially hides the image.
	}

	/**
	 * Displays the "You Win" image by adding it to the {@link Group} if it is not already present.
	 * Ensures that the image is visible on the screen.
	 */
	public void showWinImage() {
		if (!root.getChildren().contains(this)) {
			root.getChildren().add(this); // Adds the image to the root group if not already added.
		}
		setVisible(true); // Makes the image visible.
	}
}
