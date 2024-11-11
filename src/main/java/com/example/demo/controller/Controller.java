package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import com.example.demo.LevelParent;
import com.example.demo.UserPlane;
import com.example.demo.UserProjectile;

/**
 * Controller class for managing the game flow, including user input and level transitions.
 * Observes the LevelParent to handle game level updates.
 */
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;
	private UserPlane userPlane;

	/**
	 * Constructs the Controller with the primary game stage.
	 *
	 * @param stage the primary game stage
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game by starting the first level.
	 *
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {

		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Transitions to the specified level by loading its class dynamically.
	 *
	 * @param className the name of the level class to load
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {

		// Dynamically load the level class
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);

		// Set up the game scene
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);

		// Retrieve the UserPlane instance from LevelParent
		userPlane = myLevel.getUser();

		// Set up key press and release event handlers
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP) {
				userPlane.moveUp();
			} else if (event.getCode() == KeyCode.DOWN) {
				userPlane.moveDown();
			} else if (event.getCode() == KeyCode.SPACE) {
				// Fire a projectile when the space bar is pressed
				UserProjectile projectile = (UserProjectile) userPlane.fireProjectile();
				myLevel.addProjectile(projectile); // Add projectile to level for display and updating
			}
		});

		scene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
				userPlane.stop();
			}
		});

		myLevel.startGame();
	}

	/**
	 * Observer update method, used to transition to the next level when notified.
	 *
	 * @param observable the observable object
	 * @param arg        the argument passed from the observable
	 */
	@Override
	public void update(Observable observable, Object arg) {
		try {
			goToLevel((String) arg);
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
				 | InstantiationException | IllegalAccessException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}
}
