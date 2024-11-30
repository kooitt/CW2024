// Controller.java
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

/**
 * Controller manages the game's flow and navigation between levels and menus.
 */
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";
	private final Stage stage;
	private LevelParent currentLevel;
	private MainMenu mainMenu;
	private SettingsPage settingsPage;

	/**
	 * Constructs a Controller with the specified stage.
	 *
	 * @param stage the primary stage.
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Displays the main menu.
	 */
	public void showMainMenu() {
		if (mainMenu == null) mainMenu = new MainMenu(this);
		Scene scene = mainMenu.getScene();
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Launches the game by loading Level One.
	 */
	public void launchGame() {
		try {
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Displays the settings page.
	 */
	public void showSettings() {
		if (settingsPage == null) settingsPage = new SettingsPage(stage, this);
		Scene scene = settingsPage.getScene();
		stage.setScene(scene);
	}

	/**
	 * Exits the game.
	 */
	public void exitGame() {
		stage.close();
	}

	// 在 Controller 的 goToLevel 方法中
	private void goToLevel(String className) {
		try {
			if (currentLevel != null) {
				currentLevel.deleteObserver(this); // 移除当前观察者
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
				System.out.println("Cause: " + cause.getMessage());
			}
		} else {
			e.printStackTrace();
		}
		Alert alert = new Alert(AlertType.ERROR, e.getClass().toString());
		alert.show();
	}
}
