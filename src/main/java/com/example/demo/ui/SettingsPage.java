package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.utils.KeyBindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsPage {
    private StackPane root;
    private Controller controller;
    private KeyBindings keyBindings;
    private Runnable backAction;
    private Button upKeyBtn, downKeyBtn, leftKeyBtn, rightKeyBtn;

    public SettingsPage(Controller controller) {
        this.controller = controller;
        this.keyBindings = KeyBindings.getInstance();
        initialize();
    }

    private void initialize() {
        root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.75);");
        root.setPrefSize(controller.getStage().getWidth(), controller.getStage().getHeight());

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        Label title = new Label("Settings");
        title.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");

        VBox keyBox = new VBox(10);
        keyBox.setAlignment(Pos.CENTER);

        keyBox.getChildren().addAll(
                createKeySetting("Up Key:", keyBindings.getUpKey(), keyBindings::setUpKey, btn -> upKeyBtn = btn),
                createKeySetting("Down Key:", keyBindings.getDownKey(), keyBindings::setDownKey, btn -> downKeyBtn = btn),
                createKeySetting("Left Key:", keyBindings.getLeftKey(), keyBindings::setLeftKey, btn -> leftKeyBtn = btn),
                createKeySetting("Right Key:", keyBindings.getRightKey(), keyBindings::setRightKey, btn -> rightKeyBtn = btn)
        );

        Button backBtn = createStyledButton("Back", () -> {
            if (backAction != null) backAction.run();
        });

        Button returnToMainMenuBtn = createStyledButton("Return to Main Menu", controller::returnToMainMenu);

        contentBox.getChildren().addAll(title, keyBox, backBtn, returnToMainMenuBtn);
        root.getChildren().add(contentBox);
    }

    public void refresh() {
        upKeyBtn.setText(keyBindings.getUpKey().getName());
        downKeyBtn.setText(keyBindings.getDownKey().getName());
        leftKeyBtn.setText(keyBindings.getLeftKey().getName());
        rightKeyBtn.setText(keyBindings.getRightKey().getName());
    }

    private HBox createKeySetting(String labelText, KeyCode currentKey, java.util.function.Consumer<KeyCode> setter, java.util.function.Consumer<Button> buttonReference) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        Button keyBtn = new Button(currentKey.getName());
        keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;");
        keyBtn.setOnMouseEntered(e -> keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 18px; -fx-border-color: yellow;"));
        keyBtn.setOnMouseExited(e -> keyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-color: white;"));

        buttonReference.accept(keyBtn);

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

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        String style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2;";
        String hoverStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 24px; -fx-border-color: white; -fx-border-width: 2;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> action.run());
        return button;
    }

    public StackPane getRoot() {
        return root;
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }
}