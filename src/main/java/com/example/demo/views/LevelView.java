package com.example.demo.views;

import javafx.geometry.Pos;
import javafx.scene.Group;
import com.example.demo.ui.*;

public class LevelView {

	private static final double HEART_X = 5;
	private static final double HEART_Y = 25;
	private static final int WIN_X = 355;
	private static final int WIN_Y = 175;
	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;

	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_X, HEART_Y, heartsToDisplay);
		this.winImage = new WinImage(WIN_X, WIN_Y);
		this.gameOverImage = new GameOverImage();
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	public void removeHearts(int heartsRemaining) {
		int currentHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	public void addHearts(int heartsToAdd) {
		for(int i = 0; i < heartsToAdd; i++) {
			heartDisplay.addHeart();
		}
	}
}
