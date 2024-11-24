package com.example.demo;

import com.example.demo.views.LevelView;
import javafx.application.Platform;
import javafx.scene.Group;


public class LevelViewLevelTwo extends LevelView {

	private static final int SHIELD_X_POSITION = 500;
	private static final int SHIELD_Y_POSITION = 300;
	private final ShieldImage shieldImage;

	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();
	}

	private void addImagesToRoot() {
		System.out.println("Adding shield to root");
		Platform.runLater(() -> {
			getRoot().getChildren().add(shieldImage.getContainer());
			System.out.println("Children in root: " + getRoot().getChildren().size());
		});
	}

	public void showShield() {
		shieldImage.showShield();
	}

	public void hideShield() {
		shieldImage.hideShield();
	}
}
