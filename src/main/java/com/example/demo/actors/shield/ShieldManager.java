package com.example.demo.actors.shield;

import com.example.demo.actors.planes.bosses.Boss;
import com.example.demo.actors.planes.bosses.BossManager;
import javafx.scene.image.ImageView;

public class ShieldManager extends ImageView {

    private static final int MAX_FRAMES_WITH_SHIELD = 500; // active for ~8.33 seconds
    private static final double BOSS_SHIELD_PROBABILITY = 0.002;
    public boolean isShielded;
    public int framesWithShieldActivated;
    private final ShieldImage shieldImage;
    private final BossManager bossManager;

    public ShieldManager(ShieldImage shieldImage, BossManager bossManager) {
        this.shieldImage = shieldImage;
        this.isShielded = false;
        this.framesWithShieldActivated = 0;
        this.bossManager = bossManager;
    }

    public void updateShieldPosition(){
        double shieldXOffset = - bossManager.getImageWidth() * 0.1;
        shieldImage.setLayoutX(bossManager.getLayoutX() + shieldXOffset);
        shieldImage.setLayoutY(bossManager.getLayoutY() + bossManager.getTranslateY() - bossManager.getImageHeight() / 4.0);
    }

    public void updateShield() {
        if (isShielded) {
            framesWithShieldActivated++;
            System.out.println("Shield Active: " + isShielded + ", Frames: " + framesWithShieldActivated);
        }
        else if (shieldShouldBeActivated()) {
            activateShield();
            showShield(); // show shield when activated
            System.out.println("Shield Active: " + isShielded + ", Frames: " + framesWithShieldActivated);
        }
        if (shieldExhausted()) {
            deactivateShield();
            hideShield(); // hide shield when exhausted
            System.out.println("Shield Active: " + isShielded + ", Frames: " + framesWithShieldActivated);
        }
    }

    public void showShield() {
        shieldImage.setVisible(true);
        System.out.println("Shield is now visible");
    }

    public void hideShield() {
        shieldImage.setVisible(false);
        System.out.println("Shield is now hidden");
    }

    public boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    public boolean shieldExhausted() {
        return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
    }

    public void activateShield() {
        isShielded = true;
    }

    public void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
    }
}