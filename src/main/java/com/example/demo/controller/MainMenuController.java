package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainMenuController {

    private static final int SCREEN_HEIGHT = 750;
    private static final int SCREEN_WIDTH = 1300;
    private Stage stage;
    private static final String SETTINGS_FXML = "/fxml/settings.fxml";
    private static final String BUTTON_CLICK_SFX = "/com/example/demo/sfx/ui_sfx/buttonclick.mp3";
    private static final String BG_MUSIC = "/com/example/demo/sfx/level_music/mainMenuMusic.mp3";
    private static MediaPlayer mediaPlayer;
    private SoundManager soundManager;

    public void initialize(Stage stage) {
        this.stage = stage;
        soundManager = SoundManager.getInstance();

        // Load sound effects
        soundManager.loadSFX("button_click", BUTTON_CLICK_SFX);

        // Play background music if not already playing
        if (soundManager.getBackgroundMusicPlayer() == null) {
            soundManager.playBackgroundMusic(BG_MUSIC);  // Play background music only if not already playing
        }
    }

    @FXML
    private void onStartButtonClicked(ActionEvent event) throws IOException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println("Start button clicked!");
        soundManager.playSFX("button_click");  // Play button click sound
        soundManager.stopBackgroundMusic();
        Controller myController = new Controller(stage);
        myController.launchGame();
    }

    @FXML
    private void onSettingsButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Settings button clicked!");
        soundManager.playSFX("button_click");

        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource(SETTINGS_FXML));
        Parent root = settingsLoader.load();

        SettingsController settingsController = settingsLoader.getController();
        settingsController.initialize(stage); // Pass MediaPlayer and SoundManager

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onExitButtonClicked(ActionEvent event) {
        System.out.println("Exiting game!");
        soundManager.playSFX("button_click");  // Play button click sound
        System.exit(0);
    }

    public void showMainMenu(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainmenu.fxml"));
        Parent root = loader.load();

        // Get the controller and initialize it
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.initialize(stage);

        // Set the scene and show the stage
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);

    }
}