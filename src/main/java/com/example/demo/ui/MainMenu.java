package com.example.demo.ui;

import com.example.demo.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the main menu of the game.
 */
public class MainMenu {

    private Scene scene;
    private Controller controller;

    /**
     * Constructs a MainMenu with the specified controller.
     *
     * @param controller the game controller.
     */
    public MainMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        StackPane root = new StackPane();

        Image background = new Image(getClass().getResource("/com/example/demo/images/StartMenu.png").toExternalForm());
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);

        Button startBtn = createButton("Start Game", controller::launchGame);
        Button settingsBtn = createButton("Settings", controller::showSettings);
        Button exitBtn = createButton("Exit Game", controller::exitGame);

        menuBox.getChildren().addAll(startBtn, settingsBtn, exitBtn);
        root.getChildren().addAll(bgView, menuBox);
        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
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
