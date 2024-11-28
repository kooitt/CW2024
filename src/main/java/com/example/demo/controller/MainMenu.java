package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
    private final Stage stage;
    private Scene mainMenuScene;

    public MainMenu(Stage stage) {
        this.stage = stage;
    }

    public Scene createMainMenuScene(Controller controller) {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 50;");

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> {
            try {
                controller.launchGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> stage.close());

        menuLayout.getChildren().addAll(playButton, quitButton);

        mainMenuScene = new Scene(menuLayout, stage.getWidth(), stage.getHeight());
        return mainMenuScene;
    }
}
