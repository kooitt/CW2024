package com.example.demo.components;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundComponent {

    // 索引对应 SOUND_LOCATIONS:
    // 0: enemydown.wav (短音效)
    // 1: bossdown.wav   (短音效)
    // 2: bullet.wav     (短音效)
    // 3: level1.wav     (BGM)
    // 4: level2.mp3     (BGM)
    // 5: mainmenu.wav   (BGM)
    // 6: upgrade.wav    (短音效)
    // 7: gameover.wav   (短音效)
    // 8: getbullet.wav  (短音效)
    // 9: gethealth.wav  (短音效)
    // 10: blink.wav     (短音效)

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
            "/com/example/demo/sounds/blink.wav"
    };

    // 对于 BGM 使用 MediaPlayer
    private static MediaPlayer level1Player;
    private static MediaPlayer level2Player;
    private static MediaPlayer mainmenuPlayer;

    // 对于短音效使用 AudioClip
    private static AudioClip enemyDownClip;
    private static AudioClip bossDownClip;
    private static AudioClip bulletClip;
    private static AudioClip upgradeClip;
    private static AudioClip gameoverClip;
    private static AudioClip getbulletClip;
    private static AudioClip gethealthClip;
    private static AudioClip blinkClip;

    static {
        try {
            // 加载BGM对应的MediaPlayer
            // level1 (index 3)
            Media level1Media = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[3]).toExternalForm());
            level1Player = new MediaPlayer(level1Media);

            // level2 (index 4)
            Media level2Media = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[4]).toExternalForm());
            level2Player = new MediaPlayer(level2Media);

            // mainmenu (index 5)
            Media mainmenuMedia = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[5]).toExternalForm());
            mainmenuPlayer = new MediaPlayer(mainmenuMedia);

            // 加载短音效的 AudioClip
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

    // 短音效直接 play()
    public static void playExplosionSound() {
        // enemydown.wav 当成爆炸声吗？你的原代码中playExplosionSound()用的是index 0
        enemyDownClip.play();
    }

    public static void playBossdownSound(Runnable onFinished) {
        // bossdown是短音效，用AudioClip播放后完成无法回调endOfMedia的回调方式
        // AudioClip没有EndOfMedia回调，但可以在播放后设置一个计时器
        // 简单起见：因为 bossdownClip.play()很短，如果需要回调，使用一个Timeline或者Platform.runLater延迟。
        // 如果不需要严格的end回调，可以直接运行onFinished
        bossDownClip.play();
        if (onFinished != null) {
            // bossdown音效一旦播放，就直接回调(或加个延迟)
            onFinished.run();
        }
    }

    public static void playBossdownSound() {
        playBossdownSound(null);
    }

    public static void playBlinkSound() {
        blinkClip.play();
    }

    public static void playShootingSound() {
        bulletClip.play();
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

    public static void playMainmenuSound() {
        stopAllSound();
        currentBGM = mainmenuPlayer;
        currentBGM.setCycleCount(MediaPlayer.INDEFINITE);
        currentBGM.play();
    }

    public static void playUpgradeSound() {
        upgradeClip.play();
    }

    public static void playGameoverSound() {
        gameoverClip.play();
    }

    public static void playGetbulletSound() {
        getbulletClip.play();
    }

    public static void playGethealthSound() {
        gethealthClip.play();
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
        // AudioClip短音效不需要专门stop，是一次性播放
    }
}
