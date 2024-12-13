package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.example.demo.levels.LevelParentController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.util.Observer;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";
	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() {
		try{
			stage.show();
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			handleException(e);
		}
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);
		LevelParentController levelController = new LevelParentController(myLevel, myLevel.getLevelView());
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);
		myLevel.startGame();

	}

	@Override
	public void update(String levelName) {
		try {
			goToLevel(levelName);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An error occurred");
		alert.setContentText(e.getClass() + ": " + e.getMessage());
		alert.show();
		e.printStackTrace();
	}

}
