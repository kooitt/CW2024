package com.example.demo.controller;

import com.example.demo.managers.LeaderboardManager;
import com.example.demo.managers.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LeaderboardController {
    private Stage stage;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Pane scoreBackground;

    @FXML
    private VBox scoresContainer;

    @FXML
    private VBox scoresList;
    private SoundManager soundManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        soundManager = SoundManager.getInstance();
        // Bind background image dimensions
        backgroundImage.fitHeightProperty().bind(((Pane) backgroundImage.getParent()).heightProperty());
        backgroundImage.fitWidthProperty().bind(((Pane) backgroundImage.getParent()).widthProperty());

        // Center the scores container
        scoresContainer.layoutXProperty().bind(((Pane) scoresContainer.getParent()).widthProperty()
                .subtract(scoresContainer.widthProperty()).divide(2));
        scoresContainer.layoutYProperty().bind(((Pane) scoresContainer.getParent()).heightProperty()
                .subtract(scoresContainer.heightProperty()).divide(2));

        // Add padding to scoresContainer
        scoresContainer.setPadding(new Insets(20, 40, 20, 40));

        // Make scoreBackground match scoresContainer size with extra padding
        scoreBackground.minWidthProperty().bind(scoresContainer.widthProperty().add(80));
        scoreBackground.minHeightProperty().bind(scoresContainer.heightProperty().add(40));
        scoreBackground.maxWidthProperty().bind(scoresContainer.widthProperty().add(80));
        scoreBackground.maxHeightProperty().bind(scoresContainer.heightProperty().add(40));

        // Center the scoreBackground
        scoreBackground.layoutXProperty().bind(((Pane) scoreBackground.getParent()).widthProperty()
                .subtract(scoreBackground.widthProperty()).divide(2));
        scoreBackground.layoutYProperty().bind(((Pane) scoreBackground.getParent()).heightProperty()
                .subtract(scoreBackground.heightProperty()).divide(2));
        // Load and display scores
        displayScores();
    }

    private void displayScores() {
        List<Integer> scores = LeaderboardManager.getTopScores();
        scores.sort(Collections.reverseOrder());
        scoresList.getChildren().clear();

        int maxScores = Math.min(scores.size(), 5);
        for (int i = 0; i < maxScores; i++) {
            Label scoreLabel = new Label((i + 1) + ". " + scores.get(i) + " kills");
            scoreLabel.setTextFill(Color.BLACK);
            scoreLabel.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");
            scoresList.getChildren().add(scoreLabel);
        }

        // If no scores exist, show a message
        if (scores.isEmpty()) {
            Label noScoresLabel = new Label("No scores yet!");
            noScoresLabel.setTextFill(Color.BLACK);
            noScoresLabel.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");
            scoresList.getChildren().add(noScoresLabel);
        }
    }

    @FXML
    private void backToMenu() {
        try {
            soundManager.stopAllBackgroundMusic();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MenuScreen.fxml"));
            Scene menuScene = new Scene(loader.load());
            MainMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(menuScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}