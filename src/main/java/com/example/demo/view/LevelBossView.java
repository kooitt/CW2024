package com.example.demo.view;

import javafx.scene.Group;

/**
 * Represents the view for the level boss, including the shield image.
 */
public class LevelBossView extends LevelView {

//	private static final int SHIELD_X_POSITION = 1200; // X-coordinate position of the shield
//	private static final int SHIELD_Y_POSITION = 350; // Y-coordinate position of the shield
//	//no use
	private final Group root;
//	private final ShieldImage shieldImage;

	/**
	 * Constructs a LevelBossView with the specified root group, number of hearts to display, and maximum kills.
	 *
	 * @param root the root group of the scene.
	 * @param heartsToDisplay the number of hearts to display.
	 * @param maxKills the maximum number of kills to display.
	 */
	public LevelBossView(Group root, int heartsToDisplay, int maxKills) {
		super(root, heartsToDisplay, maxKills);
		this.root = root;
//		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
//		addImagesToRoot();
	}


	/**
	 * Adds images to root
	 */
//	private void addImagesToRoot() {
//		root.getChildren().addAll(shieldImage);
//	}
}