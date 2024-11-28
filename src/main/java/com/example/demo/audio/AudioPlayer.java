package com.example.demo.audio;

import javafx.scene.media.AudioClip;

import java.util.Objects;

public class AudioPlayer {
    private AudioClip audioClip;

    // Load audio file
    public void loadAudio(String filePath) {
        try {
            audioClip = new AudioClip(Objects.requireNonNull(getClass().getResource(filePath)).toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading audio: " + e.getMessage());
        }
    }

    // Play audio
    public void play() {
        if (isReady()) {
            audioClip.play();
        }
    }

    // Set volume (0.0 to 1.0)
    public void setVolume(double volume) {
        if (audioClip != null) {
            audioClip.setVolume(Math.max(0.0, Math.min(volume, 1.0)));
        }
    }

    // Check if audio clip is ready
    private boolean isReady() {
        return audioClip != null;
    }
}