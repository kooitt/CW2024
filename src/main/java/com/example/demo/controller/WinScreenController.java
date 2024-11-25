package com.example.demo.controller;

import com.example.demo.config.GameConfig;
import com.example.demo.levels.LevelOne;
import com.example.demo.managers.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WinScreenController {
    @FXML
    private StackPane winRoot;
    private Stage stage;
    private Class<?> baseLevelClass = LevelOne.class;

    public void setStage(Stage stage) {
        this.stage = stage;
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
        winRoot.setPrefWidth(GameConfig.SCREEN_WIDTH);
        winRoot.setPrefHeight(GameConfig.SCREEN_HEIGHT);
    }

    @FXML
    private void handleRestart() {
        NavigationManager navigationManager = new NavigationManager(
                winRoot.getScene(),
                stage.getWidth(),
                stage.getHeight()
        );
        navigationManager.restartLevel(baseLevelClass);
    }

    @FXML
    private void handleMainMenu() {
        NavigationManager navigationManager = new NavigationManager(
                winRoot.getScene(),
                stage.getWidth(),
                stage.getHeight()
        );
        navigationManager.goToMainMenu();
    }

}