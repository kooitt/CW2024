package com.example.demo.interfaces;

/**
 * The Hitbox interface defines the methods that must be implemented
 * by any class representing an object with a rectangular hitbox.
 * It provides methods to retrieve the x and y coordinates, width,
 * and height of the hitbox.
 */
public interface Hitbox {

    /**
     * Gets the x-coordinate of the hitbox.
     *
     * @return the x-coordinate of the hitbox as a double.
     */
    double getHitboxX();

    /**
     * Gets the y-coordinate of the hitbox.
     *
     * @return the y-coordinate of the hitbox as a double.
     */
    double getHitboxY();

    /**
     * Gets the width of the hitbox.
     *
     * @return the width of the hitbox as a double.
     */
    double getHitboxWidth();

    /**
     * Gets the height of the hitbox.
     *
     * @return the height of the hitbox as a double.
     */
    double getHitboxHeight();
}
