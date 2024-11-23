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

public class HowToPlayWindow {

    public void display() {
        // Create a new stage
        Stage howToPlayStage = new Stage();
        howToPlayStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main menu
        howToPlayStage.setTitle("How to Play");

        // Title text
        Text title = new Text("Key Stroke Summary");
        title.setFont(new Font("Arial", 30));
        title.setStyle("-fx-fill: white;");

        // Key stroke summary
        Text summary = new Text(
            "P: Pause the game\n" +
            "UP Arrow: Move the plane up\n" +
            "DOWN Arrow: Move the plane down\n" +
            "SPACE: Fire a projectile\n" +
            "Release UP/DOWN: Stop the plane movement"
        );
        summary.setFont(new Font("Arial", 20));
        summary.setStyle("-fx-fill: white;");

        // Close button
        Button closeButton = new Button("CLOSE");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");
        closeButton.setOnAction(e -> howToPlayStage.close());

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(title);
        layout.setCenter(summary);
        layout.setBottom(closeButton);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(closeButton, Pos.CENTER);

        // Background
        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");

        Scene scene = new Scene(root, 600, 400);

        howToPlayStage.setScene(scene);
        howToPlayStage.show();
    }
}
