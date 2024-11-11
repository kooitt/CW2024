package com.example.demo;

import javafx.scene.Group;

public class LevelView {
	
	private static final double HEART_DISPLAY_X_POSITION = 550;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final double KILL_COUNT_X_POSITION = 1200;
	private static final double KILL_COUNT_Y_POSITION = 25;
	private static final int GAME_IMAGE_X_POSITION = 370;
	private static final int GAME_IMAGE_Y_POSITION = 175;
	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;
	private final KillCountDisplay killCountDisplay;
	
	public LevelView(Group root, int heartsToDisplay, int maxKills) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.killCountDisplay = new KillCountDisplay(KILL_COUNT_X_POSITION, KILL_COUNT_Y_POSITION, maxKills);
		this.winImage = new WinImage(GAME_IMAGE_X_POSITION, GAME_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(GAME_IMAGE_X_POSITION, GAME_IMAGE_Y_POSITION);
	}
	
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showKillCountDisplay() {
		root.getChildren().add(killCountDisplay.getContainer());
	}

	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}
	
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}
	
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	public void updateKillCount(int kills) {
		killCountDisplay.updateKillCount(kills);
	}

}
