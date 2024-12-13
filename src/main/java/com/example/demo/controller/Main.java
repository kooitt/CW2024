package com.example.demo.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.example.demo.ui.LoseScreen;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.WinScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	//comment
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		//set up stage
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		//initialize controller
		myController = new Controller(stage);

		WinScreen.getInstance().initialize(stage, myController);
		LoseScreen.getInstance().initialize(stage, myController);

		//create MainMenu and set it as initial scene
		MainMenu mainMenu = new MainMenu();
		Scene startMenuScene = mainMenu.createMainMenu(stage, myController);

		stage.setScene(startMenuScene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}