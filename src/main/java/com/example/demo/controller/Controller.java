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
    Class<?> levelClass = Class.forName(className);
    Constructor<?> constructor = levelClass.getConstructor(double.class, double.class);
    LevelParent level = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());

    level.addObserver(this);

    Scene scene = level.initializeScene();
    stage.setScene(scene);
    level.startGame();
} catch (InvocationTargetException e) {
    Throwable cause = e.getCause();
    System.err.println("Error during level transition: " + cause);
    cause.printStackTrace();

    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("An error occurred during level transition.");
    alert.setContentText("Error: " + cause.getMessage());
    alert.show(); // Use show() instead of showAndWait()
} catch (Exception e) {
    System.err.println("Unexpected error during level transition: " + e);
    e.printStackTrace();

    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Unexpected Error");
    alert.setHeaderText("An unexpected error occurred.");
    alert.setContentText("Error: " + e.getMessage());
    alert.show(); // Use show() instead of showAndWait()
}
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
            // Log and display any error during the transition
            System.err.println("Error while transitioning to the next level: " + e);
            e.printStackTrace();

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Level Transition Error");
            alert.setHeaderText("An error occurred while transitioning to the next level.");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
