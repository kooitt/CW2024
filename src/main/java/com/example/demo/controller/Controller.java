package com.example.demo.controller;

import com.example.demo.LevelParent;
import com.example.demo.LevelOne; // 显式引用 LevelOne
import com.example.demo.LevelTwo; // 显式引用 LevelTwo
import com.example.demo.StartMenu; // 引入 StartMenu 类
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Controller {

	private static final Logger logger = Logger.getLogger(Controller.class.getName());
	private final Stage stage;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	// 显示初始菜单界面
	public void showStartMenu() {
		StartMenu startMenu = new StartMenu(stage);
		startMenu.showMenu(this);
	}

	// 启动游戏
	public void launchGame() {
		try {
			stage.show(); // 显示主窗口
			goToLevelOne(); // 启动第一关
		} catch (Exception e) {
			showError("Failed to launch game", e);
		}
	}

	// 加载第一关
	private void goToLevelOne() {
		try {
			LevelOne levelOne = new LevelOne(stage.getHeight(), stage.getWidth());
			loadLevel(levelOne);
		} catch (Exception e) {
			showError("Failed to load Level One", e);
		}
	}

	// 加载第二关
	private void goToLevelTwo() {
		try {
			LevelTwo levelTwo = new LevelTwo(stage.getHeight(), stage.getWidth());
			loadLevel(levelTwo);
		} catch (Exception e) {
			showError("Failed to load Level Two", e);
		}
	}

	// 通用加载关卡逻辑
	private void loadLevel(LevelParent level) {
		try {
			Scene scene = level.initializeScene();
			stage.setScene(scene);
			level.startGame();
		} catch (Exception e) {
			showError("Failed to load level: " + level.getClass().getSimpleName(), e);
		}
	}

	// 显示错误信息
	private void showError(String message, Exception e) {
		logger.severe(message + ": " + e.getMessage());
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message + "\n" + e.getClass().getSimpleName() + ": " + e.getMessage());
		alert.showAndWait();
	}
}
