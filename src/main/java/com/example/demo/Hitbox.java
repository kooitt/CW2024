package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hitbox {
    private final Rectangle hitbox;

    public Hitbox(double initialXPos, double initialYPos, int imageWidth, int imageHeight){
        //initialize hitbox with same size as the plane's image
        this.hitbox = new Rectangle(initialXPos, initialYPos, imageWidth, imageHeight); // imageWidth should be declared
        hitbox.setStroke(Color.TRANSPARENT);  // change to a colour to visualize the hitbox for debugging
        hitbox.setFill(Color.TRANSPARENT);  // transparent inside
    }

    // get the hitbox
    public Rectangle getHitbox() {
        return hitbox;
    }

    // update the hitbox position to match the plane position
    public void updatePosition(double newX, double newY) {
        hitbox.setX(newX);
        hitbox.setY(newY);
    }
}
