package com.example.demo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.example.demo.levels.LevelParent;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * The Controller class manages transitions between levels and handles errors during gameplay.
 * It listens for property change events to trigger level transitions.
 */
public class Controller implements PropertyChangeListener {

    // Fully qualified class name for the first level of the game
    private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";

    // Primary stage of the application where scenes are displayed
    private final Stage stage;

    /**
     * Constructor for Controller.
     *
     * @param stage The primary stage of the application.
     */
    public Controller(Stage stage) {
        this.stage = stage; // Initialize the primary stage
    }

    /**
     * Launches the game by starting at Level One.
     * This method initializes the stage and loads the first level.
     */
    public void launchGame() {
        try {
            stage.show(); // Display the stage
            goToLevel(LEVEL_ONE_CLASS_NAME); // Load the first level
        } catch (Exception e) {
            handleUnexpectedError(e); // Handle any errors that occur
        }
    }

    /**
     * Transitions to a specified level using reflection.
     *
     * @param className The fully qualified class name of the level to load.
     */
    private void goToLevel(String className) {
        try {
            // Use reflection to dynamically load the level class
            Class<?> levelClass = Class.forName(className);

            // Get the constructor for the level class with specific parameters
            Constructor<?> constructor = levelClass.getConstructor(double.class, double.class, Stage.class);

            // Instantiate the level using the constructor
            LevelParent level = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), stage);

            // Register this controller as a property change listener for the level
            level.addPropertyChangeListener(this);

            // Initialize the scene for the level and set it on the stage
            Scene scene = level.initializeScene();
            stage.setScene(scene);

            // Start the game for the loaded level
            level.startGame();
        } catch (InvocationTargetException e) {
            handleLevelTransitionError(e.getCause()); // Handle specific level transition errors
        } catch (Exception e) {
            handleUnexpectedError(e); // Handle any other unexpected errors
        }
    }

    /**
     * Handles property change events for level transitions.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Check if the event corresponds to a level transition
        if ("levelTransition".equals(evt.getPropertyName())) {
            String nextLevel = (String) evt.getNewValue(); // Get the next level's class name
            try {
                goToLevel(nextLevel); // Transition to the next level
            } catch (Exception e) {
                handleUnexpectedError(e); // Handle any errors during transition
            }
        }
    }

    /**
     * Handles errors during level transitions.
     *
     * @param cause The cause of the error.
     */
    private void handleLevelTransitionError(Throwable cause) {
        // Log the error details to the console
        System.err.println("Error during level transition: " + cause);
        cause.printStackTrace();

        // Display an error alert to the user
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred during level transition.");
        alert.setContentText("Error: " + cause.getMessage());
        alert.show();
    }

    /**
     * Handles unexpected errors in the application.
     *
     * @param e The exception that occurred.
     */
    private void handleUnexpectedError(Exception e) {
        // Log the error details to the console
        System.err.println("Unexpected error during level transition: " + e);
        e.printStackTrace();

        // Display an error alert to the user
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected Error");
        alert.setHeaderText("An unexpected error occurred.");
        alert.setContentText("Error: " + e.getMessage());
        alert.show();
    }
}
