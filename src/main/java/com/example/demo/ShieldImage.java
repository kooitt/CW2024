package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

public class ShieldImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	private static final int SHIELD_SIZE = 200;
	
	public ShieldImage(double xPosition, double yPosition) {
		//this.setImage(new Image(IMAGE_NAME));
		URL resource = getClass().getResource(IMAGE_NAME);
		if (resource != null){
			this.setImage(new Image(resource.toExternalForm()));
		}else{
			System.err.println("Background image not found: ");
		}
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void showShield() {
		this.setVisible(true);
		System.out.println("Shield is now visible");
	}
	
	public void hideShield() {
		this.setVisible(false);
		System.out.println("Shield is now hidden");
	}

}
