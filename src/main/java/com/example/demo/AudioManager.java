package com.example.demo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private final Map<String, Clip> audioClips = new HashMap<>();

    // Load an audio file
    public void loadAudio(String key, String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            audioClips.put(key, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play a specific audio
    public void playAudio(String key) {
        Clip clip = audioClips.get(key);
        if (clip != null) {
            clip.start();
        }
    }

    // Stop a specific audio
    public void stopAudio(String key) {
        Clip clip = audioClips.get(key);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Loop a specific audio continuously
    public void loopAudio(String key) {
        Clip clip = audioClips.get(key);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Close all audio resources
    public void closeAllAudio() {
        for (Clip clip : audioClips.values()) {
            clip.close();
        }
    }

    public static void main(String[] args) {
        AudioManager audioManager = new AudioManager();
        audioManager.loadAudio("background", "path/to/your/background.wav");
        audioManager.loadAudio("effect", "path/to/your/effect.wav");

        audioManager.playAudio("background");

        // Add your game logic here

        // Example to play an effect sound
        audioManager.playAudio("effect");

        // Example to stop background music after some time
        try {
            Thread.sleep(5000); // Play for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioManager.stopAudio("background");
        audioManager.closeAllAudio();
    }
}