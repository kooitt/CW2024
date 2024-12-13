package com.example.demo.levels;

import com.example.demo.actors.shield.ShieldImage;
import javafx.scene.Group;

public class LevelFourView extends LevelParentView {

    private static final int SHIELD_X_POSITION = 600;
    private static final int SHIELD_Y_POSITION = 1500;
    private final Group root;
    private final ShieldImage shieldImage;

    public LevelFourView(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.root = root;
        this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();
    }

    private void addImagesToRoot() {
        root.getChildren().addAll(shieldImage);
    }

}

