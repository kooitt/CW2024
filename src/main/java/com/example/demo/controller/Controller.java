package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;

public class Controller implements Observer {

    private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
    private final Stage stage;

    public Controller(Stage stage) {
        this.stage = stage;
    }

    public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        stage.show();
        goToLevel(LEVEL_ONE_CLASS_NAME);
    }

    private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            // Use reflection to instantiate the level class
            Class<?> levelClass = Class.forName(className);
            Constructor<?> constructor = levelClass.getConstructor(double.class, double.class, Stage.class);
            LevelParent level = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), stage);

            // Set up observer for level transitions
            level.addObserver(this);

            // Initialize the level scene and start the game
            Scene scene = level.initializeScene();
            stage.setScene(scene);
            level.startGame();
        } catch (InvocationTargetException e) {
            // Handle errors caused by level transition
            handleLevelTransitionError(e.getCause());
        } catch (Exception e) {
            // Handle unexpected errors
            handleUnexpectedError(e);
        }
    }

    private void handleLevelTransitionError(Throwable cause) {
        System.err.println("Error during level transition: " + cause);
        cause.printStackTrace();

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred during level transition.");
        alert.setContentText("Error: " + cause.getMessage());
        alert.show(); // Display error alert
    }

    private void handleUnexpectedError(Exception e) {
        System.err.println("Unexpected error during level transition: " + e);
        e.printStackTrace();

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected Error");
        alert.setHeaderText("An unexpected error occurred.");
        alert.setContentText("Error: " + e.getMessage());
        alert.show(); // Display error alert
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            // Transition to the next level based on the argument
            if (arg instanceof String) {
                goToLevel((String) arg);
            } else {
                throw new IllegalArgumentException("Invalid level name passed to update: " + arg);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // Handle any errors during the level transition
            handleUnexpectedError(e);
        }
    }
}
