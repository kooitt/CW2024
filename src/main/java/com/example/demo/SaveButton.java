package com.example.demo;

import javafx.scene.control.Button;
import java.io.*;

/**
 * The SaveButton class creates a button that allows the user to save the game state.
 */
public class SaveButton {

    private Button saveButton;

    /**
     * Constructs a save button.
     */
    public SaveButton() {
        saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> saveGame());
    }

    /**
     * Saves the current game state to a file.
     */
    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("game_save.dat"))) {
            out.writeObject(GameState.getInstance());  // Save current game state (singleton or game state object)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Button getSaveButton() {
        return saveButton;
    }
}
