package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu {

    private Scene scene;
    private Controller controller;

    public MainMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Button startGameButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button exitGameButton = new Button("Exit Game");

        startGameButton.setOnAction(e -> controller.launchGame());
        settingsButton.setOnAction(e -> controller.showSettings());
        exitGameButton.setOnAction(e -> controller.exitGame());

        root.getChildren().addAll(startGameButton, settingsButton, exitGameButton);

        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
