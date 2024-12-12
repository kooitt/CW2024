package com.example.demo.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	private static final String TITLE = "Sky Battle";

	@Override
	public void start(Stage stage) throws IOException {

		// Initialize the main menu
		MainMenuController mainMenuController = new MainMenuController();
		mainMenuController.showMainMenu(stage);

		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}