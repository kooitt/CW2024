package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;

/**
 * The Controller class is responsible for managing game levels and transitioning
 * between them based on updates it receives. It serves as an Observer that
 * listens for level changes and dynamically loads new levels.
 */
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;

	/**
	 * Constructs a Controller with the specified stage.
	 *
	 * @param stage The primary stage of the application where scenes are displayed.
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game by displaying the stage and loading the first level.
	 *
	 * @throws ClassNotFoundException if the specified level class cannot be found.
	 * @throws NoSuchMethodException if the specified constructor for the level class does not exist.
	 * @throws SecurityException if access to the level class or its constructor is restricted.
	 * @throws InstantiationException if the level class cannot be instantiated.
	 * @throws IllegalAccessException if access to the level class or its constructor is not allowed.
	 * @throws IllegalArgumentException if the constructor is invoked with inappropriate arguments.
	 * @throws InvocationTargetException if the constructor throws an exception.
	 */
	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Loads and transitions to the specified game level.
	 *
	 * @param className The fully qualified class name of the level to load.
	 * @throws ClassNotFoundException if the specified level class cannot be found.
	 * @throws NoSuchMethodException if the specified constructor for the level class does not exist.
	 * @throws SecurityException if access to the level class or its constructor is restricted.
	 * @throws InstantiationException if the level class cannot be instantiated.
	 * @throws IllegalAccessException if access to the level class or its constructor is not allowed.
	 * @throws IllegalArgumentException if the constructor is invoked with inappropriate arguments.
	 * @throws InvocationTargetException if the constructor throws an exception.
	 */
	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);
		myLevel.startGame();
	}

	/**
	 * Called when the observed level notifies of a change, indicating a level transition.
	 * Transitions to a new level based on the provided argument.
	 *
	 * @param arg0 The observable object; typically the current level.
	 * @param arg1 The argument passed by the observable, typically the next level's class name.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}
}
