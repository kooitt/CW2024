package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu {

    private Scene scene;
    private Controller controller;

    public MainMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        StackPane root = new StackPane();

        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(controller.getStage().getWidth());
        backgroundImageView.setFitHeight(controller.getStage().getHeight());
        backgroundImageView.setPreserveRatio(false);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Button startGameButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button exitGameButton = new Button("Exit Game");

        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-border-color: transparent;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 24px; -fx-border-color: transparent;";

        startGameButton.setStyle(buttonStyle);
        settingsButton.setStyle(buttonStyle);
        exitGameButton.setStyle(buttonStyle);

        setHoverEffect(startGameButton, buttonStyle, hoverStyle);
        setHoverEffect(settingsButton, buttonStyle, hoverStyle);
        setHoverEffect(exitGameButton, buttonStyle, hoverStyle);

        startGameButton.setOnAction(e -> controller.launchGame());
        settingsButton.setOnAction(e -> controller.showSettings());
        exitGameButton.setOnAction(e -> controller.exitGame());

        menuBox.getChildren().addAll(startGameButton, settingsButton, exitGameButton);

        root.getChildren().addAll(backgroundImageView, menuBox);

        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
    }

    private void setHoverEffect(Button button, String normalStyle, String hoverStyle) {
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    public Scene getScene() {
        return scene;
    }
}
