package com.example.demo.components;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * The SoundComponent class is responsible for managing the game's background music (BGM)
 * and sound effects (SFX). It provides methods to play, stop, and adjust the volume of
 * sounds used during gameplay and specific game events.
 */
public class SoundComponent {

    /**
     * The currently playing background music (BGM).
     */
    private static MediaPlayer currentBGM;

    /**
     * Array containing the file paths of sound resources. Each index corresponds to a
     * specific sound effect or background music used in the game.
     */
    private static final String[] SOUND_LOCATIONS = {
            "/com/example/demo/sounds/enemydown.wav",   // Sound when an enemy is destroyed
            "/com/example/demo/sounds/bossdown.wav",   // Sound when a boss is destroyed
            "/com/example/demo/sounds/bullet.wav",     // Bullet firing sound
            "/com/example/demo/sounds/level1.wav",     // Background music for Level 1
            "/com/example/demo/sounds/level2.mp3",     // Background music for Level 2
            "/com/example/demo/sounds/mainmenu.wav",   // Background music for the main menu
            "/com/example/demo/sounds/upgrade.wav",    // Upgrade item sound effect
            "/com/example/demo/sounds/gameover.wav",   // Game over sound
            "/com/example/demo/sounds/getbullet.wav",  // Bullet power-up collected sound
            "/com/example/demo/sounds/gethealth.wav",  // Health power-up collected sound
            "/com/example/demo/sounds/blink.wav",      // Blink sound effect
            "/com/example/demo/sounds/level3.mp3"      // Background music for Level 3
    };

    /**
     * MediaPlayer for Level 1 background music.
     */
    private static MediaPlayer level1Player;

    /**
     * MediaPlayer for Level 2 background music.
     */
    private static MediaPlayer level2Player;

    /**
     * MediaPlayer for Level 3 background music.
     */
    private static MediaPlayer level3Player;

    /**
     * MediaPlayer for the main menu background music.
     */
    private static MediaPlayer mainmenuPlayer;

    /**
     * AudioClip for the "enemy down" sound effect.
     */
    private static AudioClip enemyDownClip;

    /**
     * AudioClip for the "boss down" sound effect.
     */
    private static AudioClip bossDownClip;

    /**
     * AudioClip for the "bullet shooting" sound effect.
     */
    private static AudioClip bulletClip;

    /**
     * AudioClip for the "upgrade collected" sound effect.
     */
    private static AudioClip upgradeClip;

    /**
     * AudioClip for the "game over" sound effect.
     */
    private static AudioClip gameoverClip;

    /**
     * AudioClip for the "bullet power-up collected" sound effect.
     */
    private static AudioClip getbulletClip;

    /**
     * AudioClip for the "health power-up collected" sound effect.
     */
    private static AudioClip gethealthClip;

    /**
     * AudioClip for the "blink" sound effect.
     */
    private static AudioClip blinkClip;

    /**
     * The volume level for background music (BGM), ranging from 0.0 (mute) to 1.0 (maximum volume).
     */
    private static double bgmVolume = 0.5;

    /**
     * The volume level for sound effects (SFX), ranging from 0.0 (mute) to 1.0 (maximum volume).
     */
    private static double sfxVolume = 0.5;

    // Static initialization block to set up MediaPlayer and AudioClip objects
    static {
        try {
            // Initialize MediaPlayer for background music
            Media level1Media = new Media(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[3])).toExternalForm());
            level1Player = new MediaPlayer(level1Media);
            level1Player.setVolume(bgmVolume);

            Media level2Media = new Media(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[4])).toExternalForm());
            level2Player = new MediaPlayer(level2Media);
            level2Player.setVolume(bgmVolume);

            Media level3Media = new Media(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[11])).toExternalForm());
            level3Player = new MediaPlayer(level3Media);
            level3Player.setVolume(bgmVolume);

            Media mainmenuMedia = new Media(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[5])).toExternalForm());
            mainmenuPlayer = new MediaPlayer(mainmenuMedia);
            mainmenuPlayer.setVolume(bgmVolume);

            // Initialize AudioClip objects for sound effects
            enemyDownClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[0])).toExternalForm());
            bossDownClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[1])).toExternalForm());
            bulletClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[2])).toExternalForm());
            upgradeClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[6])).toExternalForm());
            gameoverClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[7])).toExternalForm());
            getbulletClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[8])).toExternalForm());
            gethealthClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[9])).toExternalForm());
            blinkClip = new AudioClip(Objects.requireNonNull(SoundComponent.class.getResource(SOUND_LOCATIONS[10])).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the background music (BGM) volume.
     *
     * @param volume the desired volume, ranging from 0.0 (mute) to 1.0 (maximum).
     */
    public static void setBgmVolume(double volume) {
        bgmVolume = Math.max(0, Math.min(volume, 1));
        if (level1Player != null) level1Player.setVolume(bgmVolume);
        if (level2Player != null) level2Player.setVolume(bgmVolume);
        if (level3Player != null) level3Player.setVolume(bgmVolume);
        if (mainmenuPlayer != null) mainmenuPlayer.setVolume(bgmVolume);
    }

    /**
     * Gets the current background music (BGM) volume.
     *
     * @return the current BGM volume level.
     */
    public static double getBgmVolume() {
        return bgmVolume;
    }

    /**
     * Sets the sound effects (SFX) volume.
     *
     * @param volume the desired volume, ranging from 0.0 (mute) to 1.0 (maximum).
     */
    public static void setSfxVolume(double volume) {
        sfxVolume = Math.max(0, Math.min(volume, 1));
    }

    /**
     * Gets the current sound effects (SFX) volume.
     *
     * @return the current SFX volume level.
     */
    public static double getSfxVolume() {
        return sfxVolume;
    }

    /**
     * Pauses the currently playing background music.
     */
    public static void pauseCurrentLevelSound() {
        if (currentBGM != null) {
            currentBGM.pause();
        }
    }

    /**
     * Resumes the currently paused background music.
     */
    public static void resumeCurrentLevelSound() {
        if (currentBGM != null) {
            currentBGM.play();
        }
    }

    /**
     * Plays the "enemy down" sound effect.
     */
    public static void playExplosionSound() {
        enemyDownClip.play(sfxVolume);
    }

    /**
     * Plays the "boss down" sound effect with an optional callback.
     *
     * @param onFinished the callback to execute when the sound finishes playing.
     */
    public static void playBossdownSound(Runnable onFinished) {
        bossDownClip.play(sfxVolume);
        if (onFinished != null) {
            onFinished.run();
        }
    }

    /**
     * Overloaded method to play the "boss down" sound effect without a callback.
     */
    public static void playBossdownSound() {
        playBossdownSound(null);
    }

    /**
     * Plays the "blink" sound effect.
     */
    public static void playBlinkSound() {
        blinkClip.play(sfxVolume);
    }

    /**
     * Plays the "bullet shooting" sound effect.
     */
    public static void playShootingSound() {
        bulletClip.play(sfxVolume);
    }

    /**
     * Starts the background music for Level 1.
     */
    public static void playLevel1Sound() {
        stopAllSound();
        currentBGM = level1Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts the background music for Level 2.
     */
    public static void playLevel2Sound() {
        stopAllSound();
        currentBGM = level2Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts the background music for Level 3.
     */
    public static void playLevel3Sound() {
        stopAllSound();
        currentBGM = level3Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts the background music for the main menu.
     */
    public static void playMainmenuSound() {
        stopAllSound();
        currentBGM = mainmenuPlayer;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Plays the "upgrade" sound effect.
     */
    public static void playUpgradeSound() {
        upgradeClip.play(sfxVolume);
    }

    /**
     * Plays the "game over" sound effect.
     */
    public static void playGameoverSound() {
        gameoverClip.play(sfxVolume);
    }

    /**
     * Plays the "bullet power-up collected" sound effect.
     */
    public static void playGetbulletSound() {
        getbulletClip.play(sfxVolume);
    }

    /**
     * Plays the "health power-up collected" sound effect.
     */
    public static void playGethealthSound() {
        gethealthClip.play(sfxVolume);
    }

    /**
     * Stops the background music for Level 1.
     */
    public static void stopLevel1Sound() {
        if (level1Player != null) {
            level1Player.stop();
        }
        if (currentBGM == level1Player) {
            currentBGM = null;
        }
    }

    /**
     * Stops the background music for Level 2.
     */
    public static void stopLevel2Sound() {
        if (level2Player != null) {
            level2Player.stop();
        }
        if (currentBGM == level2Player) {
            currentBGM = null;
        }
    }

    /**
     * Stops all currently playing sounds, including BGM and SFX.
     */
    public static void stopAllSound() {
        if (currentBGM != null) {
            currentBGM.stop();
            currentBGM = null;
        }
    }
}
