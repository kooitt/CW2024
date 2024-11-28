package com.example.demo.controller;

import com.example.demo.levels.ArcadeLevel;
import com.example.demo.managers.SoundManager;
import javafx.stage.Stage;

public class ArcadeModeController {
    private final Stage stage;
    private final SoundManager soundManager;

    public ArcadeModeController(Stage stage) {
        this.stage = stage;
        this.soundManager = SoundManager.getInstance();
    }

    public void launchArcadeMode() throws Exception {


        // Create and start arcade level using your existing level infrastructure
        String arcadeLevelClass = "com.example.demo.levels.ArcadeLevel";
        String arcadeLevelName = "Arcade Mode";

        // Use the existing GameController to handle the level
        GameController gameController = new GameController(stage);

        // Launch the arcade level using the existing game infrastructure
        gameController.goToLevel(arcadeLevelClass, arcadeLevelName);
    }
}