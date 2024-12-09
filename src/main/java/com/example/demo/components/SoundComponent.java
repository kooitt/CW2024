package com.example.demo.components;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * The SoundComponent class is responsible for managing the game's background music (BGM) and sound effects (SFX).
 * It provides methods to play, stop, and adjust the volume of sounds used in various levels and game events.
 */
public class SoundComponent {

    /**
     * The currently playing background music (BGM) for the game.
     */
    private static MediaPlayer currentBGM;

    /**
     * Array of sound file locations used in the game.
     * Each index corresponds to a specific sound effect or background music.
     */
    private static final String[] SOUND_LOCATIONS = {
            "/com/example/demo/sounds/enemydown.wav",   // Enemy down sound effect
            "/com/example/demo/sounds/bossdown.wav",   // Boss down sound effect
            "/com/example/demo/sounds/bullet.wav",     // Bullet shooting sound effect
            "/com/example/demo/sounds/level1.wav",     // Level 1 background music
            "/com/example/demo/sounds/level2.mp3",     // Level 2 background music
            "/com/example/demo/sounds/mainmenu.wav",   // Main menu background music
            "/com/example/demo/sounds/upgrade.wav",    // Upgrade sound effect
            "/com/example/demo/sounds/gameover.wav",   // Game over sound effect
            "/com/example/demo/sounds/getbullet.wav",  // Get bullet sound effect
            "/com/example/demo/sounds/gethealth.wav",  // Get health sound effect
            "/com/example/demo/sounds/blink.wav",      // Blink sound effect
            "/com/example/demo/sounds/level3.mp3"      // Level 3 background music
    };

    // MediaPlayer instances for level background music
    private static MediaPlayer level1Player;
    private static MediaPlayer level2Player;
    private static MediaPlayer level3Player;
    private static MediaPlayer mainmenuPlayer;

    // AudioClip instances for sound effects
    private static AudioClip enemyDownClip;
    private static AudioClip bossDownClip;
    private static AudioClip bulletClip;
    private static AudioClip upgradeClip;
    private static AudioClip gameoverClip;
    private static AudioClip getbulletClip;
    private static AudioClip gethealthClip;
    private static AudioClip blinkClip;

    /**
     * The current volume for background music (BGM).
     * Ranges from 0.0 (mute) to 1.0 (maximum volume).
     */
    private static double bgmVolume = 0.5;

    /**
     * The current volume for sound effects (SFX).
     * Ranges from 0.0 (mute) to 1.0 (maximum volume).
     */
    private static double sfxVolume = 0.5;

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

            // Initialize AudioClips for sound effects
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
     * Sets the volume for background music (BGM).
     *
     * @param volume the desired volume, ranging from 0.0 to 1.0.
     */
    public static void setBgmVolume(double volume) {
        bgmVolume = Math.max(0, Math.min(volume, 1));
        if (level1Player != null) level1Player.setVolume(bgmVolume);
        if (level2Player != null) level2Player.setVolume(bgmVolume);
        if (level3Player != null) level3Player.setVolume(bgmVolume);
        if (mainmenuPlayer != null) mainmenuPlayer.setVolume(bgmVolume);
    }

    /**
     * Gets the current volume for background music (BGM).
     *
     * @return the current BGM volume.
     */
    public static double getBgmVolume() {
        return bgmVolume;
    }

    /**
     * Sets the volume for sound effects (SFX).
     *
     * @param volume the desired volume, ranging from 0.0 to 1.0.
     */
    public static void setSfxVolume(double volume) {
        sfxVolume = Math.max(0, Math.min(volume, 1));
    }

    /**
     * Gets the current volume for sound effects (SFX).
     *
     * @return the current SFX volume.
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
     * Plays the sound effect for an enemy being destroyed.
     */
    public static void playExplosionSound() {
        enemyDownClip.play(sfxVolume);
    }

    /**
     * Plays the sound effect for a boss being destroyed.
     *
     * @param onFinished a callback to execute when the sound finishes playing (optional).
     */
    public static void playBossdownSound(Runnable onFinished) {
        bossDownClip.play(sfxVolume);
        if (onFinished != null) {
            onFinished.run();
        }
    }

    /**
     * Plays the sound effect for a boss being destroyed.
     */
    public static void playBossdownSound() {
        playBossdownSound(null);
    }

    /**
     * Plays the blink sound effect.
     */
    public static void playBlinkSound() {
        blinkClip.play(sfxVolume);
    }

    /**
     * Plays the bullet shooting sound effect.
     */
    public static void playShootingSound() {
        bulletClip.play(sfxVolume);
    }

    /**
     * Starts playing the background music for Level 1.
     */
    public static void playLevel1Sound() {
        stopAllSound();
        currentBGM = level1Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts playing the background music for Level 2.
     */
    public static void playLevel2Sound() {
        stopAllSound();
        currentBGM = level2Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts playing the background music for Level 3.
     */
    public static void playLevel3Sound() {
        stopAllSound();
        currentBGM = level3Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Starts playing the background music for the main menu.
     */
    public static void playMainmenuSound() {
        stopAllSound();
        currentBGM = mainmenuPlayer;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    /**
     * Plays the upgrade sound effect.
     */
    public static void playUpgradeSound() {
        upgradeClip.play(sfxVolume);
    }

    /**
     * Plays the game over sound effect.
     */
    public static void playGameoverSound() {
        gameoverClip.play(sfxVolume);
    }

    /**
     * Plays the sound effect for collecting a bullet power-up.
     */
    public static void playGetbulletSound() {
        getbulletClip.play(sfxVolume);
    }

    /**
     * Plays the sound effect for collecting a health power-up.
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
     * Stops all currently playing sounds, including background music and sound effects.
     */
    public static void stopAllSound() {
        if (currentBGM != null) {
            currentBGM.stop();
            currentBGM = null;
        }
    }
}
