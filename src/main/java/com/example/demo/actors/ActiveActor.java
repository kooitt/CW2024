package com.example.demo.actors;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ActiveActor extends Group {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	protected ImageView imageView;

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		imageView = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		imageView.setFitHeight(imageHeight);
		imageView.setPreserveRatio(true);
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.getChildren().add(imageView);
	}

	public abstract void updatePosition();

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
