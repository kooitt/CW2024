package com.example.demo.levels;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PauseHandler {

    private final Scene scene;
    private boolean isPaused = false;
    private final Runnable pauseAction;
    private final Runnable resumeAction;

    public PauseHandler(Scene scene, Runnable pauseAction, Runnable resumeAction) {
        this.scene = scene;
        this.pauseAction = pauseAction;
        this.resumeAction = resumeAction;
        initializePauseHandler();
    }

    public void initializePauseHandler() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                if (isPaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });
    }

    private void pauseGame() {
            pauseAction.run();
            isPaused = true;
    }

    private void resumeGame() {
            resumeAction.run();
            isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }
}