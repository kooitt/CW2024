package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelSelectionMenu {
    private final Stage stage;
    private final Controller controller;

    public LevelSelectionMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public Scene createLevelSelectionScene() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-alignment: center; -fx-padding: 50;");

        for (int i = 1; i <= 5; i++) {
            int level = i;
            Button levelButton = new Button("Level " + level);

            String className = "com.example.demo.Levels.Level" + getLevelName(level);

            levelButton.setOnAction(e -> {
                try {
                    controller.goToLevel(className);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            layout.getChildren().add(levelButton);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> controller.showMainMenu());

        layout.getChildren().add(backButton);

        return new Scene(layout, stage.getWidth(), stage.getHeight());
    }

    private String getLevelName(int levelNumber) {
        switch (levelNumber) {
            case 1: return "One";
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            default: throw new IllegalArgumentException("Invalid level number: " + levelNumber);
        }
    }
}
