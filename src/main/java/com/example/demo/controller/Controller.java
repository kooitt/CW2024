// Controller.java
package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;
import java.util.Observable;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
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
	private StackPane rootPane; // 使用 StackPane 来包含 MainMenu 和 SettingsPage

	/**
	 * Constructs a Controller with the specified stage.
	 *
	 * @param stage the primary stage.
	 */
	public Controller(Stage stage) {
		this.stage = stage;
		initialize();
	}

	private void initialize() {
		rootPane = new StackPane();

		// 初始化 MainMenu 和 SettingsPage
		mainMenu = new MainMenu(this);
		settingsPage = new SettingsPage(this);

		// 将 MainMenu 和 SettingsPage 添加到 StackPane 中
		rootPane.getChildren().addAll(mainMenu.getRoot(), settingsPage.getRoot());

		// 初始时显示 MainMenu，隐藏 SettingsPage
		mainMenu.getRoot().setVisible(true);
		settingsPage.getRoot().setVisible(false);

		// 创建一个单一的 Scene
		Scene scene = new Scene(rootPane, stage.getWidth(), stage.getHeight());

		stage.setScene(scene);
		stage.show();
	}

	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Displays the main menu by showing MainMenu pane and hiding SettingsPage pane.
	 */
	public void showMainMenu() {
		mainMenu.getRoot().setVisible(true);
		settingsPage.getRoot().setVisible(false);
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
	 * Displays the settings page by showing SettingsPage pane and hiding MainMenu pane.
	 */
	public void showSettings() {
		mainMenu.getRoot().setVisible(false);
		settingsPage.getRoot().setVisible(true);
	}

	/**
	 * Exits the game.
	 */
	public void exitGame() {
		stage.close();
	}

	private void goToLevel(String className) {
		try {
			if (currentLevel != null) currentLevel.cleanUp();
			Class<?> myClass = Class.forName(className);
			Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
			currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
			currentLevel.addObserver(this);
			Scene levelScene = currentLevel.initializeScene();
			stage.setScene(levelScene);
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
				System.out.println("Cause: " + cause.getMessage());
			}
		} else {
			e.printStackTrace();
		}
		Alert alert = new Alert(AlertType.ERROR, e.getClass().toString());
		alert.show();
	}
}
