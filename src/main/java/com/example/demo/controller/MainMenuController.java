package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class MainMenuController {

    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;
        // Any additional initialization logic
    }

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button controlsButton;

    // Define actions for buttons
    @FXML
    private void onStartButtonClicked (ActionEvent event) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("Start button clicked!");
        Controller myController = new Controller(stage);
        myController.launchGame();
    }

    @FXML
    private void onSettingsButtonClicked(ActionEvent event) {
        System.out.println("Settings button clicked!");
        // Logic to show settings
    }

    @FXML
    private void onControlsButtonClicked(ActionEvent event) {
        System.out.println("Controls button clicked!");
        // Logic to show controls
    }
}