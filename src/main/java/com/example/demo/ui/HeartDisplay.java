// HeartDisplay.java
package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Manages the display of hearts representing player health.
 */
public class HeartDisplay {

	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;
	private HBox container;
	private double containerX;
	private double containerY;
	private int heartsToDisplay;

	/**
	 * Constructs a HeartDisplay at specified position with given number of hearts.
	 *
	 * @param xPosition      X position.
	 * @param yPosition      Y position.
	 * @param heartsToDisplay number of hearts.
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerX = xPosition;
		this.containerY = yPosition;
		this.heartsToDisplay = heartsToDisplay;
		initialize();
	}

	private void initialize() {
		container = new HBox();
		container.setLayoutX(containerX);
		container.setLayoutY(containerY);
		for (int i = 0; i < heartsToDisplay; i++) {
			ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
	}

	/**
	 * Removes a heart from the display.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	public HBox getContainer() {
		return container;
	}
}
