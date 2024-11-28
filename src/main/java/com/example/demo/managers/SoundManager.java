package com.example.demo.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    // Singleton instance
    private static SoundManager instance;

    private Map<String, MediaPlayer> musicPlayers;
    private Map<String, AudioClip> shootSounds;
    private final static String LEVEL_BACKGROUND_MUSIC_FILE_PATH = "/music/fightmusic.mp3";
    private final static String MENU_BACKGROUND_MUSIC_FILE_PATH = "/music/backgroundmusic.mp3";
    private final static String USER_SHOOT_SOUND_FILE_PATH = "/music/userprojectile.wav";
    private final static String ENEMY_SHOOT_SOUND_FILE_PATH = "/music/enemyprojectile.wav";

    // Make constructor private
    private SoundManager() {
        musicPlayers = new HashMap<>();
        shootSounds = new HashMap<>();

        // Initialize sounds
        loadBackgroundMusic("menu", MENU_BACKGROUND_MUSIC_FILE_PATH);
        loadBackgroundMusic("level", LEVEL_BACKGROUND_MUSIC_FILE_PATH);
        loadShootSound("user", USER_SHOOT_SOUND_FILE_PATH);
        loadShootSound("enemy", ENEMY_SHOOT_SOUND_FILE_PATH);
    }

    // Singleton getter
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadBackgroundMusic(String key, String musicFilePath) {
        Media music = new Media(getClass().getResource(musicFilePath).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        musicPlayers.put(key, mediaPlayer);
    }

    private void loadShootSound(String key, String soundFilePath) {
        AudioClip audioClip = new AudioClip(getClass().getResource(soundFilePath).toString());
        shootSounds.put(key, audioClip);
    }

    public void playBackgroundMusic(String key) {
        MediaPlayer player = musicPlayers.get(key);
        if (player != null) {
            stopAllBackgroundMusic(); // Stop any currently playing music
            player.stop(); // Ensure this specific player is stopped
            player.seek(player.getStartTime()); // Reset to beginning
            player.play();
        }
    }

    public void stopAllBackgroundMusic() {
        for (MediaPlayer player : musicPlayers.values()) {
            player.stop();
            player.seek(player.getStartTime()); // Reset all players to beginning
        }
    }

    public void playShootSound(String key) {
        AudioClip clip = shootSounds.get(key);
        if (clip != null) {
            clip.play();
        }
    }

    public void playDamagedSound() {
        AudioClip clip = new AudioClip(getClass().getResource("/music/damaged.mp3").toString());
        clip.play();
    }
}