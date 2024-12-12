package com.example.demo.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private final Map<String, Media> soundEffects; // Store Media objects instead of MediaPlayer
    private MediaPlayer backgroundMusicPlayer;
    private boolean muted = false;

    private SoundManager() {
        soundEffects = new HashMap<>();
    }

    // Singleton instance getter
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Load sound effect by its name
    public void loadSFX(String name, String filePath) {
        Media media = new Media(getClass().getResource(filePath).toExternalForm());
        soundEffects.put(name, media); // Store Media instead of MediaPlayer
    }

    // Play a sound effect by name
    public void playSFX(String soundName) {
        if (!muted && soundEffects.containsKey(soundName)) {
            Media media = soundEffects.get(soundName);
            MediaPlayer sfxPlayer = new MediaPlayer(media); // Create a new MediaPlayer for each playback
            sfxPlayer.setOnEndOfMedia(sfxPlayer::dispose); // Dispose of MediaPlayer after playback
            sfxPlayer.play();
        } else {
            System.out.println("Sound not found or muted: " + soundName); // Debugging line
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    // Play background music
    public void playBackgroundMusic(String filePath) {
        if (backgroundMusicPlayer == null || !backgroundMusicPlayer.getMedia().getSource().equals(getClass().getResource(filePath).toExternalForm())) {
            Media media = new Media(getClass().getResource(filePath).toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the new music
        }
        if (!muted) {
            backgroundMusicPlayer.play();
        }
    }

    public MediaPlayer getBackgroundMusicPlayer() {
        return backgroundMusicPlayer;
    }

    // Set volume for background music
    public void setBackgroundMusicVolume(double volume) {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(volume);
        }
    }

    // Toggle mute state
    public void toggleMute() {
        muted = !muted;
        if (muted) {
            stopAllSFX();
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.setMute(true);
            }
        } else {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.setMute(false);
            }
        }
    }

    // Stop all SFX (if you track active players)
    public void stopAllSFX() {
        // Optional: Implement if you want to stop all currently playing SFX
    }
}
