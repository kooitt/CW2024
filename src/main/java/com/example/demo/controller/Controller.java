package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.SettingsPage;

/**
 * The {@code Controller} class serves as the central hub for managing the
 * game's stages, levels, and transitions between different scenes such as the
 * main menu, game levels, and settings page. It coordinates the flow of the
 * application and provides methods to initialize, clean up, and navigate between scenes.
 */
public class Controller {
    /**
     * The primary JavaFX {@code Stage} used to display the current scene.
     */
    private Stage stage;

    /**
     * The current game level being played, represented as a {@code LevelParent} object.
     * It holds the active level and manages its lifecycle.
     */
    private LevelParent currentLevel;

    /**
     * The main menu scene, represented as a {@code MainMenu} object.
     * This is the entry point to the game.
     */
    private MainMenu mainMenu;

    /**
     * The settings page scene, represented as a {@code SettingsPage} object.
     * This allows the user to adjust game settings.
     */
    private SettingsPage settingsPage;

    /**
     * Constructs a {@code Controller} instance and initializes the application
     * with the main menu displayed by default.
     *
     * @param stage the primary stage used to display scenes
     */
    public Controller(Stage stage) {
        this.stage = stage;
        settingsPage = new SettingsPage(this);

        // Initialize the application with the main menu
        showMainMenu();
    }

    /**
     * Returns the settings page of the application.
     *
     * @return the {@code SettingsPage} object
     */
    public SettingsPage getSettingsPage() {
        return settingsPage;
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary {@code Stage} object
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Displays the main menu scene. A new {@code MainMenu} object is created
     * each time this method is called, ensuring a fresh scene.
     */
    public void showMainMenu() {
        mainMenu = new MainMenu(this);
        stage.setScene(mainMenu.getScene());
        stage.show();
    }

    /**
     * Starts the game by transitioning to the first level.
     */
    public void launchGame() {
        goToLevel("com.example.demo.levels.LevelOne");
    }

    /**
     * Transitions to a specified game level.
     * Dynamically loads the class corresponding to the level and initializes it.
     *
     * @param className the fully qualified name of the level class
     */
    private void goToLevel(String className) {
        try {
            if (currentLevel != null) {
                // Clean up the current level before transitioning
                currentLevel.cleanUp();
                currentLevel = null;
            }

            // Dynamically load the level class and create an instance
            Class<?> myClass = Class.forName(className);
            currentLevel = (LevelParent) myClass.getConstructor(double.class, double.class, Controller.class)
                    .newInstance(stage.getHeight(), stage.getWidth(), this);

            // Set up level-specific configurations
            currentLevel.setSettingsPageForPause(this.getSettingsPage());
            currentLevel.addLevelChangeListener(nextLevelName -> goToLevel(nextLevelName));

            // Initialize and start the new level
            Scene levelScene = currentLevel.initializeScene();
            stage.setScene(levelScene);
            currentLevel.startGame();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Returns to the main menu from the current level.
     * Ensures the current level is cleaned up properly.
     */
    public void returnToMainMenu() {
        if (currentLevel != null) {
            currentLevel.cleanUp();
            currentLevel = null;
        }
        showMainMenu();
    }

    /**
     * Closes the application by exiting the primary stage.
     */
    public void exitGame() {
        stage.close();
    }

    /**
     * Handles exceptions that may occur during runtime.
     * Currently, it prints the stack trace of the exception.
     *
     * @param e the exception to handle
     */
    private void handleException(Exception e) {
        e.printStackTrace();
    }
}
