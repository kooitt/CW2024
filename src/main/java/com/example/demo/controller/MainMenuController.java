package com.example.demo.controller;

import com.example.demo.managers.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller class for the main menu of the application.
 */
public class MainMenuController {

    private Stage stage;
    private SoundManager soundManager;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private VBox buttonContainer;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the primary stage for this application.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic("menu");
        // Bind the ImageView size to the Pane size
        backgroundImage.fitHeightProperty().bind(((Pane) backgroundImage.getParent()).heightProperty());
        backgroundImage.fitWidthProperty().bind(((Pane) backgroundImage.getParent()).widthProperty());

        // Center the VBox in the Pane
        buttonContainer.layoutXProperty().bind(((Pane) buttonContainer.getParent()).widthProperty().subtract(buttonContainer.widthProperty()).divide(2));
        buttonContainer.layoutYProperty().bind(((Pane) buttonContainer.getParent()).heightProperty().subtract(buttonContainer.heightProperty()).divide(2));
    }

    /**
     * Starts the story mode game by creating a new GameController and launching the game.
     */
    @FXML
    private void startGame() {
        try {
            soundManager.stopAllBackgroundMusic();
            GameController gameController = new GameController(stage);
            gameController.launchGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the arcade mode game.
     */
    @FXML
    private void startArcadeMode() {
        try {
            soundManager.stopAllBackgroundMusic();
           ArcadeModeController arcadeModeController = new ArcadeModeController(stage);
           arcadeModeController.launchArcadeMode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the leaderboard screen.
     */
    @FXML
    private void showLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LeaderboardScreen.fxml"));
            Scene leaderboardScene = new Scene(loader.load());
            LeaderboardController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(leaderboardScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the game by closing the stage.
     */
    @FXML
    private void exitGame() {
        stage.close();
    }
}