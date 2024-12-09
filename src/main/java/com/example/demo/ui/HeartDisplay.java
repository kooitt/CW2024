package com.example.demo.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * The {@code HeartDisplay} class is responsible for displaying and managing a visual representation
 * of hearts as a life indicator. Hearts are displayed in an {@link HBox} and can be dynamically
 * added or removed based on game state.
 */
public class HeartDisplay {

	/**
	 * The file path for the heart image resource.
	 */
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";

	/**
	 * The height of the heart image.
	 */
	private static final int HEART_HEIGHT = 50;

	/**
	 * The index of the first item in the heart container.
	 */
	private static final int INDEX_OF_FIRST_ITEM = 0;

	/**
	 * The container to hold and display heart images.
	 */
	private HBox container;

	/**
	 * The X-coordinate of the heart display container.
	 */
	private final double containerX;

	/**
	 * The Y-coordinate of the heart display container.
	 */
	private final double containerY;

	/**
	 * The number of hearts to display initially.
	 */
	private final int heartsToDisplay;

	/**
	 * The root {@link Group} where the heart display will be added.
	 */
	private final Group root;

	/**
	 * Constructs a {@code HeartDisplay} with the specified parameters.
	 *
	 * @param root           the root group where the heart display is rendered
	 * @param xPosition      the X-coordinate of the heart display container
	 * @param yPosition      the Y-coordinate of the heart display container
	 * @param heartsToDisplay the number of hearts to display initially
	 */
	public HeartDisplay(Group root, double xPosition, double yPosition, int heartsToDisplay) {
		this.root = root;
		this.containerX = xPosition;
		this.containerY = yPosition;
		this.heartsToDisplay = heartsToDisplay;
		initialize();
	}

	/**
	 * Initializes the heart display container and populates it with the initial number of hearts.
	 */
	private void initialize() {
		container = new HBox(5); // Adds spacing between hearts for better visual appearance
		container.setLayoutX(containerX);
		container.setLayoutY(containerY);
		for (int i = 0; i < heartsToDisplay; i++) {
			ImageView heart = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(HEART_IMAGE_NAME)).toExternalForm()));
			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
	}

	/**
	 * Adds the heart display container to the root if it is not already added.
	 */
	public void showHeartDisplay() {
		if (!root.getChildren().contains(container)) {
			root.getChildren().add(container);
		}
	}

	/**
	 * Removes a single heart from the heart display.
	 * If no hearts are left, this method has no effect.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	/**
	 * Adds a single heart to the heart display.
	 */
	public void addHeart() {
		ImageView heart = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(HEART_IMAGE_NAME)).toExternalForm()));
		heart.setFitHeight(HEART_HEIGHT);
		heart.setPreserveRatio(true);
		container.getChildren().add(heart);
	}

	/**
	 * Removes multiple hearts from the heart display until the specified number of hearts remain.
	 *
	 * @param heartsRemaining the number of hearts to leave in the display
	 */
	public void removeHearts(int heartsRemaining) {
		int currentHearts = container.getChildren().size();
		for (int i = 0; i < currentHearts - heartsRemaining; i++) {
			removeHeart();
		}
	}

	/**
	 * Adds multiple hearts to the heart display.
	 *
	 * @param heartsToAdd the number of hearts to add
	 */
	public void addHearts(int heartsToAdd) {
		for (int i = 0; i < heartsToAdd; i++) {
			addHeart();
		}
	}
}
