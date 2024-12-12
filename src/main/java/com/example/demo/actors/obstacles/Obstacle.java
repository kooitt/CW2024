package com.example.demo.actors.obstacles;

import com.example.demo.actors.ActiveActorDestructible;

public abstract class Obstacle extends ActiveActorDestructible {

    private int health;

    public Obstacle(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.health = health;
    }

    @Override
    public void takeDamage() {
        health--;
        if (healthAtZero()) {
            this.destroy();
        }
    }

    private boolean healthAtZero() {
        return health == 0;
    }

    public int getHealth() {
        return health;
    }
}