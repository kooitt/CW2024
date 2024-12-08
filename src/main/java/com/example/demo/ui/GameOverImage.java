package com.example.demo.ui;

import com.example.demo.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "gameover.png";

	public GameOverImage(double xPosition, double yPosition) {
		this.loadGameOverImage();
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}

	public void loadGameOverImage(){
		Image image = ImageLoader.loadImage(IMAGE_NAME);
		if (image != null) {
			this.setImage(image);
		}
	}

}
