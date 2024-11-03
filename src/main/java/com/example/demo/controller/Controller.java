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
import javafx.animation.AnimationTimer;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;
	private LevelParent currentLevel;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() {
		stage.show();
		try {
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void goToLevel(String className) {
		try {
			if (currentLevel != null) {
				currentLevel.cleanUp();
			}
			Class<?> myClass = Class.forName(className);
			Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
			currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
			currentLevel.addObserver(this);
			Scene scene = currentLevel.initializeScene();
			stage.setScene(scene);
			currentLevel.startGame();


		} catch (Exception e) {
			handleException(e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			goToLevel((String) arg);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				System.out.println("Cause of InvocationTargetException: " + cause.getMessage());
			}
		} else {
			e.printStackTrace();
		}
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(e.getClass().toString());
		alert.show();
	}
}