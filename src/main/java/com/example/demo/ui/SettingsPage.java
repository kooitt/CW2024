// SettingsPage.java
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

    private StackPane root; // 使用 StackPane 作为根布局
    private Controller controller;
    private KeyBindings keyBindings;

    public SettingsPage(Controller controller) {
        this.controller = controller;
        this.keyBindings = KeyBindings.getInstance();
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

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        Label title = new Label("Settings");
        title.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");

        VBox keyBox = new VBox(10);
        keyBox.setAlignment(Pos.CENTER);

        keyBox.getChildren().addAll(
                createKeySetting("Up Key:", keyBindings.getUpKey(), keyBindings::setUpKey),
                createKeySetting("Down Key:", keyBindings.getDownKey(), keyBindings::setDownKey),
                createKeySetting("Left Key:", keyBindings.getLeftKey(), keyBindings::setLeftKey),
                createKeySetting("Right Key:", keyBindings.getRightKey(), keyBindings::setRightKey)
        );

        Button backBtn = createButton("Back to Main Menu", controller::showMainMenu);

        contentBox.getChildren().addAll(title, keyBox, backBtn);

        // 将背景图和内容添加到 StackPane
        root.getChildren().addAll(bgView, contentBox);
    }

    private HBox createKeySetting(String labelText, KeyCode currentKey, java.util.function.Consumer<KeyCode> setter) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button keyBtn = new Button(currentKey.getName());
        keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;");

        keyBtn.setOnMouseEntered(e -> keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 18px; -fx-border-color: yellow;"));
        keyBtn.setOnMouseExited(e -> keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;"));

        keyBtn.setOnAction(e -> {
            Stage keyStage = new Stage();
            Label instruction = new Label("Press any key to set " + labelText);
            VBox vbox = new VBox(instruction);
            vbox.setAlignment(Pos.CENTER);
            Scene keyScene = new Scene(vbox, 300, 100);
            keyScene.setOnKeyPressed(event -> {
                KeyCode newKey = event.getCode();
                setter.accept(newKey);
                keyBtn.setText(newKey.getName());
                keyStage.close();
            });
            keyStage.setScene(keyScene);
            keyStage.show();
        });

        HBox hbox = new HBox(10, label, keyBtn);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
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
