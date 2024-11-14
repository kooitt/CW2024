package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The MainMenu class represents the main menu of the game.
 * It provides buttons for starting the game, viewing instructions, and quitting.
 */
public class MainMenu {

    private Stage primaryStage;

    // Image path for the background image used across other classes
    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/background2.jpg";

    /**
     * Constructs the main menu with buttons for the user to choose an option.
     *
     * @param stage The primary stage where the main menu will be displayed.
     */
    public MainMenu(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Initializes the main menu UI with buttons and their respective actions.
     *
     * @return The scene containing the main menu UI.
     */
    public Scene createMenuScene() {
        // Create a VBox layout for the menu with some padding and spacing
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-padding: 30; -fx-alignment: center;");

        // Set up the title
        Text title = new Text("Welcome to the Game!");
        title.setFont(Font.font("Arial", 40));
        title.setFill(Color.WHITE); // White text color to stand out against background

        // Set up buttons with styling
        Button startButton = createStyledButton("Start Game");
        Button instructionsButton = createStyledButton("Instructions");
        Button quitButton = createStyledButton("Quit");

        // Add action handlers for buttons
        startButton.setOnAction(e -> startGame());
        instructionsButton.setOnAction(e -> showInstructions());
        quitButton.setOnAction(e -> quitGame());

        // Add all components to the layout
        menuLayout.getChildren().addAll(title, startButton, instructionsButton, quitButton);

        // Set the background image
        setBackground(menuLayout);

        // Return the scene with the menu layout
        return new Scene(menuLayout, 600, 400);
    }

    /**
     * Creates a styled button with hover effects.
     *
     * @param text The text to display on the button.
     * @return The styled button.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 16));
        button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(0, 0, 0, 1); " +
                "-fx-text-fill: yellow;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-text-fill: white;"));
        return button;
    }

    /**
     * Sets the background image for the layout.
     *
     * @param layout The layout to set the background on.
     */
    private void setBackground(VBox layout) {
        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_PATH).toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                null, BackgroundSize.DEFAULT);
        layout.setBackground(new Background(background));
    }

    private void startGame() {
        GameController gameController = new GameController(primaryStage);
        gameController.startGame();
    }

    private void showInstructions() {
        Instructions instructions = new Instructions(primaryStage);
        primaryStage.setScene(instructions.createInstructionsScene());
    }

    private void quitGame() {
        // Create the confirmation alert
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Quit Game");
        quitAlert.setHeaderText("Are you sure you want to quit?");
        quitAlert.setContentText("Your progress will be lost.");

        // Show the alert and wait for user's response
        quitAlert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                primaryStage.close();  // Close the application
            }
        });
    }
}
