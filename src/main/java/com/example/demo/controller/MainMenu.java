package com.example.demo.controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Background image
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/menu_background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1300); // Match the window width
        backgroundView.setFitHeight(750); // Match the window height

        // Title text
        Text titleText = new Text("SPACE BATTLE");
        titleText.setFont(new Font("Arial", 50));
        titleText.setStyle("-fx-fill: white;");

        // Buttons
        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        startButton.setOnAction(e -> {
            try {
                // Launch the game using the Controller
                Controller controller = new Controller(primaryStage);
                controller.launchGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        quitButton.setOnAction(e -> primaryStage.close()); // Exit the game

        // VBox to arrange the title and buttons
        VBox vbox = new VBox(20); // Spacing of 20 pixels between elements
        vbox.getChildren().addAll(titleText, startButton, quitButton);
        vbox.setAlignment(Pos.TOP_CENTER); // Align elements horizontally centered
        vbox.setTranslateY(350); // Move everything downward

        // StackPane to layer the background and VBox
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, vbox);

        // Create the scene
        Scene scene = new Scene(root, 1300, 750);

        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
