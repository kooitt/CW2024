package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundComponent {

    private static MediaPlayer currentBGM;

    private static final String[] SOUND_LOCATIONS = {
            "/com/example/demo/sounds/enemydown.wav",
            "/com/example/demo/sounds/bossdown.wav",
            "/com/example/demo/sounds/bullet.wav",
            "/com/example/demo/sounds/level1.wav",
            "/com/example/demo/sounds/level2.mp3",
            "/com/example/demo/sounds/mainmenu.wav",
            "/com/example/demo/sounds/upgrade.wav",
            "/com/example/demo/sounds/gameover.wav",
            "/com/example/demo/sounds/getbullet.wav",
            "/com/example/demo/sounds/gethealth.wav",
            "/com/example/demo/sounds/blink.wav",
            "/com/example/demo/sounds/level3.mp3"
    };

    private static MediaPlayer level1Player;
    private static MediaPlayer level2Player;
    private static MediaPlayer level3Player;
    private static MediaPlayer mainmenuPlayer;

    private static AudioClip enemyDownClip;
    private static AudioClip bossDownClip;
    private static AudioClip bulletClip;
    private static AudioClip upgradeClip;
    private static AudioClip gameoverClip;
    private static AudioClip getbulletClip;
    private static AudioClip gethealthClip;
    private static AudioClip blinkClip;

    // 新增两个静态变量用于存储音量
    private static double bgmVolume = 0.5; // 默认50%音量
    private static double sfxVolume = 0.5; // 默认50%音量

    static {
        try {
            Media level1Media = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[3]).toExternalForm());
            level1Player = new MediaPlayer(level1Media);
            level1Player.setVolume(bgmVolume); // 设置初始音量

            Media level2Media = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[4]).toExternalForm());
            level2Player = new MediaPlayer(level2Media);
            level2Player.setVolume(bgmVolume);

            Media level3Media = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[11]).toExternalForm());
            level3Player = new MediaPlayer(level3Media);
            level3Player.setVolume(bgmVolume);

            Media mainmenuMedia = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[5]).toExternalForm());
            mainmenuPlayer = new MediaPlayer(mainmenuMedia);
            mainmenuPlayer.setVolume(bgmVolume);

            enemyDownClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[0]).toExternalForm());
            bossDownClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[1]).toExternalForm());
            bulletClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[2]).toExternalForm());
            upgradeClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[6]).toExternalForm());
            gameoverClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[7]).toExternalForm());
            getbulletClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[8]).toExternalForm());
            gethealthClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[9]).toExternalForm());
            blinkClip = new AudioClip(SoundComponent.class.getResource(SOUND_LOCATIONS[10]).toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 添加设置和获取 BGM 音量的方法
    public static void setBgmVolume(double volume) {
        bgmVolume = Math.max(0, Math.min(volume, 1)); // 保证音量在0到1之间
        if (level1Player != null) level1Player.setVolume(bgmVolume);
        if (level2Player != null) level2Player.setVolume(bgmVolume);
        if (level3Player != null) level3Player.setVolume(bgmVolume);
        if (mainmenuPlayer != null) mainmenuPlayer.setVolume(bgmVolume);
    }

    public static double getBgmVolume() {
        return bgmVolume;
    }

    // 添加设置和获取 SFX 音量的方法
    public static void setSfxVolume(double volume) {
        sfxVolume = Math.max(0, Math.min(volume, 1)); // 保证音量在0到1之间
    }

    public static double getSfxVolume() {
        return sfxVolume;
    }

    public static void pauseCurrentLevelSound() {
        if (currentBGM != null) {
            currentBGM.pause();
        }
    }

    public static void resumeCurrentLevelSound() {
        if (currentBGM != null) {
            currentBGM.play();
        }
    }

    public static void playExplosionSound() {
        enemyDownClip.play(sfxVolume);
    }

    public static void playBossdownSound(Runnable onFinished) {
        bossDownClip.play(sfxVolume);
        if (onFinished != null) {
            onFinished.run();
        }
    }

    public static void playBossdownSound() {
        playBossdownSound(null);
    }

    public static void playBlinkSound() {
        blinkClip.play(sfxVolume);
    }

    public static void playShootingSound() {
        bulletClip.play(sfxVolume);
    }

    public static void playLevel1Sound() {
        stopAllSound();
        currentBGM = level1Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    public static void playLevel2Sound() {
        stopAllSound();
        currentBGM = level2Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    public static void playLevel3Sound() {
        stopAllSound();
        currentBGM = level3Player;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    public static void playMainmenuSound() {
        stopAllSound();
        currentBGM = mainmenuPlayer;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    public static void playUpgradeSound() {
        upgradeClip.play(sfxVolume);
    }

    public static void playGameoverSound() {
        gameoverClip.play(sfxVolume);
    }

    public static void playGetbulletSound() {
        getbulletClip.play(sfxVolume);
    }

    public static void playGethealthSound() {
        gethealthClip.play(sfxVolume);
    }

    public static void stopLevel1Sound() {
        if (level1Player != null) {
            level1Player.stop();
        }
        if (currentBGM == level1Player) {
            currentBGM = null;
        }
    }

    public static void stopLevel2Sound() {
        if (level2Player != null) {
            level2Player.stop();
        }
        if (currentBGM == level2Player) {
            currentBGM = null;
        }
    }

    public static void stopAllSound() {
        if (currentBGM != null) {
            currentBGM.stop();
            currentBGM = null;
        }
    }
}
