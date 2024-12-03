package com.example.demo;

import javafx.scene.image.*;
import java.net.URL;

public abstract class ActiveActor extends ImageView {

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		this.loadActiveActorImage(imageName);
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	public void loadActiveActorImage(String imageName){
		Image image = ImageLoader.loadImage(imageName);
		if (image != null) {
			this.setImage(image);
		}
	}

	public abstract void updatePosition();

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}

}
