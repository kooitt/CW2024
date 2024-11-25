package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.utils.KeyBindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;

public class SettingsPage {

    private Scene scene;
    private Controller controller;
    private KeyBindings keyBindings;

    public SettingsPage(Controller controller) {
        this.controller = controller;
        this.keyBindings = KeyBindings.getInstance();
        initialize();
    }

    private void initialize() {
        StackPane root = new StackPane();

        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(controller.getStage().getWidth());
        backgroundImageView.setFitHeight(controller.getStage().getHeight());
        backgroundImageView.setPreserveRatio(false);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        backgroundImageView.setEffect(colorAdjust);

        VBox settingsBox = new VBox(15);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setPrefWidth(400);
        settingsBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20px;");

        Label upKeyLabel = new Label("Move Up Key:");
        upKeyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField upKeyField = createCustomTextField(keyBindings.getUpKey().getName());

        Label downKeyLabel = new Label("Move Down Key:");
        downKeyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField downKeyField = createCustomTextField(keyBindings.getDownKey().getName());

        Label fireKeyLabel = new Label("Fire Key:");
        fireKeyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField fireKeyField = createCustomTextField(keyBindings.getFireKey().getName());

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back to Main Menu");

        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: transparent;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 18px; -fx-border-color: transparent;";

        saveButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        setHoverEffect(saveButton, buttonStyle, hoverStyle);
        setHoverEffect(backButton, buttonStyle, hoverStyle);

        saveButton.setOnAction(e -> {
            keyBindings.setUpKey(KeyCode.getKeyCode(upKeyField.getText()));
            keyBindings.setDownKey(KeyCode.getKeyCode(downKeyField.getText()));
            keyBindings.setFireKey(KeyCode.getKeyCode(fireKeyField.getText()));
        });

        backButton.setOnAction(e -> controller.showMainMenu());

        settingsBox.getChildren().addAll(
                upKeyLabel, upKeyField,
                downKeyLabel, downKeyField,
                fireKeyLabel, fireKeyField,
                saveButton, backButton
        );

        root.getChildren().addAll(backgroundImageView, settingsBox);

        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
    }

    private TextField createCustomTextField(String text) {
        TextField textField = new TextField(text);
        textField.setEditable(false);
        textField.setPrefWidth(200);
        textField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white; -fx-font-size: 16px; -fx-border-color: transparent; -fx-alignment: center;");
        textField.setOnKeyPressed(e -> {
            e.consume();
            textField.setText(e.getCode().getName());
        });
        return textField;
    }

    private void setHoverEffect(Button button, String normalStyle, String hoverStyle) {
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    public Scene getScene() {
        return scene;
    }
}
