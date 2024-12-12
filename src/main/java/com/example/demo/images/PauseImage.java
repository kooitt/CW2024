package com.example.demo.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PauseImage extends ImageView {

    private static final String IMAGE_NAME = "/com/example/demo/images/LevelUI/pause.png";
    private static final int HEIGHT = 200;
    private static final int WIDTH = 600;

    public PauseImage(double xPosition, double yPosition) {
        this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
        this.setVisible(false);
        this.setFitHeight(HEIGHT);
        this.setFitWidth(WIDTH);
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
    }

    public void showPauseImage() {
        this.setVisible(true);
    }

    public void hidePauseImage() {
        this.setVisible(false);
    }
}