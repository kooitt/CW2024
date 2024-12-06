package com.example.demo.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	private static final int SCREEN_HEIGHT = 750;
	private static final int SCREEN_WIDTH= 1300;
	private static final String TITLE = "Sky Battle";
	private static final String MAIN_MENU_FXML = "/fxml/mainmenu.fxml";

    @Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_FXML));
		Parent root = loader.load();
		System.out.println("Main menu loaded!");
		MainMenuController mainMenuController = loader.getController();
		mainMenuController.initialize(stage);

		Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
		stage.setScene(scene);
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);
		stage.show();
        //Controller myController = new Controller(stage);
		//myController.launchGame();
	}

  	public static void main(String[] args) {
		launch();
	}
}