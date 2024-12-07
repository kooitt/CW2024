package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import java.util.Observable;

public class HealthComponent extends Observable {
    private int maxHealth;
    private int currentHealth;
    private Actor owner;

    public HealthComponent(Actor owner, int maxHealth) {
        this.owner = owner;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
        setChanged();
        notifyObservers(currentHealth);
        if (currentHealth == 0) owner.destroy();
    }

    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
        setChanged();
        notifyObservers(currentHealth);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int health) {
        currentHealth = Math.max(0, Math.min(health, maxHealth));
        setChanged();
        notifyObservers(currentHealth);
        if (currentHealth == 0) owner.destroy();
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) currentHealth = maxHealth;
        setChanged();
        notifyObservers(currentHealth);
    }

    public void reset() {
        currentHealth = maxHealth;
        setChanged();
        notifyObservers(currentHealth);
    }
}
