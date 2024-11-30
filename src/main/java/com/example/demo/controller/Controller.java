package com.example.demo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;

/**
 * The Controller class manages transitions between levels and handles errors during gameplay.
 * It listens for property changes to trigger level transitions.
 */
public class Controller implements PropertyChangeListener {

    private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
    private final Stage stage;

    /**
     * Constructor for Controller.
     *
     * @param stage The primary stage of the application.
     */
    public Controller(Stage stage) {
        this.stage = stage;
    }

    /**
     * Launches the game by starting at Level One.
     */
    public void launchGame() {
        try {
            stage.show();
            goToLevel(LEVEL_ONE_CLASS_NAME);
        } catch (Exception e) {
            handleUnexpectedError(e);
        }
    }

    /**
     * Transitions to a specified level using reflection.
     *
     * @param className The fully qualified class name of the level to load.
     */
    private void goToLevel(String className) {
        try {
            // Use reflection to instantiate the level class
            Class<?> levelClass = Class.forName(className);
            Constructor<?> constructor = levelClass.getConstructor(double.class, double.class, Stage.class);
            LevelParent level = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), stage);

            // Add this controller as a property change listener for level transitions
            level.addPropertyChangeListener(this);

            // Initialize the level scene and start the game
            Scene scene = level.initializeScene();
            stage.setScene(scene);
            level.startGame();
        } catch (InvocationTargetException e) {
            handleLevelTransitionError(e.getCause());
        } catch (Exception e) {
            handleUnexpectedError(e);
        }
    }

    /**
     * Handles property change events for level transitions.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("levelTransition".equals(evt.getPropertyName())) {
            String nextLevel = (String) evt.getNewValue();
            try {
                goToLevel(nextLevel);
            } catch (Exception e) {
                handleUnexpectedError(e);
            }
        }
    }

    /**
     * Handles errors during level transitions.
     *
     * @param cause The cause of the error.
     */
    private void handleLevelTransitionError(Throwable cause) {
        System.err.println("Error during level transition: " + cause);
        cause.printStackTrace();

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
        System.err.println("Unexpected error during level transition: " + e);
        e.printStackTrace();

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected Error");
        alert.setHeaderText("An unexpected error occurred.");
        alert.setContentText("Error: " + e.getMessage());
        alert.show();
    }
}
