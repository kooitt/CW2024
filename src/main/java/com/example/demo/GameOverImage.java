package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	public GameOverImage(double xPosition, double yPosition) {
		URL resource = getClass().getResource(IMAGE_NAME);
		if (resource != null){
			setImage(new Image(resource.toExternalForm()));
		} else {
			System.err.println("Background image not found: ");
		}
//		setImage(ImageSetUp.getImageList().get(ImageSetUp.getGameOver()));
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}

}
