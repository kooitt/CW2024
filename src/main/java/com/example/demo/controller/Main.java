// Main.java
package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for the Sky Battle application.
 * Initializes the main window and controller.
 */
public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	private Controller myController;

	@Override
	public void start(Stage stage) {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);
		myController = new Controller(stage);
		myController.showMainMenu();
	}

	public static void main(String[] args) {
		launch();
	}
}
