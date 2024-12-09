package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point for the Sky Battle application.
 * This class extends the JavaFX {@link Application} and initializes the game settings and the main controller.
 */
public class Main extends Application {

	/**
	 * The width of the application window in pixels.
	 */
	private static final int SCREEN_WIDTH = 1300;

	/**
	 * The height of the application window in pixels.
	 */
	private static final int SCREEN_HEIGHT = 750;

	/**
	 * The title displayed on the application window.
	 */
	private static final String TITLE = "Sky Battle";

	/**
	 * The main entry point for the JavaFX application.
	 * This method sets the stage's title, size, and initializes the main {@link Controller}.
	 *
	 * @param stage the primary stage for this application, onto which the application scene is set.
	 */
	@Override
	public void start(Stage stage) {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);
		new Controller(stage);
	}

	/**
	 * The main method of the application.
	 * This method launches the JavaFX application by invoking {@link Application#launch(String...)}.
	 *
	 * @param args the command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch();
	}
}
