// SoundComponent.java
package com.example.demo.components;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundComponent {

    private static final String ENEMYDOWN_SOUND_LOCATION = "/com/example/demo/sounds/enemydown.wav";
    private static final String BOSSDOWN_SOUND_LOCATION = "/com/example/demo/sounds/bossdown.wav";
    private static final String SHOOT_SOUND_LOCATION = "/com/example/demo/sounds/bullet.wav";
    private static final String LEVEL1_SOUND_LOCATION = "/com/example/demo/sounds/level1.wav";
    private static final String LEVEL2_SOUND_LOCATION = "/com/example/demo/sounds/level2.mp3";
    private static final String MAINMENU_SOUND_LOCATION = "/com/example/demo/sounds/mainmenu.wav";
    private static final String UPGRADE_SOUND_LOCATION = "/com/example/demo/sounds/upgrade.wav";
    private static final String GAMEOVER_SOUND_LOCATION = "/com/example/demo/sounds/gameover.wav";
    private static final String GETBULLET_SOUND_LOCATION = "/com/example/demo/sounds/getbullet.wav";
    private static final String GETHEALTH_SOUND_LOCATION = "/com/example/demo/sounds/gethealth.wav";

    private static Media explosionMedia;
    private static Media bossdownMedia;
    private static Media shootMedia;
    private static Media level1Media;
    private static Media level2Media;
    private static Media mainmenuMedia;
    private static Media upgradeMedia;
    private static Media gameoverMedia;
    private static Media getbulletMedia;
    private static Media gethealthMedia;
    private static MediaPlayer enemydownPlayer;
    private static MediaPlayer bossdownPlayer;
    private static MediaPlayer shootPlayer;
    private static MediaPlayer level1Player;
    private static MediaPlayer level2Player;
    private static MediaPlayer mainmenuPlayer;
    private static MediaPlayer upgradePlayer;
    private static MediaPlayer gameoverPlayer;
    private static MediaPlayer getbulletPlayer;
    private static MediaPlayer gethealthPlayer;


    static {
        try {
            // 加载爆炸声音
            explosionMedia = new Media(SoundComponent.class.getResource(ENEMYDOWN_SOUND_LOCATION).toExternalForm());
            enemydownPlayer = new MediaPlayer(explosionMedia);

            // 加载射击声音
            shootMedia = new Media(SoundComponent.class.getResource(SHOOT_SOUND_LOCATION).toExternalForm());
            shootPlayer = new MediaPlayer(shootMedia);

            //加载bossdown声音
            bossdownMedia = new Media(SoundComponent.class.getResource(BOSSDOWN_SOUND_LOCATION).toExternalForm());
            bossdownPlayer = new MediaPlayer(bossdownMedia);

            //加载level1声音
            level1Media = new Media(SoundComponent.class.getResource(LEVEL1_SOUND_LOCATION).toExternalForm());
            level1Player = new MediaPlayer(level1Media);


            level2Media = new Media(SoundComponent.class.getResource(LEVEL2_SOUND_LOCATION).toExternalForm());
            level2Player = new MediaPlayer(level2Media);

            //加载mainmenu声音
            mainmenuMedia = new Media(SoundComponent.class.getResource(MAINMENU_SOUND_LOCATION).toExternalForm());
            mainmenuPlayer = new MediaPlayer(mainmenuMedia);

            //加载upgrade声音
            upgradeMedia = new Media(SoundComponent.class.getResource(UPGRADE_SOUND_LOCATION).toExternalForm());
            upgradePlayer = new MediaPlayer(upgradeMedia);

            //加载gameover声音
            gameoverMedia = new Media(SoundComponent.class.getResource(GAMEOVER_SOUND_LOCATION).toExternalForm());
            gameoverPlayer = new MediaPlayer(gameoverMedia);

            //加载getbullet声音
            getbulletMedia = new Media(SoundComponent.class.getResource(GETBULLET_SOUND_LOCATION).toExternalForm());
            getbulletPlayer = new MediaPlayer(getbulletMedia);

            //加载gethealth声音
            gethealthMedia = new Media(SoundComponent.class.getResource(GETHEALTH_SOUND_LOCATION).toExternalForm());
            gethealthPlayer = new MediaPlayer(gethealthMedia);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放爆炸声音
     */
    public static void playExplosionSound() {
        if (enemydownPlayer != null) {
            enemydownPlayer.stop(); // 重置播放器以便重复播放
            enemydownPlayer.play();
        }
    }

    public static void stopExplosionSound() {
        if (enemydownPlayer != null) {
            enemydownPlayer.stop();
        }
    }

    /**
     * 播放射击声音
     */
    public static void playShootingSound() {
        if (shootPlayer != null) {
            shootPlayer.stop(); // 重置播放器以便重复播放
            shootPlayer.play();
        }
    }

    public static void stopShootingSound() {
        if (shootPlayer != null) {
            shootPlayer.stop();
        }
    }

    /**
     * 播放bossdown声音
     */
    public static void playBossdownSound(Runnable onFinished) {
        if (bossdownPlayer != null) {
            bossdownPlayer.stop(); // 重置播放器以便重复播放
            bossdownPlayer.setOnEndOfMedia(() -> {
                bossdownPlayer.setOnEndOfMedia(null); // 重置事件处理器
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            bossdownPlayer.play();
        } else if (onFinished != null) {
            onFinished.run(); // 如果播放器不可用，立即执行回调
        }
    }

    // 不带参数的重载方法，兼容其他调用
    public static void playBossdownSound() {
        playBossdownSound(null);
    }



    public static void stopBossdownSound() {
        if (bossdownPlayer != null) {
            bossdownPlayer.stop();
        }
    }

    /**
     * 播放level1声音
     */
    public static void playLevel1Sound() {
        if (level1Player != null) {
            level1Player.stop(); // 重置播放器以便重复播放
            level1Player.play();
        }
    }

    public static void stopLevel1Sound() {
        if (level1Player != null) {
            level1Player.stop();
        }
    }

    public static void playLevel2Sound() {
        if (level2Player != null) {
            level2Player.stop(); // 重置播放器以便重复播放
            level2Player.play();
        }
    }

    public static void stopLevel2Sound() {
        if (level2Player != null) {
            level2Player.stop();
        }
    }

    /**
     * 播放mainmenu声音
     */
    public static void playMainmenuSound() {
        if (mainmenuPlayer != null) {
            mainmenuPlayer.stop(); // 重置播放器以便重复播放
            mainmenuPlayer.play();
        }
    }

    public static void stopMainmenuSound() {
        if (mainmenuPlayer != null) {
            mainmenuPlayer.stop();
        }
    }

    /**
     * 播放upgrade声音
     */
    public static void playUpgradeSound() {
        if (upgradePlayer != null) {
            upgradePlayer.stop(); // 重置播放器以便重复播放
            upgradePlayer.play();
        }
    }

    public static void stopUpgradeSound() {
        if (upgradePlayer != null) {
            upgradePlayer.stop();
        }
    }

    /**
     * 播放gameover声音
     */
    public static void playGameoverSound() {
        if (gameoverPlayer != null) {
            gameoverPlayer.stop(); // 重置播放器以便重复播放
            gameoverPlayer.play();
        }
    }

    public static void stopGameoverSound() {
        if (gameoverPlayer != null) {
            gameoverPlayer.stop();
        }
    }

    /**
     * 播放getbullet声音
     */
    public static void playGetbulletSound() {
        if (getbulletPlayer != null) {
            getbulletPlayer.stop(); // 重置播放器以便重复播放
            getbulletPlayer.play();
        }
    }

    public static void stopGetbulletSound() {
        if (getbulletPlayer != null) {
            getbulletPlayer.stop();
        }
    }

    /**
     * 播放gethealth声音
     */
    public static void playGethealthSound() {
        if (gethealthPlayer != null) {
            gethealthPlayer.stop(); // 重置播放器以便重复播放
            gethealthPlayer.play();
        }
    }

    public static void stopGethealthSound() {
        if (gethealthPlayer != null) {
            gethealthPlayer.stop();
        }
    }

    public static void stopAllSound() {
        if (level1Player != null) {
            level1Player.stop();
        }
        if (level2Player != null) {
            level2Player.stop();
        }
        if (bossdownPlayer != null) {
            bossdownPlayer.stop();
        }
        if (shootPlayer != null) {
            shootPlayer.stop();
        }
        if (enemydownPlayer != null) {
            enemydownPlayer.stop();
        }
        if (mainmenuPlayer != null) {
            mainmenuPlayer.stop();
        }
        if (upgradePlayer != null) {
            upgradePlayer.stop();
        }
        if (gameoverPlayer != null) {
            gameoverPlayer.stop();
        }
        if (getbulletPlayer != null) {
            getbulletPlayer.stop();
        }
    }

}
