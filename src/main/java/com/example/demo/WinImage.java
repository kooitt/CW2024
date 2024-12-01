package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

public class WinImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int HEIGHT = 500;
	private static final int WIDTH = 600;
	
	public WinImage(double xPosition, double yPosition) {
		URL resource = getClass().getResource(IMAGE_NAME);
		if (resource != null){
			this.setImage(new Image(resource.toExternalForm()));
		} else {
			System.err.println("Background image not found: ");
		}
		this.setVisible(false);
		this.setFitHeight(HEIGHT);
		this.setFitWidth(WIDTH);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
	}
	
	public void showWinImage() {
		this.setVisible(true);
	}

}
