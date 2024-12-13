package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoseScreen {
    private static LoseScreen instance;
    private Stage stage;
    private Controller controller;

    private LoseScreen() {}

    public static LoseScreen getInstance() {
        if (instance == null) {
            instance = new LoseScreen();
        }
        return instance;
    }

    public void initialize(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void showLoseScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ui/LoseScreen.fxml"));
            Parent root = loader.load();

            // Get the LoseScreenController and initialize it
            LoseScreenController loseScreenController = loader.getController();
            loseScreenController.initialize(stage, controller); // Pass the stage and controller

            Scene loseScreenScene = new Scene(root);
            stage.setScene(loseScreenScene); // Switch to the lose screen scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
