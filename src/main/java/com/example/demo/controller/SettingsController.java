package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Slider;

import java.io.IOException;

public class SettingsController {

    private Stage stage;
    private static final String BUTTON_CLICK_SFX = "/com/example/demo/sfx/ui_sfx/buttonclick.mp3";
    private SoundManager soundManager;

    @FXML
    private Button backButton;

    @FXML
    private Slider volumeSlider;

    public void initialize(Stage stage) {
        this.stage = stage;
        soundManager = SoundManager.getInstance();
        soundManager.loadSFX("button_click",BUTTON_CLICK_SFX);
        // Set up the volume slider
        MediaPlayer backgroundPlayer = soundManager.getBackgroundMusicPlayer();
        if (backgroundPlayer != null) {
            // Set the slider to the current volume
            volumeSlider.setValue(backgroundPlayer.getVolume() * 100);

            // Add listener to update volume in SoundManager
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                soundManager.setBackgroundMusicVolume(newValue.doubleValue() / 100);
            });
        }
    }

    @FXML
    private void onBackButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Returning to the main menu...");

        soundManager.playSFX("button_click");

        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.showMainMenu(stage);

    }
}
