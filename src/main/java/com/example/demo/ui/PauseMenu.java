package com.example.demo.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The PauseMenu class represents the user interface displayed when the game is paused.
 * It provides options to resume the game, return to the main menu, or restart the current game level.
 */
public class PauseMenu {

    private final StackPane root; // Root container for the PauseMenu UI

    /**
     * Constructor for PauseMenu.
     *
     * @param stage        The Stage object representing the main game window.
     * @param resumeAction The action to be performed when the "RESUME" button is clicked.
     * @param menuAction   The action to be performed when the "MENU" button is clicked.
     * @param restartAction The action to be performed when the "RESTART" button is clicked.
     */
    public PauseMenu(Stage stage, Runnable resumeAction, Runnable menuAction, Runnable restartAction) {
        root = new StackPane();

        // Create a semi-transparent black background overlay
        Rectangle backgroundOverlay = new Rectangle(1300, 750);
        backgroundOverlay.setFill(Color.BLACK);
        backgroundOverlay.setOpacity(0.8);

        // Create the title text for the pause menu
        Text title = new Text("PAUSED");
        title.setFont(new Font("Arial", 50)); // Set font size and type
        title.setFill(Color.WHITE); // Set text color to white

        // Create the "RESUME" button and configure its style and action
        Button resumeButton = new Button("RESUME");
        resumeButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px;");
        resumeButton.setOnAction(e -> resumeAction.run()); // Bind the resume action to the button click

        // Create the "MENU" button and configure its style and action
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 20px;");
        menuButton.setOnAction(e -> menuAction.run()); // Bind the menu action to the button click

        // Create the "RESTART" button and configure its style and action
        Button restartButton = new Button("RESTART");
        restartButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        restartButton.setOnAction(e -> restartAction.run()); // Bind the restart action to the button click

        // Create a VBox layout to arrange the title and buttons vertically
        VBox vbox = new VBox(20, title, resumeButton, menuButton, restartButton);
        vbox.setAlignment(Pos.CENTER); // Center align all elements in the VBox

        // Add the background overlay and VBox to the root StackPane
        root.getChildren().addAll(backgroundOverlay, vbox);
    }

    /**
     * Returns the root node of the PauseMenu, which can be added to the game scene.
     *
     * @return The StackPane containing the PauseMenu UI components.
     */
    public StackPane getRoot() {
        return root;
    }
}
