package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {
	
	
	private static final int SHIELD_SIZE = 200;
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";

	public ShieldImage(double xPosition, double yPosition) {
	    this.setLayoutX(xPosition);
	    this.setLayoutY(yPosition);
	    this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm())); // Ensure correct path
	    this.setVisible(false);
	    this.setFitHeight(SHIELD_SIZE);
	    this.setFitWidth(SHIELD_SIZE);
	}

	
	public void showShield() {
		this.setVisible(true);
	}
	
	public void hideShield() {
		this.setVisible(false);
	}

}
