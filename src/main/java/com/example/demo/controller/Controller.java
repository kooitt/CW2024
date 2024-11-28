package com.example.demo.controller;

import com.example.demo.levels.LevelParent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;

public class Controller {

    private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.Levels.LevelOne";
    private final Stage stage;

    public Controller(Stage stage) {
        this.stage = stage;
    }

    public void showMainMenu() {
        MainMenu mainMenu = new MainMenu(stage);
        Scene mainMenuScene = mainMenu.createMainMenuScene(this);
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public void launchGame() throws Exception {
        goToLevel(LEVEL_ONE_CLASS_NAME);
    }


    public void showLevelSelectionMenu() {
        LevelSelectionMenu levelSelectionMenu = new LevelSelectionMenu(stage, this);
        Scene levelSelectionScene = levelSelectionMenu.createLevelSelectionScene();
        stage.setScene(levelSelectionScene);
        stage.show();
    }

    protected void goToLevel(String className) throws Exception {
        Class<?> myClass = Class.forName(className);
        Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
        LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
        myLevel.addObserver((o, arg) -> {
            try {
                goToLevel((String) arg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Scene scene = myLevel.initializeScene();
        stage.setScene(scene);
        myLevel.startGame();
    }
}
