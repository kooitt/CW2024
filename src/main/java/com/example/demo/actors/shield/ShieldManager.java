package com.example.demo.actors.shield;

import com.example.demo.actors.planes.bosses.Boss;
import com.example.demo.actors.planes.bosses.BossManager;
import com.example.demo.actors.planes.bosses.MonstrousNightmare;
import javafx.scene.image.ImageView;

public class ShieldManager extends ImageView {

    private static final int MAX_FRAMES_WITH_SHIELD = 300; // active for ~8.33 seconds
    private static final double BOSS_SHIELD_PROBABILITY = 0.01;
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
        double shieldXOffset;
        double shieldYOffset;

        if (bossManager instanceof Boss) {
            shieldXOffset = - bossManager.getImageWidth() * 0.1;
            shieldYOffset = bossManager.getImageHeight() / 4.0; // Custom offset for Boss1
        } else if (bossManager instanceof MonstrousNightmare) {
            shieldXOffset = - bossManager.getImageWidth() * 0.25;
            shieldYOffset = bossManager.getImageHeight() / 20.0; // Custom offset for Boss2
        } else {
            shieldXOffset = - bossManager.getImageWidth() * 0.1;
            shieldYOffset = bossManager.getImageHeight() / 4.0; // Default offset
        }

        shieldImage.setLayoutX(bossManager.getLayoutX() + shieldXOffset);
        shieldImage.setLayoutY(bossManager.getLayoutY() + bossManager.getTranslateY() - shieldYOffset);
    }

    public void updateShield() {
        if (isShielded) {
            framesWithShieldActivated++;
        }
        else if (shieldShouldBeActivated()) {
            activateShield();
            showShield(); // show shield when activated
        }
        if (shieldExhausted()) {
            deactivateShield();
            hideShield(); // hide shield when exhausted
        }
    }

    public void showShield() {
        shieldImage.setVisible(true);
    }

    public void hideShield() {
        shieldImage.setVisible(false);
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