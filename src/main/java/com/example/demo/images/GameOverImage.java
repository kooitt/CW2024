package com.example.demo.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/LevelUI/gameover.png";
	private static final double IMAGE_HEIGHT = 300;
	private static final double IMAGE_WIDTH = 300;
	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()) );
		setFitHeight(IMAGE_HEIGHT);
		setFitWidth(IMAGE_WIDTH);
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}

}
