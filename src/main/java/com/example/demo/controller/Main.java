package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	@Override
	public void start(Stage stage) {
		// 设置窗口属性
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		// 创建 Controller
		myController = new Controller(stage);

		// 显示初始界面
		myController.showStartMenu();
	}

	public static void main(String[] args) {
		launch();
	}
}
