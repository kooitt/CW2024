package com.example.demo.actors.shield;

import com.example.demo.actors.planes.Boss;
import javafx.scene.image.ImageView;

public class ShieldManager extends ImageView {

    private static final int MAX_FRAMES_WITH_SHIELD = 500; // active for ~8.33 seconds
    private static final double BOSS_SHIELD_PROBABILITY = 0.002;
    public boolean isShielded;
    private int framesWithShieldActivated;
    private final ShieldImage shieldImage;
    private final Boss boss;

    public ShieldManager(ShieldImage shieldImage, Boss boss) {
        this.shieldImage = shieldImage;
        this.isShielded = false;
        this.framesWithShieldActivated = 0;
        this.boss = boss;
    }

    public void updateShieldPosition(){
        double shieldXOffset = - boss.getImageWidth() * 0.1;
        shieldImage.setLayoutX(boss.getLayoutX() + shieldXOffset);
        shieldImage.setLayoutY(boss.getLayoutY() + boss.getTranslateY() - boss.getImageHeight() / 4.0);
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

    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    private boolean shieldExhausted() {
        return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
    }

    private void activateShield() {
        isShielded = true;
    }

    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
    }
}