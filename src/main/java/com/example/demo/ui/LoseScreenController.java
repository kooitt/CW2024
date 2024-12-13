package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoseScreenController {
    @FXML
    private Button mainMenuButton;

    @FXML
    private Button playAgainButton;

    private Stage stage;
    private Controller controller;
    private MainMenu mainMenu;

    // Initialize method to pass stage and controller
    public void initialize(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mainMenu = new MainMenu();

        // Set button actions
        mainMenuButton.setOnAction(e -> goToMainMenu());
        playAgainButton.setOnAction(e -> restartGame());
    }

    // Logic for "Main Menu" button
    private void goToMainMenu() {
        Scene mainMenuScene = mainMenu.createMainMenu(stage, controller);
        stage.setScene(mainMenuScene); // Switch to main menu scene
    }

    // Logic for "Play Again" button
    private void restartGame() {
        try {
            controller.launchGame(); // Relaunch the game
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
