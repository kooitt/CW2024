package com.example.demo.handlers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Handles pausing and resuming the game when the ESCAPE key is pressed.
 */
public class PauseHandler {

    private final Scene scene;
    private boolean isPaused = false;
    private final Runnable pauseAction;
    private final Runnable resumeAction;

    /**
     * Constructs a PauseHandler with the specified scene and actions for pausing and resuming the game.
     *
     * @param scene the scene to attach the pause handler to.
     * @param pauseAction the action to perform when the game is paused.
     * @param resumeAction the action to perform when the game is resumed.
     */
    public PauseHandler(Scene scene, Runnable pauseAction, Runnable resumeAction) {
        this.scene = scene;
        this.pauseAction = pauseAction;
        this.resumeAction = resumeAction;
        initializePauseHandler();
    }

    /**
     * Initializes the pause handler by setting a key press event handler on the scene.
     */
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

    /**
     * Pauses the game by running the pause action and setting the paused state to true.
     */
    private void pauseGame() {
        pauseAction.run();
        isPaused = true;
    }

    /**
     * Resumes the game by running the resume action and setting the paused state to false.
     */
    private void resumeGame() {
        resumeAction.run();
        isPaused = false;
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return true if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }
}