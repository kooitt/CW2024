package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Objects;

public class MainMenu {
    private final Stage stage;
    private Scene mainMenuScene;

    public MainMenu(Stage stage) {
        this.stage = stage;
    }

    public Scene createMainMenuScene(Controller controller) {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 50;");

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/background1.jpg")).toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        menuLayout.setBackground(new Background(bgImage));

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> controller.showLevelSelectionMenu());

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> stage.close());

        menuLayout.getChildren().addAll(playButton, quitButton);

        mainMenuScene = new Scene(menuLayout, stage.getWidth(), stage.getHeight());
        return mainMenuScene;
    }
}
