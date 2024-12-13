package com.example.demo.levels;

import com.example.demo.ui.*;
import javafx.scene.Group;

public class LevelParentView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;

	private final Group root;
	private final HeartDisplay heartDisplay;

	public LevelParentView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void updateHeartCount(int heartsRemaining) {
		heartDisplay.removeHearts(heartsRemaining);
	}

	public void showWinScreen(){
		WinScreen.getInstance().showWinScreen();
	}

	public void showLoseScreen(){
		LoseScreen.getInstance().showLoseScreen();
	}
}
