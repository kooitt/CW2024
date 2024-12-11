package com.example.demo;

import com.example.demo.UserPlane;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MovementHandler{

    private UserPlane user;
    private LevelParent level;
    
    public MovementHandler(UserPlane user, LevelParent level) {
        this.user = user;
        this.level = level;
    }

    private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
            level.spawningProjectile(projectile);
        }
	}

    public void KeyPress(KeyEvent e) {
        KeyCode kc = e.getCode();
        if (kc == KeyCode.UP) user.moveUp();
        if (kc == KeyCode.DOWN) user.moveDown();
        if (kc == KeyCode.SPACE) fireProjectile();
        if (kc == KeyCode.RIGHT) user.moveRight();
        if (kc == KeyCode.LEFT) user.moveLeft();
    }

    public void KeyRelease(KeyEvent e) {
        KeyCode kc = e.getCode();
        if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.verticalStop();
        if (kc == KeyCode.RIGHT || kc == KeyCode.LEFT) user.horizontalStop();
    }


}
