// HealthComponent.java

package com.example.demo.components;

import com.example.demo.actors.ActiveActor;

public class HealthComponent {
    private int maxHealth;
    private int currentHealth;
    private ActiveActor owner;

    public HealthComponent(ActiveActor owner, int maxHealth) {
        this.owner = owner;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            owner.destroy();
        }
    }

    public void heal(int amount) {
        currentHealth += amount;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int health) {
        currentHealth = Math.max(0, Math.min(health, maxHealth));
        if (currentHealth == 0) {
            owner.destroy();
        }
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void reset() {
        currentHealth = maxHealth;
    }
}
