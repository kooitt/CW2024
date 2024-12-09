package com.example.demo.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.SettingsPage;

public class Controller {
    private Stage stage;
    private LevelParent currentLevel;
    private MainMenu mainMenu;
    private SettingsPage settingsPage;

    public Controller(Stage stage) {
        this.stage = stage;
        settingsPage = new SettingsPage(this);

        // 初始化后直接显示主菜单
        showMainMenu();
    }

    public SettingsPage getSettingsPage() {
        return settingsPage;
    }

    public Stage getStage() {
        return stage;
    }

    public void showMainMenu() {
        // 每次显示主菜单都创建新的 MainMenuParent，从而拥有全新场景
        mainMenu = new MainMenu(this);
        stage.setScene(mainMenu.getScene());
        stage.show();
    }

    public void launchGame() {
        goToLevel("com.example.demo.levels.LevelOne");
    }

    private void goToLevel(String className) {
        try {
            if (currentLevel != null) {
                currentLevel.cleanUp();
                currentLevel = null;
            }
            Class<?> myClass = Class.forName(className);
            currentLevel = (LevelParent) myClass.getConstructor(double.class, double.class, Controller.class)
                    .newInstance(stage.getHeight(), stage.getWidth(), this);

            currentLevel.setSettingsPageForPause(this.getSettingsPage());
            currentLevel.addLevelChangeListener(nextLevelName -> goToLevel(nextLevelName));

            Scene levelScene = currentLevel.initializeScene();
            stage.setScene(levelScene);
            currentLevel.startGame();
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void returnToMainMenu() {
        if (currentLevel != null) {
            currentLevel.cleanUp();
            currentLevel = null;
        }
        showMainMenu();
    }

    public void exitGame() {
        stage.close();
    }

    private void handleException(Exception e) {
        e.printStackTrace();
    }
}
