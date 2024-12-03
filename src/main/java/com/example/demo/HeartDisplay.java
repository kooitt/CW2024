package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeartDisplay {
	
	private static final String HEART_IMAGE_NAME = "heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;
	private HBox container;
	private final double containerXPosition;
	private final double containerYPosition;
	private final int numberOfHeartsToDisplay;
	private final Image heartImage;

	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		this.heartImage = loadHeartImage();
		initializeContainer();
		initializeHearts();
	}

	private Image loadHeartImage(){
		Image image = ImageLoader.loadImage(HEART_IMAGE_NAME);
		if (image == null) {
			System.err.println("Error: Unable to load heart image.");
		}
		return image;
	}
	
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);		
	}
	
	private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			ImageView heart = createHeartImageView();
			container.getChildren().add(heart);
		}
	}

	private ImageView createHeartImageView(){
		ImageView heart = new ImageView();
		if (heart != null) {
			heart.setImage(heartImage);
		}
		heart.setFitHeight(HEART_HEIGHT);
		heart.setPreserveRatio(true);
		return heart;
	}

	public void removeHearts(int heartsRemaining) {
		int numberOfHearts = getContainer().getChildren().size();
		int heartsToRemove = numberOfHearts - heartsRemaining;
		for (int i = 0; i < heartsToRemove; i++) {
			removeHeart();
		}
	}

	public void removeHeart() {
		if (!container.getChildren().isEmpty())
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
	}
	
	public HBox getContainer() {
		return container;
	}

}
