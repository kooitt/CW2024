package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WinScreen {
    private static WinScreen instance;
    private Stage stage;
    private Controller controller;

    private WinScreen() {}

    public static WinScreen getInstance() {
        if (instance == null) {
            instance = new WinScreen();
        }
        return instance;
    }

    public void initialize(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void showWinScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ui/WinScreen.fxml"));
            Parent root = loader.load();

            // Get the WinScreenController and initialize it
            WinScreenController winScreenController = loader.getController();
            winScreenController.initialize(stage, controller); // Pass the stage and controller

            Scene winScreenScene = new Scene(root);
            stage.setScene(winScreenScene); // Switch to the win screen scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
