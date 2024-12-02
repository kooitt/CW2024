package com.example.demo.controller;

import com.example.demo.ui.MainMenu;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the Sky Battle game.
 * It initializes the application window and launches the main menu.
 */
public class Main extends Application {

    // Constants for the application window
    private static final int SCREEN_WIDTH = 1300; // Width of the application window
    private static final int SCREEN_HEIGHT = 750; // Height of the application window
    private static final String TITLE = "Sky Battle"; // Title of the application window

    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the primary stage and displays the main menu.
     *
     * @param primaryStage The main stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Set the properties of the primary stage
        primaryStage.setTitle(TITLE); // Set the window title
        primaryStage.setResizable(false); // Disable window resizing
        primaryStage.setHeight(SCREEN_HEIGHT); // Set the window height
        primaryStage.setWidth(SCREEN_WIDTH); // Set the window width

        // Launch the MainMenu
        MainMenu mainMenu = new MainMenu(); // Create an instance of the main menu
        try {
            mainMenu.start(primaryStage); // Start the main menu interface
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur during launch
        }
    }

    /**
     * The main method is the entry point for the application.
     * It launches the JavaFX application lifecycle.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(); // Start the JavaFX application
    }
}
