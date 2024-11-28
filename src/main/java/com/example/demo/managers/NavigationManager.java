package com.example.demo.managers;

import com.example.demo.config.GameConfig;
import com.example.demo.controller.GameController;
import com.example.demo.controller.GameOverController;
import com.example.demo.controller.MainMenuController;
import com.example.demo.controller.WinScreenController;
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
    private final double screenWidth = GameConfig.SCREEN_WIDTH;
    private final double screenHeight = GameConfig.SCREEN_HEIGHT;

    public NavigationManager(Scene currentScene, double screenWidth, double screenHeight) {
        this.currentScene = currentScene;
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

    public void goToMainMenu() {
        loadScreen("MenuScreen.fxml", MainMenuController.class);
    }

    public void showWinScreen() {
        loadScreen("WinScreen.fxml", WinScreenController.class);
    }

    public void showGameOverScreen(Class<?> levelClass) {
        GameOverController controller = loadScreen("GameOver.fxml", GameOverController.class);
        if (controller != null) {
            controller.setCurrentLevelClass(levelClass);
        }
    }

    private <T> T loadScreen(String fxmlName, Class<T> controllerType) {
        try {
            URL fxmlLocation = getClass().getClassLoader().getResource(fxmlName);
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            root.prefWidth(screenWidth);
            root.prefHeight(screenHeight);
            Scene scene = new Scene(root, screenWidth, screenHeight);

            Stage stage = (Stage) currentScene.getWindow();
            T controller = loader.getController();
            if (controller != null) {
                if (controller instanceof MainMenuController) {
                    ((MainMenuController) controller).setStage(stage);
                } else if (controller instanceof GameOverController) {
                    ((GameOverController) controller).setStage(stage);
                } else if (controller instanceof WinScreenController) {
                    ((WinScreenController) controller).setStage(stage);
                }
            }
            stage.setScene(scene);
            return controller;
        } catch (IOException e) {
            showErrorAlert(e);
            return null;
        }
    }

    private void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        alert.show();
        e.printStackTrace();
    }
}
