package com.example.demo.levels;

import com.example.demo.controller.Controller;
import com.example.demo.ui.*;
import javafx.scene.Group;
import javafx.stage.Stage;

public class LevelParentView {
	
	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -55;
	private static final int LOSS_SCREEN_Y_POSITION = -335;

	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;
	//private final WinScreen winScreen;
	
	public LevelParentView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSITION);
		//this.winScreen = winScreen;
	}
	
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void updateHeartCount(int heartsRemaining) {
		heartDisplay.removeHearts(heartsRemaining);
	}

//	public void showWinImage() {
//		root.getChildren().add(winImage);
//		winImage.showWinImage();
//	}

	public void showWinScreen(){
		WinScreen.getInstance().showWinScreen();
	}

	
//	public void showGameOverImage() {
//		root.getChildren().add(gameOverImage);
//	}

	public void showLoseScreen(){
		LoseScreen.getInstance().showLoseScreen();
	}
}
