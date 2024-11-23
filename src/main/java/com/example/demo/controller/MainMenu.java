package com.example.demo.controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        // Start button
        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        startButton.setOnAction(e -> {
            try {
                // Launch the game
                Controller controller = new Controller(primaryStage);
                controller.launchGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // "How To Play" button
        Button howToPlayButton = new Button("HOW TO PLAY");
        howToPlayButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 20px;");
        howToPlayButton.setOnAction(e -> {
            // Open the How To Play window
            new HowToPlayWindow().display();
        });

        // Quit button
        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        quitButton.setOnAction(e -> primaryStage.close()); // Close the application

        // Layout (VBox for buttons)
        VBox vbox = new VBox(20, titleText, startButton, howToPlayButton, quitButton);
        vbox.setAlignment(Pos.CENTER); // Center align the VBox

        // StackPane to layer the background and VBox
        StackPane root = new StackPane(backgroundView, vbox);

        // Scene setup
        Scene scene = new Scene(root, 1300, 750);

        // Stage setup
        primaryStage.setTitle("Space Battle");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Prevent resizing
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
