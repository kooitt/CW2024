package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private Button quitButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button startButton;

    @FXML
    private ImageView mainMenuBg;

    private Stage stage;
    private Controller controller;

    public void initialize(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;

        //mainMenuBg.setImage(new Image(getClass().getResource("/path/to/your/image.png").toExternalForm()));

        // Attach button actions
        startButton.setOnAction(e -> startGame());
        settingsButton.setOnAction(e -> openSettings());
        quitButton.setOnAction(e -> System.exit(0));
    }

    private void startGame() {
        try {
            controller.launchGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSettings() {
        System.out.println("open settings");
        try {
            // Load the SettingsMenu FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ui/SettingsMenu.fxml"));
            Parent root = loader.load();

            Stage settingsStage = new Stage();  // Create a new Stage for the pop-up
            settingsStage.setScene(new Scene(root));
            // Use SettingsMenu to display the pop-up window
            SettingsMenu settingsMenu = new SettingsMenu();
            settingsMenu.createSettingsScene(stage);

            // Show the pop-up window
            //settingsStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

