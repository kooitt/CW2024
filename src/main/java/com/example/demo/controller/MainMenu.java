package com.example.demo.controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends Application {

    // MediaPlayer instance for background music
    private MediaPlayer backgroundMusicPlayer;

    @Override
    public void start(Stage primaryStage) {
        // Set up background image
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/menu_background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1300); // Match window width
        backgroundView.setFitHeight(750); // Match window height

        // Title text
        Text titleText = new Text("SPACE BATTLE");
        titleText.setFont(new Font("Arial", 50)); // Set font size and style
        titleText.setStyle("-fx-fill: white;"); // White text color

        // Initialize background music
        Media backgroundMusic = new Media(getClass().getResource("/com/example/demo/sounds/menu_background.wav").toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the background music
        backgroundMusicPlayer.play(); // Start playing

        // Start button
        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        startButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            backgroundMusicPlayer.stop(); // Stop the background music
            try {
                // Launch the game
                Controller controller = new Controller(primaryStage);
                controller.launchGame();
            } catch (Exception ex) {
                ex.printStackTrace(); // Print any exceptions to console
            }
        });

        // How To Play button
        Button howToPlayButton = new Button("HOW TO PLAY");
        howToPlayButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 20px;");
        howToPlayButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            new HowToPlayWindow().display(); // Show the How To Play window
        });

        // Quit button
        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        quitButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            backgroundMusicPlayer.stop(); // Stop the background music
            primaryStage.close(); // Close the application
        });

        // VBox layout for buttons
        VBox vbox = new VBox(20, titleText, startButton, howToPlayButton, quitButton); // Spacing of 20px
        vbox.setAlignment(Pos.CENTER); // Center align the buttons

        // StackPane to layer background image and VBox
        StackPane root = new StackPane(backgroundView, vbox);

        // Create a Scene
        Scene scene = new Scene(root, 1300, 750);

        // Configure Stage (Window)
        primaryStage.setTitle("Space Battle");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Prevent resizing of the window
        primaryStage.show(); // Show the stage
    }

    // Method to play button click sound
    private void playButtonClickSound() {
        try {
            Media clickSound = new Media(getClass().getResource("/com/example/demo/sounds/button_click.wav").toExternalForm());
            MediaPlayer clickPlayer = new MediaPlayer(clickSound);
            clickPlayer.play(); // Play the button click sound
        } catch (Exception ex) {
            ex.printStackTrace(); // Print any exceptions to console
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args); // Start JavaFX application
    }
}
