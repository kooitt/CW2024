package com.example.demo.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsMenu {
    public Scene createSettingsScene(Stage stage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ui/SettingsMenu.fxml"));
            Parent root = loader.load();

            // Create a new stage for the pop-up
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(new Scene(root));
            settingsStage.setResizable(false); // Optional: Prevent resizing of the pop-up window

            // Show the settings pop-up
            settingsStage.show();

            // Return the scene created from the FXML layout
            return new Scene(root);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load MainMenu.fxml", e);
        }
    }
}
