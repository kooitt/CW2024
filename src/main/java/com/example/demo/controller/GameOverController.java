package com.example.demo.controller;

import com.example.demo.config.GameConfig;
import com.example.demo.managers.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameOverController {
    @FXML
    private StackPane gameOverRoot;
    private Stage stage;
    private Class<?> currentLevelClass;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentLevelClass(Class<?> levelClass) {
        this.currentLevelClass = levelClass;
    }

    public double getScreenWidth() {
        return GameConfig.SCREEN_WIDTH;
    }

    public double getScreenHeight() {
        return GameConfig.SCREEN_HEIGHT;
    }

    @FXML
    public void initialize() {
        // Make sure the root pane fills the entire screen
        gameOverRoot.setPrefWidth(GameConfig.SCREEN_WIDTH);
        gameOverRoot.setPrefHeight(GameConfig.SCREEN_HEIGHT);
    }

    @FXML
    private void handleRestart() {
        NavigationManager navigationManager = new NavigationManager(
                gameOverRoot.getScene(),
                GameConfig.SCREEN_WIDTH,
                GameConfig.SCREEN_HEIGHT
        );
        navigationManager.restartLevel(currentLevelClass);
    }

    @FXML
    private void handleMainMenu() {
        NavigationManager navigationManager = new NavigationManager(
                gameOverRoot.getScene(),
                GameConfig.SCREEN_WIDTH,
                GameConfig.SCREEN_HEIGHT
        );
        navigationManager.goToMainMenu();
    }

}