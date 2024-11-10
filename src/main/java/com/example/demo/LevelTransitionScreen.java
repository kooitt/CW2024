package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelTransitionScreen {

    private final Stage stage;
    private final String levelName;
    private final Runnable onTransitionComplete;

    public LevelTransitionScreen(Stage stage, String levelName, Runnable onTransitionComplete) {
        this.stage = stage;
        this.levelName = levelName;
        this.onTransitionComplete = onTransitionComplete;
    }

    public void show() {
        StackPane root = new StackPane();
        Text levelText = new Text(levelName);

        // Customize the font
        levelText.setFont(Font.font("Arcade", FontWeight.BOLD, 48)); // Adjust the font size and style as needed
        levelText.setStyle("-fx-fill: yellow; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 10, 0.5, 0, 0);");

        root.getChildren().add(levelText);
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
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