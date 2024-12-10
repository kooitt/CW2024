package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.level.LevelParent;
import com.example.demo.controller.TransitionScreen;

import java.lang.reflect.Constructor;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.util.Duration;

public class Controller {
	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.level.LevelOne";
	private static final String LEVEL_TWO_CLASS_NAME = "com.example.demo.level.LevelTwo";
	private static final String LEVEL_THREE_CLASS_NAME = "com.example.demo.level.LevelThree"; // 新增第三关类名
	private final Stage stage;
	private boolean isPaused = false;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() {
		try {
			goToLevelWithTransition(LEVEL_ONE_CLASS_NAME);
		} catch (Exception e) {
			showError(e);
		}
		stage.show();
	}

	private void goToLevelWithTransition(String className) {
		// 动态设置加载界面显示的文字
		String levelText = "Loading...";
		if (className.equals(LEVEL_ONE_CLASS_NAME)) {
			levelText = "Level 1";
		} else if (className.equals(LEVEL_TWO_CLASS_NAME)) {
			levelText = "Level 2";
		} else if (className.equals(LEVEL_THREE_CLASS_NAME)) { // 新增对第三关的支持
			levelText = "Level 3";
		}

		// 使用 TransitionScreen 处理加载动画和过渡
		Scene transitionScene = new TransitionScreen(stage.getWidth(), stage.getHeight(), levelText, () -> {
			try {
				// 动态加载关卡类
				Class<?> myClass = Class.forName(className);
				Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
				LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
				myLevel.setOnLevelChange(this::goToLevelWithTransition); // 设置关卡切换回调

				// 初始化关卡场景
				Scene gameScene = myLevel.initializeScene();

				// 为新场景应用淡入动画
				Group root = (Group) gameScene.getRoot();
				root.setOpacity(0);

				FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
				fadeIn.setFromValue(0);
				fadeIn.setToValue(1);
				fadeIn.setOnFinished(event -> myLevel.startGame());

				// 切换到关卡场景
				stage.setScene(gameScene);
				fadeIn.play();
			} catch (Exception e) {
				showError(e);
			}
		}).getScene();

		stage.setScene(transitionScene);
	}

	private void showError(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An error occurred");
		alert.setContentText(e.getMessage());
		alert.show();
	}

	public void togglePause() {
		isPaused = !isPaused;
	}

	public boolean isPaused() {
		return isPaused;
	}
}
