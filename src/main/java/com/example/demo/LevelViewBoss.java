package com.example.demo;

import javafx.scene.Group;

public class LevelViewBoss extends LevelView {

    private static final int SHIELD_X_POSITION = 1150;
    private static final int SHIELD_Y_POSITION = 500;
    private final Group root;
    private final ShieldImage shieldImage;

    public LevelViewBoss(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.root = root;
        this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();
    }

    // Add the shield image to the game root
    private void addImagesToRoot() {
        root.getChildren().add(shieldImage);
    }

    // Show the boss's shield
    public void showShield() {
        shieldImage.showShield();
    }

    // Hide the boss's shield
    public void hideShield() {
        shieldImage.hideShield();
    }
}
