// MovementComponent.java
package com.example.demo.components;

import com.example.demo.actors.Actor;

/**
 * Manages movement based on velocity.
 */
public class MovementComponent {

    private double velocityX;
    private double velocityY;

    /**
     * Constructs a MovementComponent with specified velocities.
     *
     * @param velocityX horizontal velocity.
     * @param velocityY vertical velocity.
     */
    public MovementComponent(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Updates the actor's position based on velocity.
     *
     * @param actor the actor to move.
     */
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
