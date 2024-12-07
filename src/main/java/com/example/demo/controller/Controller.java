package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observer;
import java.util.Observable;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;
import com.example.demo.ui.MainMenu;
import com.example.demo.ui.SettingsPage;

public class Controller implements Observer {
 private Stage stage;
 private LevelParent currentLevel;
 private MainMenu mainMenu;
 private SettingsPage settingsPage;
 private StackPane rootPane;

 public Controller(Stage stage) {
  this.stage = stage;
  initialize();
 }

 private void initialize() {
  rootPane = new StackPane();
  mainMenu = new MainMenu(this);
  settingsPage = new SettingsPage(this);

  // 只添加 mainMenu 的 root 到 rootPane
  rootPane.getChildren().add(mainMenu.getRoot());
  mainMenu.getRoot().setVisible(true);

  Scene scene = new Scene(rootPane, stage.getWidth(), stage.getHeight());
  stage.setScene(scene);
  stage.show();
 }

 public SettingsPage getSettingsPage() {
  return settingsPage;
 }

 public Stage getStage() {
  return this.stage;
 }

 public void showMainMenu() {
  mainMenu.getRoot().setVisible(true);
 }

 public void launchGame() {
  try {
   goToLevel("com.example.demo.levels.LevelOne");
  } catch (Exception e) {
   handleException(e);
  }
 }

 private void goToLevel(String className) {
  try {
   if (currentLevel != null) currentLevel.cleanUp();
   Class<?> myClass = Class.forName(className);
   Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
   currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
   currentLevel.addObserver(this);

   // 确保 settingsPage 不在别的父节点中
   if (settingsPage.getRoot().getParent() != null) {
    ((Pane)settingsPage.getRoot().getParent()).getChildren().remove(settingsPage.getRoot());
   }

   currentLevel.setSettingsPageForPause(settingsPage);

   Scene levelScene = currentLevel.initializeScene();
   stage.setScene(levelScene);
   currentLevel.startGame();
  } catch (Exception e) {
   handleException(e);
  }
 }
 
 public void exitGame() {
  stage.close();
 }

 @Override
 public void update(Observable o, Object arg) {
  try {
   goToLevel((String) arg);
  } catch (Exception e) {
   handleException(e);
  }
 }

 private void handleException(Exception e) {
  if (e instanceof InvocationTargetException) {
   Throwable cause = e.getCause();
   if (cause != null) {
    cause.printStackTrace();
    System.out.println("Cause: " + cause.getMessage());
   }
  } else {
   e.printStackTrace();
  }
  Alert alert = new Alert(Alert.AlertType.ERROR, e.getClass().toString());
  alert.show();
 }
}
