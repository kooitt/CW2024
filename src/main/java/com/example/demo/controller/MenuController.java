package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void startGame() {
            try {
                Controller gameController = new Controller(stage);
                gameController.launchGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML
    private void exitGame() {
        stage.close();
    }
}