package com.example.demo.controller;

import com.example.demo.config.GameConfig;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class PauseMenuController {
    @FXML
    private StackPane pauseRoot;

    @FXML
    private Rectangle overlay;

    private Runnable resumeAction;
    private Runnable restartAction;
    private Runnable mainMenuAction;

    public double getScreenWidth() {
        return GameConfig.SCREEN_WIDTH;
    }

    public double getScreenHeight() {
        return GameConfig.SCREEN_HEIGHT;
    }

    @FXML
    public void initialize() {
        // Make sure the root pane fills the entire screen
        pauseRoot.setPrefWidth(GameConfig.SCREEN_WIDTH);
        pauseRoot.setPrefHeight(GameConfig.SCREEN_HEIGHT);
    }

    public void setActions(Runnable resumeAction, Runnable restartAction, Runnable mainMenuAction) {
        this.resumeAction = resumeAction;
        this.restartAction = restartAction;
        this.mainMenuAction = mainMenuAction;
    }

    @FXML
    private void handleResume() {
        if (resumeAction != null) {
            resumeAction.run();
        }
    }

    @FXML
    private void handleRestart() {
        if (restartAction != null) {
            restartAction.run();
        }
    }

    @FXML
    private void handleMainMenu() {
        if (mainMenuAction != null) {
            mainMenuAction.run();
        }
    }

    public StackPane getPauseRoot() {
        return pauseRoot;
    }
}