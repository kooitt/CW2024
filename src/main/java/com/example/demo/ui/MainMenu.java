package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Represents the main menu of the application, providing options to start the game, open the settings, or exit the game.
 */
public class MainMenu {

    /**
     * The controller that manages the application's main logic and state transitions.
     */
    private final Controller controller;

    /**
     * The root group that contains all the UI elements of the main menu.
     */
    private final Group root;

    /**
     * The scene associated with the main menu, displayed on the stage.
     */
    private final Scene scene;

    /**
     * Constructs the MainMenu with the given controller.
     *
     * @param controller the controller used to manage the main menu's actions and settings.
     */
    public MainMenu(Controller controller) {
        this.controller = controller;
        root = new Group();
        scene = new Scene(root, controller.getStage().getWidth(), controller.getStage().getHeight());
        SoundComponent.playMainmenuSound();
        initializeUI();
    }

    /**
     * Initializes the user interface for the main menu.
     * Sets the background image and creates buttons for starting the game, accessing settings, and exiting the game.
     */
    private void initializeUI() {
        // Set the background image
        ImageView bgView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/StartMenu.png")).toExternalForm()));
        bgView.setFitWidth(controller.getStage().getWidth());
        bgView.setFitHeight(controller.getStage().getHeight());
        bgView.setPreserveRatio(false);

        // Create buttons
        Button startButton = createButton("Start Game", controller::launchGame);
        Button settingsButton = createButton("Settings", () -> {
            // Show the settings page
            if (!root.getChildren().contains(controller.getSettingsPage().getRoot())) {
                root.getChildren().add(controller.getSettingsPage().getRoot());
            }
            controller.getSettingsPage().getRoot().setVisible(true);
            controller.getSettingsPage().setBackAction(() -> {
                controller.getSettingsPage().getRoot().setVisible(false);
                root.getChildren().remove(controller.getSettingsPage().getRoot());
            });
        });
        Button exitButton = createButton("Exit Game", controller::exitGame);

        // Position buttons at the center of the screen
        double centerX = controller.getStage().getWidth() / 2;
        double centerY = controller.getStage().getHeight() / 2;
        startButton.setLayoutX(centerX - 100);
        startButton.setLayoutY(centerY - 50);
        settingsButton.setLayoutX(centerX - 100);
        settingsButton.setLayoutY(centerY);
        exitButton.setLayoutX(centerX - 100);
        exitButton.setLayoutY(centerY + 50);

        // Add all elements to the root group
        root.getChildren().addAll(bgView, startButton, settingsButton, exitButton);
    }

    /**
     * Creates a styled button with specified text and action.
     *
     * @param text   the text to display on the button.
     * @param action the action to execute when the button is clicked.
     * @return the created Button instance.
     */
    private Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        String style = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;";
        String hoverStyle = "-fx-background-color: transparent; -fx-text-fill: yellow; -fx-font-size: 24px;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(style));
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Gets the scene associated with the main menu.
     *
     * @return the Scene instance representing the main menu.
     */
    public Scene getScene() {
        return scene;
    }
}
