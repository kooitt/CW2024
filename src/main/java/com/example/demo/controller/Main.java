package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point of the application, setting up the
 * main stage and initiating the game through the Controller class.
 */
public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	/**
	 * Initializes and displays the main game window, setting its title, size, and
	 * other properties. It then creates a Controller instance and starts the game.
	 *
	 * @param stage The primary stage for this application, where the main scene will be set.
	 * @throws ClassNotFoundException if the initial level class cannot be found.
	 * @throws NoSuchMethodException if the specified constructor for the initial level class does not exist.
	 * @throws SecurityException if access to the level class or its constructor is restricted.
	 * @throws InstantiationException if the initial level class cannot be instantiated.
	 * @throws IllegalAccessException if access to the level class or its constructor is not allowed.
	 * @throws IllegalArgumentException if the constructor is invoked with inappropriate arguments.
	 * @throws InvocationTargetException if the constructor of the initial level class throws an exception.
	 */
	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);
		myController = new Controller(stage);
		myController.launchGame();
	}

	/**
	 * The main entry point of the application. It launches the JavaFX application
	 * lifecycle, which will eventually call the start method.
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch();
	}
}
