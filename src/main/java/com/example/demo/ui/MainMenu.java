package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.controller.HowToPlayWindow;

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

/**
 * MainMenu is the starting screen of the application.
 * It provides options to start the game, view instructions, or quit the application.
 */
public class MainMenu extends Application {

    // MediaPlayer instance for playing background music
    private MediaPlayer backgroundMusicPlayer;

    /**
     * Initializes and displays the main menu.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Set up background image for the menu
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/menu_background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1300); // Match window width
        backgroundView.setFitHeight(750); // Match window height

        // Create and style the title text
        Text titleText = new Text("SPACE BATTLE");
        titleText.setFont(new Font("Arial", 50)); // Set font size and style
        titleText.setStyle("-fx-fill: white;"); // Set text color to white

        // Initialize and play the background music
        Media backgroundMusic = new Media(getClass().getResource("/com/example/demo/sounds/menu_background.wav").toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the background music
        backgroundMusicPlayer.play(); // Start playing the music

        // Create the START button and set its action
        Button startButton = new Button("START");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        startButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            backgroundMusicPlayer.stop(); // Stop the background music
            try {
                // Launch the game using the Controller
                Controller controller = new Controller(primaryStage);
                controller.launchGame();
            } catch (Exception ex) {
                ex.printStackTrace(); // Print any exceptions to the console
            }
        });

        // Create the HOW TO PLAY button and set its action
        Button howToPlayButton = new Button("HOW TO PLAY");
        howToPlayButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 20px;");
        howToPlayButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            new HowToPlayWindow().display(); // Show the instructions window
        });

        // Create the QUIT button and set its action
        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        quitButton.setOnAction(e -> {
            playButtonClickSound(); // Play button click sound
            backgroundMusicPlayer.stop(); // Stop the background music
            primaryStage.close(); // Close the application
        });

        // Create a VBox layout for arranging buttons and title text vertically
        VBox vbox = new VBox(20, titleText, startButton, howToPlayButton, quitButton); // Spacing of 20px
        vbox.setAlignment(Pos.CENTER); // Center align the VBox contents

        // Create a StackPane to layer the background image and VBox
        StackPane root = new StackPane(backgroundView, vbox);

        // Create the Scene with the root node
        Scene scene = new Scene(root, 1300, 750);

        // Configure the primary Stage (window)
        primaryStage.setTitle("Space Battle"); // Set window title
        primaryStage.setScene(scene);          // Set the scene to the stage
        primaryStage.setResizable(false);      // Prevent resizing the window
        primaryStage.show();                   // Show the stage
    }

    /**
     * Plays a sound effect when a button is clicked.
     */
    private void playButtonClickSound() {
        try {
            // Load and play the button click sound
            Media clickSound = new Media(getClass().getResource("/com/example/demo/sounds/button_click.wav").toExternalForm());
            MediaPlayer clickPlayer = new MediaPlayer(clickSound);
            clickPlayer.play();
        } catch (Exception ex) {
            ex.printStackTrace(); // Print any exceptions to the console
        }
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }
}
