// GameOverImage.java
package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";
	private static final int IMAGE_SIZE = 100;
	private static final int LOSS_X = 450;
	private static final int LOSS_Y = 150;

	public GameOverImage() {
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		setFitWidth(IMAGE_SIZE*4);
		setFitHeight(IMAGE_SIZE*3);
		setLayoutX(LOSS_X);
		setLayoutY(LOSS_Y);
	}
}
