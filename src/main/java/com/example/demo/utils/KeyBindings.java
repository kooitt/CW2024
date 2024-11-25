// KeyBindings.java

package com.example.demo.utils;

import javafx.scene.input.KeyCode;

public class KeyBindings {
    private static KeyBindings instance;

    private KeyCode upKey;
    private KeyCode downKey;
    private KeyCode leftKey;
    private KeyCode rightKey;
    // 移除 fireKey，因为不再需要

    private KeyBindings() {
        // 默认按键绑定
        upKey = KeyCode.UP;
        downKey = KeyCode.DOWN;
        leftKey = KeyCode.LEFT;
        rightKey = KeyCode.RIGHT;
        // fireKey = KeyCode.SPACE; // 移除
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
        this.upKey = upKey;
    }

    public KeyCode getDownKey() {
        return downKey;
    }

    public void setDownKey(KeyCode downKey) {
        this.downKey = downKey;
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(KeyCode leftKey) {
        this.leftKey = leftKey;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public void setRightKey(KeyCode rightKey) {
        this.rightKey = rightKey;
    }

    // 移除 getFireKey() 和 setFireKey() 方法
}
