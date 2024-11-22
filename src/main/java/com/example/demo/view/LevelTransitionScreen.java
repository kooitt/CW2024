package com.example.demo.view;

import com.example.demo.config.GameConfig;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents the screen displayed during level transitions.
 */
public class LevelTransitionScreen {

    private final Stage stage;
    private final String levelName;
    private final Runnable onTransitionComplete;

    /**
     * Constructs a LevelTransitionScreen with the specified stage, level name, and transition completion handler.
     *
     * @param stage the stage on which the transition screen is displayed.
     * @param levelName the name of the level to display.
     * @param onTransitionComplete the handler to run when the transition is complete.
     */
    public LevelTransitionScreen(Stage stage, String levelName, Runnable onTransitionComplete) {
        this.stage = stage;
        this.levelName = levelName;
        this.onTransitionComplete = onTransitionComplete;
    }

    /**
     * Displays the level transition screen with a fade-in and fade-out effect.
     */
    public void show() {
        StackPane root = new StackPane();
        Text levelText = new Text(levelName);

        // Customize the font
        levelText.setFont(Font.font("Arcade", FontWeight.BOLD, 48)); // Adjust the font size and style as needed
        levelText.setStyle("-fx-fill: yellow; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 10, 0.5, 0, 0);");

        root.getChildren().add(levelText);
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        stage.setScene(scene);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> onTransitionComplete.run());
            fadeOut.play();
        });
        fadeIn.play();
    }
}