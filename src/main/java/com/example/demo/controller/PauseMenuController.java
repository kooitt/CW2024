package com.example.demo.controller;

import com.example.demo.levels.LevelParent;
import javafx.fxml.FXML;

public class PauseMenuController {

    private LevelParent level;

    public void setLevel(LevelParent level) {
        this.level = level;
    }

    @FXML
    private void handleResume() {
        level.resumeGame();
    }

    @FXML
    private void handleQuit() {
        System.exit(0);
    }
}