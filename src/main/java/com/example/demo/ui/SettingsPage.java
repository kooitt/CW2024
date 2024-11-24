package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.utils.KeyBindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;

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
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label upKeyLabel = new Label("Move Up Key:");
        TextField upKeyField = new TextField(keyBindings.getUpKey().getName());
        upKeyField.setEditable(false);
        upKeyField.setOnKeyPressed(e -> {
            e.consume();
            upKeyField.setText(e.getCode().getName());
        });

        Label downKeyLabel = new Label("Move Down Key:");
        TextField downKeyField = new TextField(keyBindings.getDownKey().getName());
        downKeyField.setEditable(false);
        downKeyField.setOnKeyPressed(e -> {
            e.consume();
            downKeyField.setText(e.getCode().getName());
        });

        Label fireKeyLabel = new Label("Fire Key:");
        TextField fireKeyField = new TextField(keyBindings.getFireKey().getName());
        fireKeyField.setEditable(false);
        fireKeyField.setOnKeyPressed(e -> {
            e.consume();
            fireKeyField.setText(e.getCode().getName());
        });

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back to Main Menu");

        saveButton.setOnAction(e -> {
            keyBindings.setUpKey(KeyCode.getKeyCode(upKeyField.getText()));
            keyBindings.setDownKey(KeyCode.getKeyCode(downKeyField.getText()));
            keyBindings.setFireKey(KeyCode.getKeyCode(fireKeyField.getText()));
        });

        backButton.setOnAction(e -> controller.showMainMenu());

        root.getChildren().addAll(
                upKeyLabel, upKeyField,
                downKeyLabel, downKeyField,
                fireKeyLabel, fireKeyField,
                saveButton, backButton
        );

        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
