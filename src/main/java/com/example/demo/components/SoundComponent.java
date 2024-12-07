package com.example.demo.components;

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
            "/com/example/demo/sounds/gethealth.wav"
    };

    private static Media[] media = new Media[SOUND_LOCATIONS.length];
    private static MediaPlayer[] players = new MediaPlayer[SOUND_LOCATIONS.length];

    static {
        try {
            for (int i = 0; i < SOUND_LOCATIONS.length; i++) {
                media[i] = new Media(SoundComponent.class.getResource(SOUND_LOCATIONS[i]).toExternalForm());
                players[i] = new MediaPlayer(media[i]);
            }
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

    public static void playSound(int index) {
        if (players[index] != null) {
            players[index].stop();
            players[index].play();
        }
    }

    public static void stopSound(int index) {
        if (players[index] != null) {
            players[index].stop();
        }
    }
    public static void playExplosionSound() {
        playSound(0); // Assuming index 0 is for explosion sound
    }
    public static void playBossdownSound(Runnable onFinished) {
        if (players[1] != null) {
            players[1].stop();
            players[1].setOnEndOfMedia(() -> {
                players[1].setOnEndOfMedia(null);
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            players[1].play();
        } else if (onFinished != null) {
            onFinished.run();
        }
    }

    public static void playBossdownSound() {
        playBossdownSound(null);
    }

    public static void playShootingSound() {
        playSound(2);
    }

    public static void playLevel1Sound() {
        playSound(3);
        currentBGM = players[3];
    }

    public static void playLevel2Sound() {
        playSound(4);
        currentBGM = players[4];
    }

    public static void playMainmenuSound() {
        playSound(5);
    }

    public static void playUpgradeSound() {
        playSound(6);
    }

    public static void playGameoverSound() {
        playSound(7);
    }

    public static void playGetbulletSound() {
        playSound(8);
    }

    public static void playGethealthSound() {
        playSound(9);
    }

    public static void stopLevel1Sound() {
        stopSound(3);
    }

    public static void stopLevel2Sound() {
        stopSound(4);
    }

    public static void stopAllSound() {
        currentBGM = null;
        for (MediaPlayer player : players) {
            if (player != null) {
                player.stop();
            }
        }
    }

}