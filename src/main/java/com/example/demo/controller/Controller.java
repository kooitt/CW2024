package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.SettingsPage;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";
	private final Stage stage;
	private LevelParent currentLevel;
	private MainMenu mainMenu;
	private SettingsPage settingsPage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void showMainMenu() {
		if (mainMenu == null) {
			mainMenu = new MainMenu(this);
		}
		Scene scene = mainMenu.getScene();
		stage.setScene(scene);
		stage.show();
	}

	public void launchGame() {
		try {
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void showSettings() {
		if (settingsPage == null) {
			settingsPage = new SettingsPage(stage, this);
		}
		Scene scene = settingsPage.getScene();
		stage.setScene(scene);
	}

	public void exitGame() {
		stage.close();
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
	public void update(java.util.Observable o, Object arg) {
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
