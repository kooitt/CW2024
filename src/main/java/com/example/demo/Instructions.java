package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The Instructions class represents the instructions screen of the game.
 * It provides the player with basic instructions on how to play the game.
 */
public class Instructions {

    private Stage primaryStage;

    /**
     * Constructs the instructions screen with the relevant text and a back button.
     *
     * @param stage The primary stage where the instructions will be displayed.
     */
    public Instructions(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Initializes the instructions UI with text and a back button.
     *
     * @return The scene containing the instructions UI.
     */
    public Scene createInstructionsScene() {
        VBox instructionsLayout = new VBox(20);
        instructionsLayout.setStyle("-fx-padding: 30; -fx-alignment: left;");

        // Title text for Instructions
        Text title = new Text("Game Instructions");
        title.setFont(Font.font("Arial", 40));
        title.setFill(Color.BLACK);

        // Mock instructions text
        Text instructionsText = new Text("1. Use the arrow keys to move your plane up and down.\n"
                + "2. Press spacebar to fire projectiles at enemy planes.\n"
                + "3. Avoid enemy projectiles and planes.\n"
                + "4. Defeat the boss to win the game!\n\n"
                + "Good luck and have fun!");
        instructionsText.setFont(Font.font("Arial", 18));
        instructionsText.setFill(Color.BLACK);

        // Back button to return to the main menu
        Button backButton = new Button("Back to Main Menu");
        backButton.setFont(Font.font("Arial", 16));
        backButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: rgba(0, 0, 0, 1); " +
                "-fx-text-fill: yellow;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-text-fill: white;"));
        backButton.setOnAction(e -> goBackToMainMenu());

        // Add all components to the layout
        instructionsLayout.getChildren().addAll(title, instructionsText, backButton);

        // Set the background image for the instructions scene
        setBackground(instructionsLayout);

        // Return the scene with instructions layout
        return new Scene(instructionsLayout, 1300, 750);
    }

    /**
     * Sets the background image for the layout.
     *
     * @param layout The layout to set the background on.
     */
    private void setBackground(VBox layout) {
        // Set the same background image as in the MainMenu
        String backgroundPath = "/com/example/demo/images/background2.jpg";
        layout.setStyle("-fx-background-image: url('" + getClass().getResource(backgroundPath).toExternalForm() + "'); "
                + "-fx-background-size: cover;");
    }

    /**
     * Goes back to the main menu scene.
     */
    private void goBackToMainMenu() {
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setScene(mainMenu.createMenuScene());
    }
}
