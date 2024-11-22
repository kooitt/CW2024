package com.example.demo.handlers;

import com.example.demo.controller.GameController;
import com.example.demo.controller.MainMenuController;
import com.example.demo.levels.LevelParent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;

public class NavigationManager {
    private final Scene currentScene;
    private final double screenWidth;
    private final double screenHeight;

    public NavigationManager(Scene currentScene, double screenWidth, double screenHeight) {
        this.currentScene = currentScene;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void goToMainMenu() {
        try {
            URL fxmlLocation = getClass().getClassLoader().getResource("MenuScreen.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent menuRoot = loader.load();
            Scene menuScene = new Scene(menuRoot, screenWidth, screenHeight);

            // Get stage from current scene
            Stage stage = (Stage) currentScene.getWindow();
            MainMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(menuScene);
        } catch (IOException e) {
            showErrorAlert(e);
        }
    }

    public void restartLevel(Class<?> currentLevelClass) {
        try {
            Constructor<?> constructor = currentLevelClass.getConstructor();
            LevelParent newLevel = (LevelParent) constructor.newInstance();

            // Get stage from current scene
            Stage stage = (Stage) currentScene.getWindow();

            newLevel.nextLevelProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    String[] levelInfo = newValue.split(",");
                    GameController gameController = new GameController(stage);
                    gameController.goToLevel(levelInfo[0], levelInfo[1]);
                } catch (Exception e) {
                    showErrorAlert(e);
                }
            });

            Scene newScene = newLevel.initializeScene();
            stage.setScene(newScene);
            newLevel.startGame();
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    private void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        alert.show();
        e.printStackTrace();
    }
}
