package com.example.demo.actors.shield;

import com.example.demo.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {
	
	private static final String IMAGE_NAME = "shield.png";
	private static final int SHIELD_SIZE = 200;
	
	public ShieldImage(double xPosition, double yPosition) {
		//this.setImage(new Image(IMAGE_NAME));
		this.loadShieldImage();
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void loadShieldImage(){
		Image image = ImageLoader.loadImage(IMAGE_NAME);
		if (image != null) {
			this.setImage(image);
		}
	}
}
