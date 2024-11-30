package com.example.demo.views;

import javafx.scene.Group;
import com.example.demo.ui.*;

/**
 * Manages the UI elements for a game level.
 */
public class LevelView {

	private static final double HEART_X = 5;
	private static final double HEART_Y = 25;
	private static final int WIN_X = 355;
	private static final int WIN_Y = 175;
	private static final int LOSS_X = -160;
	private static final int LOSS_Y = -375;
	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;

	/**
	 * Constructs a LevelView with specified root and number of hearts.
	 *
	 * @param root          the root group.
	 * @param heartsToDisplay number of hearts to display.
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_X, HEART_Y, heartsToDisplay);
		this.winImage = new WinImage(WIN_X, WIN_Y);
		this.gameOverImage = new GameOverImage(LOSS_X, LOSS_Y);
	}

	/**
	 * Adds the heart display to the root.
	 */
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	/**
	 * Displays the win image.
	 */
	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	/**
	 * Displays the game over image.
	 */
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	/**
	 * Removes hearts based on remaining health.
	 *
	 * @param heartsRemaining number of hearts remaining.
	 */
	public void removeHearts(int heartsRemaining) {
		int currentHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}
}
