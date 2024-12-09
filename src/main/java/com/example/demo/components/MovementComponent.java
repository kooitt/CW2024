package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;

/**
 * Represents a movement component that controls the velocity and movement of an {@link Actor}.
 * This component updates the position of an actor based on its velocity in the X and Y directions.
 */
public class MovementComponent {

    /**
     * The horizontal velocity of the actor.
     */
    private double velocityX;

    /**
     * The vertical velocity of the actor.
     */
    private double velocityY;

    /**
     * Constructs a new {@code MovementComponent} with the specified velocities.
     *
     * @param velocityX the initial velocity along the X-axis.
     * @param velocityY the initial velocity along the Y-axis.
     */
    public MovementComponent(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Updates the position of the specified actor based on the current velocity values.
     * The actor's translation in both the X and Y directions is incremented by the
     * respective velocity values.
     *
     * @param actor the actor whose position is updated.
     */
    public void update(Actor actor) {
        actor.setTranslateX(actor.getTranslateX() + velocityX);
        actor.setTranslateY(actor.getTranslateY() + velocityY);
    }

    /**
     * Sets the velocity of the actor in both the X and Y directions.
     *
     * @param velocityX the new velocity along the X-axis.
     * @param velocityY the new velocity along the Y-axis.
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Returns the current velocity of the actor along the X-axis.
     *
     * @return the velocity in the X direction.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the current velocity of the actor along the Y-axis.
     *
     * @return the velocity in the Y direction.
     */
    public double getVelocityY() {
        return velocityY;
    }
}
