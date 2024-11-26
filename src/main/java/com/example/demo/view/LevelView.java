package com.example.demo.view;

import javafx.scene.Group;

/**
 * Represents the view for a level in the game, including displays for hearts and kill count.
 */
public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 550; // X-coordinate position of the heart display
	private static final double HEART_DISPLAY_Y_POSITION = 25;  // Y-coordinate position of the heart display
	private static final double KILL_COUNT_X_POSITION = 1100;   // X-coordinate position of the kill count display
	private static final double KILL_COUNT_Y_POSITION = 25;     // Y-coordinate position of the kill count display
	private final Group root;
	private final HeartDisplay heartDisplay;
	private final KillCountDisplay killCountDisplay;

	/**
	 * Constructs a LevelView with the specified root group, number of hearts to display, and maximum kills.
	 *
	 * @param root the root group of the scene.
	 * @param heartsToDisplay the number of hearts to display.
	 * @param maxKills the maximum number of kills to display.
	 */
	public LevelView(Group root, int heartsToDisplay, int maxKills) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.killCountDisplay = new KillCountDisplay(KILL_COUNT_X_POSITION, KILL_COUNT_Y_POSITION, maxKills);
	}

	/**
	 * Shows the heart display by adding it to the root group.
	 */
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	/**
	 * Shows the kill count display by adding it to the root group.
	 */
	public void showKillCountDisplay() {
		root.getChildren().add(killCountDisplay.getContainer());
	}

	/**
	 * Removes hearts from the display based on the remaining number of hearts.
	 *
	 * @param heartsRemaining the number of hearts remaining.
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	/**
	 * Updates the kill count display with the specified number of kills.
	 *
	 * @param kills the current number of kills.
	 */
	public void updateKillCount(int kills) {
		killCountDisplay.updateKillCount(kills);
	}
}