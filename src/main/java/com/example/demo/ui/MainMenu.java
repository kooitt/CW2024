package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
    public Scene createMainMenu(Stage stage, Controller controller) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ui/MainMenu.fxml"));
            Parent root = loader.load();

            // Access controller to set actions or initialize behavior
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.initialize(stage, controller);

            // Return the scene created from the FXML layout
            return new Scene(root);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load MainMenu.fxml", e);
        }
    }
}