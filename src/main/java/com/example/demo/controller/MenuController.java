package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ImageView backgroundImage;

    @FXML
    private VBox buttonContainer;

    @FXML
    private void initialize() {
        // Bind the ImageView size to the Pane size
        backgroundImage.fitHeightProperty().bind(((Pane) backgroundImage.getParent()).heightProperty());
        backgroundImage.fitWidthProperty().bind(((Pane) backgroundImage.getParent()).widthProperty());

        // Center the VBox in the Pane
        buttonContainer.layoutXProperty().bind(((Pane) buttonContainer.getParent()).widthProperty().subtract(buttonContainer.widthProperty()).divide(2));
        buttonContainer.layoutYProperty().bind(((Pane) buttonContainer.getParent()).heightProperty().subtract(buttonContainer.heightProperty()).divide(2));
    }
    @FXML
    private void startGame() {
            try {
                GameController gameController = new GameController(stage);
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