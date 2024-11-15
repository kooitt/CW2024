package com.example.demo.view;

import javafx.scene.Group;

/**
 * Represents the view for the level boss, including the shield image.
 */
public class LevelBossView extends LevelView {

	private static final int SHIELD_X_POSITION = 355; // X-coordinate position of the shield
	private static final int SHIELD_Y_POSITION = 175; // Y-coordinate position of the shield
	private final Group root;
	private final ShieldImage shieldImage;

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
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		showShieldImage();
	}

	/**
	 * Shows the shield image by adding it to the root group and making it visible.
	 */
	public void showShieldImage() {
		shieldImage.showShield();
		root.getChildren().add(shieldImage);
		System.out.println("Added Shield");
		shieldImage.showShield();
	}

	/**
	 * Hides the shield image from the view.
	 */
	public void hideShieldImage() {
		shieldImage.hideShield();
	}
}