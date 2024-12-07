package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import com.example.demo.components.SoundComponent;

public class MainMenu {

    private StackPane root;
    private Controller controller;
    private SettingsPage settingsPage;

    public MainMenu(Controller controller) {
        this.controller = controller;
        this.settingsPage = new SettingsPage(controller);
        initialize();
        SoundComponent.playMainmenuSound();
    }

    private void initialize() {
        root = new StackPane();

        ImageView bgView = new ImageView(new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm()));
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        menuBox.getChildren().addAll(
            createButton("Start Game", controller::launchGame),
            createButton("Settings", this::showSettingsOverlay),
            createButton("Exit Game", controller::exitGame)
        );

        root.getChildren().addAll(bgView, menuBox);

        settingsPage.setBackAction(() -> root.getChildren().remove(settingsPage.getRoot()));
    }

    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        String style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 24px;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showSettingsOverlay() {
        if (!root.getChildren().contains(settingsPage.getRoot())) {
            root.getChildren().add(settingsPage.getRoot());
        }
    }

    public StackPane getRoot() {
        return root;
    }
}