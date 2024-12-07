package com.example.demo.controller;

import java.lang.reflect.Constructor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.SettingsPage;

public class Controller {
    private Stage stage;
    private LevelParent currentLevel;
    private MainMenu mainMenu;
    private SettingsPage settingsPage;
    private StackPane rootPane;
    private Scene mainMenuScene; // 保存主菜单场景的引用

    public Controller(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        rootPane = new StackPane();
        mainMenu = new MainMenu(this);
        settingsPage = new SettingsPage(this);

        rootPane.getChildren().add(mainMenu.getRoot());
        mainMenu.getRoot().setVisible(true);

        mainMenuScene = new Scene(rootPane, stage.getWidth(), stage.getHeight());
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public SettingsPage getSettingsPage() {
        return settingsPage;
    }

    public Stage getStage() {
        return stage;
    }

    public void showMainMenu() {
        mainMenu.getRoot().setVisible(true);
        stage.setScene(mainMenuScene);
    }

    public void launchGame() {
        goToLevel("com.example.demo.levels.LevelOne");
    }

    private void goToLevel(String className) {
        try {
            if (currentLevel != null) {
                currentLevel.cleanUp();
            }
            Class<?> myClass = Class.forName(className);
            Constructor<?> constructor = myClass.getConstructor(double.class, double.class, Controller.class);
            currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth(), this);
            currentLevel.addObserver((o, arg) -> goToLevel((String) arg));
            currentLevel.setSettingsPageForPause(settingsPage);

            Scene levelScene = currentLevel.initializeScene();
            stage.setScene(levelScene);
            currentLevel.startGame();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void clearScene(Scene scene) {
        if (scene != null) {
            if (scene.getRoot() instanceof Pane) {
                Pane rootPane = (Pane) scene.getRoot();
                rootPane.getChildren().clear();
            }
        }
    }

    public void returnToMainMenu() {
        // 在切换回主菜单前清理当前关卡
        if (currentLevel != null) {
            currentLevel.cleanUp();
            currentLevel = null;
        }
        clearScene(stage.getScene());
        showMainMenu();
    }

    public void exitGame() {
        stage.close();
    }

    private void handleException(Exception e) {
        // 错误处理逻辑
    }
}