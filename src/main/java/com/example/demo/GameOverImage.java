package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";
	private static final double IMAGE_HEIGHT = 300;
	private static final double IMAGE_WIDTH = 300;
	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()) );
		setFitHeight(IMAGE_HEIGHT);
		setFitWidth(IMAGE_WIDTH);
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}

}
