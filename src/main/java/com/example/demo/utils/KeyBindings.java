package com.example.demo.utils;

import javafx.scene.input.KeyCode;

public class KeyBindings {

    private static KeyBindings instance;

    private KeyCode upKey;
    private KeyCode downKey;
    private KeyCode fireKey;

    private KeyBindings() {
        upKey = KeyCode.UP;
        downKey = KeyCode.DOWN;
        fireKey = KeyCode.SPACE;
    }

    public static KeyBindings getInstance() {
        if (instance == null) {
            instance = new KeyBindings();
        }
        return instance;
    }

    public KeyCode getUpKey() {
        return upKey;
    }

    public void setUpKey(KeyCode upKey) {
        if (upKey != null) {
            this.upKey = upKey;
        }
    }

    public KeyCode getDownKey() {
        return downKey;
    }

    public void setDownKey(KeyCode downKey) {
        if (downKey != null) {
            this.downKey = downKey;
        }
    }

    public KeyCode getFireKey() {
        return fireKey;
    }

    public void setFireKey(KeyCode fireKey) {
        if (fireKey != null) {
            this.fireKey = fireKey;
        }
    }
}
