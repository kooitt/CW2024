package com.example.demo.levels;

import com.example.demo.images.ShieldImage;
import javafx.scene.Group;

public class LevelViewLevelFour extends LevelView {

	private static final int SHIELD_X_POSITION = 1000;
	private static final int SHIELD_Y_POSITION = 500;
	private final Group root;
	private final ShieldImage shieldImage;

	public LevelViewLevelFour(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();
	}

	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
		System.out.println("Shield added to root!");
	}

}