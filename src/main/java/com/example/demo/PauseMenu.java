package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PauseMenu {

    private final StackPane root;

    public PauseMenu(Stage stage, Runnable resumeAction, Runnable menuAction, Runnable quitAction) {
        root = new StackPane();

        // Background overlay
        Rectangle backgroundOverlay = new Rectangle(1300, 750);
        backgroundOverlay.setFill(Color.BLACK);
        backgroundOverlay.setOpacity(0.8);

        // Title text
        Text title = new Text("PAUSED");
        title.setFont(new Font("Arial", 50));
        title.setFill(Color.WHITE);

        // Resume button
        Button resumeButton = new Button("RESUME");
        resumeButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        resumeButton.setOnAction(e -> resumeAction.run());

        // Menu button
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 20px;");
        menuButton.setOnAction(e -> menuAction.run()); // Navigate back to MainMenu

        // Quit button
        Button quitButton = new Button("QUIT");
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        quitButton.setOnAction(e -> quitAction.run());

        // VBox layout for buttons
        VBox vbox = new VBox(20, title, resumeButton, menuButton, quitButton);
        vbox.setAlignment(Pos.CENTER);

        // Add components to root StackPane
        root.getChildren().addAll(backgroundOverlay, vbox);
    }

    /**
     * Returns the root node for the pause menu.
     *
     * @return The StackPane containing the pause menu UI.
     */
    public StackPane getRoot() {
        return root;
    }
}
