package com.example.demo.ui;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;

public class ShieldImage {

	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	private static final int SHIELD_SIZE = 200;
	private HBox container;
	private ImageView shieldImageView;

	public ShieldImage(double xPosition, double yPosition) {
		initializeContainer(xPosition, yPosition);
		initializeShieldImage();
	}

	private void initializeContainer(double xPosition, double yPosition) {
		container = new HBox();
		container.setLayoutX(xPosition);
		container.setLayoutY(yPosition);
		container.setVisible(false);
	}

	private void initializeShieldImage() {
		shieldImageView = new ImageView();
		URL imageUrl = getClass().getResource(IMAGE_NAME);
		if (imageUrl != null) {
			shieldImageView.setImage(new Image(imageUrl.toExternalForm()));
		}
		shieldImageView.setFitHeight(SHIELD_SIZE);
		shieldImageView.setFitWidth(SHIELD_SIZE);
		shieldImageView.setOpacity(1.0);
		shieldImageView.setPreserveRatio(true);
		container.getChildren().add(shieldImageView);
	}

	public void setPosition(double x, double y) {
		Platform.runLater(() -> {
			container.setLayoutX(x);
			container.setLayoutY(y);
		});
	}

	public void showShield() {
		Platform.runLater(() -> {
			container.setVisible(true);
			container.toFront();
		});
	}

	public void hideShield() {
		Platform.runLater(() -> {
			container.setVisible(false);
		});
	}

	public HBox getContainer() {
		return container;
	}
}
