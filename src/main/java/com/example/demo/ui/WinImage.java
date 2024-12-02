// WinImage.java
package com.example.demo.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WinImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int HEIGHT = 500;
	private static final int WIDTH = 600;

	public WinImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		setFitHeight(HEIGHT);
		setFitWidth(WIDTH);
		setLayoutX(xPosition);
		setLayoutY(yPosition);
		setVisible(false);
	}

	public void showWinImage() {
		setVisible(true);
	}
}
