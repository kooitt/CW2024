package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class MainMenu {
    private final Controller controller;
    private final Group root;
    private final Scene scene;

    public MainMenu(Controller controller) {
        this.controller = controller;
        root = new Group();
        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
        SoundComponent.playMainmenuSound();
        initializeUI();
    }

    private void initializeUI() {
        // 设置背景图
        ImageView bgView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/StartMenu.png")).toExternalForm()));
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        Button startButton = createButton("Start Game", controller::launchGame);
        Button settingsButton = createButton("Settings", () -> {
            // 显示设置界面
            if (!root.getChildren().contains(controller.getSettingsPage().getRoot())) {
                root.getChildren().add(controller.getSettingsPage().getRoot());
            }
            controller.getSettingsPage().getRoot().setVisible(true);
            controller.getSettingsPage().setBackAction(() -> {
                controller.getSettingsPage().getRoot().setVisible(false);
                root.getChildren().remove(controller.getSettingsPage().getRoot());
            });
        });
        Button exitButton = createButton("Exit Game", controller::exitGame);
        double centerX = controller.getStage().getWidth() / 2;
        double centerY = controller.getStage().getHeight() / 2;
        startButton.setLayoutX(centerX - 100);
        startButton.setLayoutY(centerY - 50);
        settingsButton.setLayoutX(centerX - 100);
        settingsButton.setLayoutY(centerY);
        exitButton.setLayoutX(centerX - 100);
        exitButton.setLayoutY(centerY + 50);



        root.getChildren().addAll(bgView, startButton, settingsButton, exitButton);
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

    public Scene getScene() {
        return scene;
    }

}
