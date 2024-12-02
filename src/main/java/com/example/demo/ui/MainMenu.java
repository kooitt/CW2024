// MainMenu.java
package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu {

    private StackPane root; // 使用 StackPane 作为根布局
    private Controller controller;

    public MainMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        root = new StackPane();

        // 加载背景图
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView bgView = new ImageView(backgroundImage);
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        // 创建按钮
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Button startBtn = createButton("Start Game", controller::launchGame);
        Button settingsBtn = createButton("Settings", controller::showSettings);
        Button exitBtn = createButton("Exit Game", controller::exitGame);

        menuBox.getChildren().addAll(startBtn, settingsBtn, exitBtn);

        // 将背景图和按钮添加到 StackPane
        root.getChildren().addAll(bgView, menuBox);
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

    public StackPane getRoot() {
        return root;
    }
}
