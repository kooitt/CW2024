package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.utils.KeyBindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SettingsPage {

    private Scene scene;
    private Stage stage;
    private Controller controller;
    private KeyBindings keyBindings;

    public SettingsPage(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        keyBindings = KeyBindings.getInstance();
        initializeScene();
    }

    private void initializeScene() {
        StackPane root = new StackPane();

        // 设置与 MainMenu 相同的背景图像
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);

        // 创建设置内容
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Settings");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");

        // 创建按键设置部分
        VBox keyBindingsBox = new VBox(10);
        keyBindingsBox.setAlignment(Pos.CENTER);

        // 上移键
        HBox upKeyBox = createKeySettingBox("Up Key:", keyBindings.getUpKey(), keyBindings::setUpKey);
        // 下移键
        HBox downKeyBox = createKeySettingBox("Down Key:", keyBindings.getDownKey(), keyBindings::setDownKey);
        // 左移键
        HBox leftKeyBox = createKeySettingBox("Left Key:", keyBindings.getLeftKey(), keyBindings::setLeftKey);
        // 右移键
        HBox rightKeyBox = createKeySettingBox("Right Key:", keyBindings.getRightKey(), keyBindings::setRightKey);

        keyBindingsBox.getChildren().addAll(upKeyBox, downKeyBox, leftKeyBox, rightKeyBox);

        // 返回主菜单按钮
        Button backButton = new Button("Back to Main Menu");

        // 设置与 MainMenu 相同的按钮样式
        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-border-color: transparent;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 24px; -fx-border-color: transparent;";

        backButton.setStyle(buttonStyle);
        setHoverEffect(backButton, buttonStyle, hoverStyle);

        backButton.setOnAction(e -> {
            controller.showMainMenu();
        });

        contentBox.getChildren().addAll(titleLabel, keyBindingsBox, backButton);

        root.getChildren().addAll(backgroundImageView, contentBox);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    private HBox createKeySettingBox(String labelText, KeyCode currentKey, KeySetter keySetter) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button keyButton = new Button(currentKey.getName());
        keyButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;");

        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 18px; -fx-border-color: yellow;";

        setHoverEffect(keyButton, buttonStyle, hoverStyle);

        keyButton.setOnAction(e -> {
            Stage keyStage = new Stage();
            Label instructionLabel = new Label("Press any key to set " + labelText);
            VBox vbox = new VBox(10, instructionLabel);
            vbox.setAlignment(Pos.CENTER);
            Scene keyScene = new Scene(vbox, 300, 100);
            keyScene.setOnKeyPressed(event -> {
                KeyCode newKey = event.getCode();
                keySetter.setKey(newKey);
                keyButton.setText(newKey.getName());
                keyStage.close();
            });
            keyStage.setScene(keyScene);
            keyStage.show();
        });

        HBox hbox = new HBox(10, label, keyButton);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    // 添加与 MainMenu 相同的悬停效果方法
    private void setHoverEffect(Button button, String normalStyle, String hoverStyle) {
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    // 定义一个函数式接口，用于设置按键
    @FunctionalInterface
    private interface KeySetter {
        void setKey(KeyCode keyCode);
    }

    public Scene getScene() {
        return scene;
    }
}
