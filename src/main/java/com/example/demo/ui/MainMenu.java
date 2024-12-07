// MainMenu.java
package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.demo.components.SoundComponent;

// MainMenu.java 部分修改
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

        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView bgView = new ImageView(backgroundImage);
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Button startBtn = createButton("Start Game", controller::launchGame);
        Button settingsBtn = createButton("Settings", this::showSettingsOverlay);
        Button exitBtn = createButton("Exit Game", controller::exitGame);

        menuBox.getChildren().addAll(startBtn, settingsBtn, exitBtn);

        root.getChildren().addAll(bgView, menuBox);

        // 设置返回操作（当在主菜单下显示settings时，back返回关闭settings界面）
        settingsPage.setBackAction(() -> {
            if (root.getChildren().contains(settingsPage.getRoot())) {
                root.getChildren().remove(settingsPage.getRoot());
            }
        });
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

