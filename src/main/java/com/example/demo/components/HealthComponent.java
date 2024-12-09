package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a component for managing the health of an {@link Actor}.
 * It notifies registered observers when the health changes.
 */
public class HealthComponent {

    /**
     * The maximum health value.
     */
    private int maxHealth;

    /**
     * The current health value.
     */
    private int currentHealth;

    /**
     * The owner of this health component.
     */
    private final Actor owner;

    /**
     * The list of health observers.
     */
    private final List<HealthObserver> observers = new CopyOnWriteArrayList<>();

    /**
     * Constructs a {@code HealthComponent} with the specified owner and maximum health.
     *
     * @param owner     The {@link Actor} that owns this health component.
     * @param maxHealth The maximum health value.
     */
    public HealthComponent(Actor owner, int maxHealth) {
        this.owner = owner;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * Reduces the current health by a specified amount and notifies observers.
     * If health reaches zero, the owner is destroyed.
     *
     * @param damage The amount of damage to apply.
     */
    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
        notifyObservers();
        if (currentHealth == 0) owner.destroy();
    }

    /**
     * Increases the current health by a specified amount, up to the maximum health.
     * Notifies observers of the updated health.
     *
     * @param amount The amount to heal.
     */
    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
        notifyObservers();
    }

    /**
     * Gets the current health value.
     *
     * @return The current health.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the maximum health value.
     *
     * @return The maximum health.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the current health to a specific value within the range [0, maxHealth].
     * Notifies observers of the updated health.
     *
     * @param health The new health value.
     */
    public void setCurrentHealth(int health) {
        currentHealth = Math.max(0, Math.min(health, maxHealth));
        notifyObservers();
        if (currentHealth == 0) owner.destroy();
    }

    /**
     * Sets the maximum health to a specific value.
     * Adjusts the current health if it exceeds the new maximum health.
     * Notifies observers of the updated health.
     *
     * @param maxHealth The new maximum health value.
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) currentHealth = maxHealth;
        notifyObservers();
    }

    /**
     * Resets the current health to the maximum health value.
     * Notifies observers of the updated health.
     */
    public void reset() {
        currentHealth = maxHealth;
        notifyObservers();
    }

    /**
     * Notifies all observers of the current health value.
     */
    private void notifyObservers() {
        for (HealthObserver observer : observers) {
            observer.onHealthUpdated(currentHealth);
        }
    }

    /**
     * Interface for observing health changes.
     */
    public interface HealthObserver {
        /**
         * Called when the health value is updated.
         *
         * @param newHealth The updated health value.
         */
        void onHealthUpdated(int newHealth);
    }
}
