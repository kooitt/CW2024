package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * GameController manages the game flow, including starting, pausing, and saving the game.
 */
public class GameController {

    private Stage stage;
    private boolean isPaused;
    private LevelParent currentLevel;

    /**
     * Constructor for initializing the GameController.
     *
     * @param stage The primary stage of the game.
     */
    public GameController(Stage stage) {
        this.stage = stage;
        this.isPaused = false;
    }

    /**
     * Starts the game by initializing the first level and setting up the scene.
     */
    public void startGame() {
        currentLevel = new LevelOne(600, 800);  // example level
        Scene scene = currentLevel.initializeScene();
        scene.setOnKeyPressed(this::handleKeyPress);
        stage.setScene(scene);
        stage.show();
        currentLevel.startGame();
    }

    /**
     * Handles the key press events, allowing pausing or saving.
     *
     * @param event The key event that occurs when a key is pressed.
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            togglePause();
        }
        if (event.getCode() == KeyCode.S) {
            saveGame();
        }
    }

    /**
     * Pauses or resumes the game based on the current state.
     */
    private void togglePause() {
        if (isPaused) {
            currentLevel.startGame();  // Resume the game
        } else {
            currentLevel.stopGame();  // Pause the game
        }
        isPaused = !isPaused;
    }

    /**
     * Saves the current state of the game (e.g., score, level, health).
     */
    private void saveGame() {
        // Save game logic (e.g., serialize the game state to a file)
    }
}
