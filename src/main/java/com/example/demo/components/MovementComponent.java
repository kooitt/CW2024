// MovementComponent.java
package com.example.demo.components;

import com.example.demo.actors.Actor;

public class MovementComponent {

    private double velocityX;
    private double velocityY;

    public MovementComponent(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void update(Actor actor) {
        actor.setTranslateX(actor.getTranslateX() + velocityX);
        actor.setTranslateY(actor.getTranslateY() + velocityY);
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
}
