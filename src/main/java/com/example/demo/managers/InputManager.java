package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.planes.UserPlane;
import com.example.demo.config.GameState;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Set;

public class InputManager {
    private final Set<KeyCode> pressedKeys;
    private final UserPlane user;
    private final ImageView background;
    private final Group root;
    private final List<ActiveActorDestructible> userProjectiles;
    private GameState gameState = GameState.ACTIVE;

    public InputManager(Set<KeyCode> pressedKeys, UserPlane user, ImageView background, Group root, List<ActiveActorDestructible> userProjectiles) {
        this.pressedKeys = pressedKeys;
        this.user = user;
        this.background = background;
        this.root = root;
        this.userProjectiles = userProjectiles;
    }

    public void initializeFireProjectileHandler() {
        background.setOnKeyPressed(e -> {
            if (gameState == GameState.ACTIVE) {
                pressedKeys.add(e.getCode());
                if (e.getCode() == KeyCode.SPACE) fireProjectile();
            }
        });
        background.setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
            if (gameState != GameState.ACTIVE) {
                stopAllMovement();
            }
        });
    }

    private void fireProjectile() {
        if (gameState == GameState.ACTIVE) {
            ActiveActorDestructible projectile = user.fireProjectile();
            root.getChildren().add(projectile);
            userProjectiles.add(projectile);
        }
    }

    public void updateUserPlaneMovement() {
        if (gameState == GameState.ACTIVE) {
            if (pressedKeys.contains(KeyCode.UP)) user.moveUp();
            if (pressedKeys.contains(KeyCode.DOWN)) user.moveDown();
            if (pressedKeys.contains(KeyCode.LEFT)) user.moveLeft();
            if (pressedKeys.contains(KeyCode.RIGHT)) user.moveRight();
            if (!pressedKeys.contains(KeyCode.UP) && !pressedKeys.contains(KeyCode.DOWN)) user.stopVertical();
            if (!pressedKeys.contains(KeyCode.LEFT) && !pressedKeys.contains(KeyCode.RIGHT)) user.stopHorizontal();
        } else {
            stopAllMovement();
        }
    }

    private void stopAllMovement() {
        pressedKeys.clear();
        user.stopVertical();
        user.stopHorizontal();
    }

    public void setGameState(GameState state) {
        this.gameState = state;
        if (state != GameState.ACTIVE) {
            stopAllMovement();
        }
    }
}