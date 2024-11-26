package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;

    // Load audio file
    public void loadAudio(String filePath) {
        try {
            Media media = new Media(new File(filePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("Error loading audio: " + e.getMessage());
        }
    }

    // Play audio
    public void play() {
        if (isReady()) {
            mediaPlayer.play();
        }
    }

    // Pause audio
    public void pause() {
        if (isReady()) {
            mediaPlayer.pause();
        }
    }

    // Stop audio
    public void stop() {
        if (isReady()) {
            mediaPlayer.stop();
        }
    }

    // Loop audio
    public void loop() {
        if (isReady()) {
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            mediaPlayer.play();
        }
    }

    // Set volume
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    // Close audio resources
    public void close() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    // Check if media player is ready
    private boolean isReady() {
        return mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.UNKNOWN;
    }
}