package com.example.demo.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The HowToPlayWindow class represents a pop-up window displaying instructions
 * on how to play the game. It provides a summary of key controls.
 */
public class HowToPlayWindow {

    /**
     * Displays the "How to Play" window.
     * The window includes a key stroke summary and a close button.
     */
    public void display() {
        // Create a new stage for the "How to Play" window
        Stage howToPlayStage = new Stage();
        
        // Block interaction with the main menu while this window is open
        howToPlayStage.initModality(Modality.APPLICATION_MODAL);
        howToPlayStage.setTitle("How to Play");

        // Title text for the window
        Text title = new Text("Key Stroke Summary");
        title.setFont(new Font("Arial", 30)); // Set font and size for the title
        title.setStyle("-fx-fill: white;"); // Set the text color to white

        // Key stroke summary text with instructions for controls
        Text summary = new Text(
            "P: Pause the game\n" +
            "UP Arrow: Move the plane up\n" +
            "DOWN Arrow: Move the plane down\n" +
            "SPACE: Fire a projectile\n" +
            "Release UP/DOWN: Stop the plane movement"
        );
        summary.setFont(new Font("Arial", 20)); // Set font and size for the summary
        summary.setStyle("-fx-fill: white;"); // Set the text color to white

        // Close button to exit the "How to Play" window
        Button closeButton = new Button("CLOSE");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;"); // Style the button
        closeButton.setOnAction(e -> howToPlayStage.close()); // Close the window when the button is clicked

        // Layout for the window using a BorderPane
        BorderPane layout = new BorderPane();
        layout.setTop(title); // Add the title to the top
        layout.setCenter(summary); // Add the summary text to the center
        layout.setBottom(closeButton); // Add the close button to the bottom

        // Center-align the title and close button in their respective positions
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(closeButton, Pos.CENTER);

        // Create a root layout with a background style
        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);"); // Gradient background

        // Create a scene with the specified width and height
        Scene scene = new Scene(root, 600, 400);

        // Set the scene for the stage and display the window
        howToPlayStage.setScene(scene);
        howToPlayStage.show();
    }
}
