package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.net.URL;

public class HeartDisplay {
	
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;
	private HBox container;
	private final double containerXPosition;
	private final double containerYPosition;
	private final int numberOfHeartsToDisplay;
	
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}
	
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);		
	}
	
	private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			ImageView heart = new ImageView();
			URL resource = getClass().getResource(HEART_IMAGE_NAME);
			if (resource != null){
				heart = new ImageView(new Image(resource.toExternalForm()));
			}else{
				System.err.println("Background image not found: ");
			}
			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
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
