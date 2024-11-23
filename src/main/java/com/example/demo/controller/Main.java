package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;
    private static final String TITLE = "Sky Battle";

    @Override
    public void start(Stage primaryStage) {
        // Set the primary stage properties
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);

        // Launch the MainMenu
        MainMenu mainMenu = new MainMenu();
        try {
            mainMenu.start(primaryStage); // Show the main menu interface
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions
        }
    }

    public static void main(String[] args) {
        launch(); // Start the JavaFX application lifecycle
    }
    
  
}
