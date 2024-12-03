package com.example.demo;

import javafx.scene.Group;

public class LevelTwoView extends LevelParentView {

	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;
	private final Group root;
	private final ShieldImage shieldImage;
	
	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();

		//debugging shield properties
		System.out.println("Shield X: " + shieldImage.getLayoutX());
		System.out.println("Shield Y: " + shieldImage.getLayoutY());
		System.out.println("Shield Visible: " + shieldImage.isVisible());
		System.out.println("Shield Width: " + shieldImage.getFitWidth());
		System.out.println("Shield Height: " + shieldImage.getFitHeight());
	}
	
	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
	}

}
