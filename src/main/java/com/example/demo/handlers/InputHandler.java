package com.example.demo.handlers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.UserPlane;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Set;

/**
 * Handles user input for controlling the user's plane and firing projectiles.
 */
public class InputHandler {

    private final Set<KeyCode> pressedKeys;
    private final UserPlane user;
    private final ImageView background;
    private final Group root;
    private final List<ActiveActorDestructible> userProjectiles;

    /**
     * Constructs an InputHandler with the specified parameters.
     *
     * @param pressedKeys the set of currently pressed keys.
     * @param user the user's plane.
     * @param background the background image view.
     * @param root the root group of the scene.
     * @param userProjectiles the list of user projectiles.
     */
    public InputHandler(Set<KeyCode> pressedKeys, UserPlane user, ImageView background, Group root, List<ActiveActorDestructible> userProjectiles) {
        this.pressedKeys = pressedKeys;
        this.user = user;
        this.background = background;
        this.root = root;
        this.userProjectiles = userProjectiles;
    }

    /**
     * Initializes the event handlers for firing projectiles and tracking key presses.
     */
    public void initializeFireProjectileHandler() {
        background.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                pressedKeys.add(e.getCode());
                if (e.getCode() == KeyCode.SPACE) fireProjectile();
            }
        });
        background.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                pressedKeys.remove(e.getCode());
            }
        });
    }

    /**
     * Fires a projectile from the user's plane.
     */
    private void fireProjectile() {
        ActiveActorDestructible projectile = user.fireProjectile();
        root.getChildren().add(projectile);
        userProjectiles.add(projectile);
    }

    /**
     * Updates the movement of the user's plane based on the currently pressed keys.
     */
    public void updateUserPlaneMovement() {
        if (pressedKeys.contains(KeyCode.UP)) user.moveUp();
        if (pressedKeys.contains(KeyCode.DOWN)) user.moveDown();
        if (pressedKeys.contains(KeyCode.LEFT)) user.moveLeft();
        if (pressedKeys.contains(KeyCode.RIGHT)) user.moveRight();
        if (!pressedKeys.contains(KeyCode.UP) && !pressedKeys.contains(KeyCode.DOWN)) user.stopVertical();
        if (!pressedKeys.contains(KeyCode.LEFT) && !pressedKeys.contains(KeyCode.RIGHT)) user.stopHorizontal();
    }
}