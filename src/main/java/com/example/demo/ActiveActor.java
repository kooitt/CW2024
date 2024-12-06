package com.example.demo;

import javafx.scene.image.*;

public abstract class ActiveActor extends ImageView implements ActorBehaviour{

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

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}

}
