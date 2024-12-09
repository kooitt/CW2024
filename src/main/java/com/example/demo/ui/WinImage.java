// WinImage.java
package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class WinImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int IMAGE_SIZE = 100;
	private static final int WIN_X = 450;
	private static final int WIN_Y = 150;

	public WinImage() {
		setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_NAME)).toExternalForm()));
		setFitWidth(IMAGE_SIZE*4);
		setFitHeight(IMAGE_SIZE*3);
		setLayoutX(WIN_X);
		setLayoutY(WIN_Y);
		setVisible(false);
	}

	public void showWinImage() {
		setVisible(true);
	}
}
