package com.example.demo.utils;

import javafx.scene.input.KeyCode;

/**
 * A singleton class for managing key bindings used in the application.
 * This class provides a centralized way to manage key mappings for up, down, left, and right actions.
 */
public class KeyBindings {

    /**
     * Singleton instance of the KeyBindings class.
     */
    private static KeyBindings instance;

    /**
     * Key binding for the "up" action.
     */
    private KeyCode upKey;

    /**
     * Key binding for the "down" action.
     */
    private KeyCode downKey;

    /**
     * Key binding for the "left" action.
     */
    private KeyCode leftKey;

    /**
     * Key binding for the "right" action.
     */
    private KeyCode rightKey;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the key bindings to default values:
     * <ul>
     *     <li>Up key: {@link KeyCode#UP}</li>
     *     <li>Down key: {@link KeyCode#DOWN}</li>
     *     <li>Left key: {@link KeyCode#LEFT}</li>
     *     <li>Right key: {@link KeyCode#RIGHT}</li>
     * </ul>
     */
    private KeyBindings() {
        upKey = KeyCode.UP;
        downKey = KeyCode.DOWN;
        leftKey = KeyCode.LEFT;
        rightKey = KeyCode.RIGHT;
    }

    /**
     * Gets the singleton instance of the KeyBindings class.
     * If the instance does not already exist, it will be created.
     *
     * @return the singleton instance of the KeyBindings class.
     */
    public static KeyBindings getInstance() {
        if (instance == null) instance = new KeyBindings();
        return instance;
    }

    /**
     * Gets the current key binding for the "up" action.
     *
     * @return the key binding for the "up" action.
     */
    public KeyCode getUpKey() {
        return upKey;
    }

    /**
     * Sets the key binding for the "up" action.
     *
     * @param upKey the new key binding for the "up" action.
     */
    public void setUpKey(KeyCode upKey) {
        this.upKey = upKey;
    }

    /**
     * Gets the current key binding for the "down" action.
     *
     * @return the key binding for the "down" action.
     */
    public KeyCode getDownKey() {
        return downKey;
    }

    /**
     * Sets the key binding for the "down" action.
     *
     * @param downKey the new key binding for the "down" action.
     */
    public void setDownKey(KeyCode downKey) {
        this.downKey = downKey;
    }

    /**
     * Gets the current key binding for the "left" action.
     *
     * @return the key binding for the "left" action.
     */
    public KeyCode getLeftKey() {
        return leftKey;
    }

    /**
     * Sets the key binding for the "left" action.
     *
     * @param leftKey the new key binding for the "left" action.
     */
    public void setLeftKey(KeyCode leftKey) {
        this.leftKey = leftKey;
    }

    /**
     * Gets the current key binding for the "right" action.
     *
     * @return the key binding for the "right" action.
     */
    public KeyCode getRightKey() {
        return rightKey;
    }

    /**
     * Sets the key binding for the "right" action.
     *
     * @param rightKey the new key binding for the "right" action.
     */
    public void setRightKey(KeyCode rightKey) {
        this.rightKey = rightKey;
    }
}
