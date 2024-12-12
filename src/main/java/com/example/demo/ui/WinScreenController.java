package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WinScreenController {
    @FXML
    private Button mainMenuButton;

    @FXML
    private Button restartButton;

    private Stage stage;
    private Controller controller;
    private Scene mainMenuScene;

    // Initialize method to pass stage and controller
    public void initialize(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mainMenuScene = mainMenuScene;

        // Set button actions
        mainMenuButton.setOnAction(e -> goToMainMenu());
        restartButton.setOnAction(e -> restartGame());
    }

    // Logic for "Main Menu" button
    private void goToMainMenu() {
        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.createMainMenu(stage, controller);
        stage.setScene(mainMenuScene); // Switch to main menu scene
    }

    // Logic for "Restart" button
    private void restartGame() {
        try {
            controller.launchGame(); // Relaunch the game
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

