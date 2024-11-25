//package com.example.demo.managers;
//
//import com.example.demo.config.GameState;
//import com.example.demo.controller.GameController;
//import com.example.demo.controller.GameOverController;
//import com.example.demo.controller.WinScreenController;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class GameStateManager {
//    private final NavigationManager navigationManager;
//    private final InputManager inputManager;
//    private GameState currentState;
//    private final SoundManager soundManager; // Assuming you have this
//
//    public GameStateManager(NavigationManager navigationManager InputManager inputManager, SoundManager soundManager) {
//        this.navigationManager = navigationManager;
//        this.inputManager = inputManager;
//        this.soundManager = soundManager;
//        this.currentState = GameState.ACTIVE;
//    }
//
//    public void setState(GameState newState) {
//        this.currentState = newState;
//        inputManager.setActive(newState == GameState.ACTIVE);
//
//        switch (newState) {
//            case WIN:
//                handleWinState();
//                break;
//            case LOSE:
//                handleGameOverState();
//                break;
//            case PAUSED:
//                handlePausedState();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void handleWinState() {
//        stopGameplay();
//        showEndScreen("WinScreen.fxml", true);
//    }
//
//    private void handleGameOverState() {
//        stopGameplay();
//        showEndScreen("GameOver.fxml", false);
//    }
//
//    private void handlePausedState() {
//        // Handle pause state if needed
//    }
//
//    private void stopGameplay() {
//        soundManager.stopAllBackgroundMusic();
//        inputManager.stopAllMovement();
//    }
//
//    private void showEndScreen(String fxmlPath, boolean isWin) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
//            Parent root = loader.load();
//            Scene endScene = new Scene(root, stage.getWidth(), stage.getHeight());
//
//            if (isWin) {
//                WinScreenController controller = loader.getController();
//                controller.setStage(stage);
//            } else {
//                GameOverController controller = loader.getController();
//                controller.setStage(stage);
//            }
//
//            stage.setScene(endScene);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public GameState getCurrentState() {
//        return currentState;
//    }
//
//    public void resumeGame() {
//        setState(GameState.ACTIVE);
//    }
//}