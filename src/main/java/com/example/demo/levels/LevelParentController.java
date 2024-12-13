package com.example.demo.levels;

import com.example.demo.actors.core.ActiveActorDestructible;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LevelParentController {
    private final LevelParent model;
    private final LevelParentView view;
    private static final KeyCode FIRE_KEY = KeyCode.SPACE;
    private static final KeyCode MOVE_UP_KEY = KeyCode.UP;
    private static final KeyCode MOVE_DOWN_KEY = KeyCode.DOWN;

    public LevelParentController(LevelParent model, LevelParentView view){
        this.model = model;
        this.view = view;
        setUpKeyHandlers();
    }

    private void setUpKeyHandlers(){
        model.getRoot().setOnKeyPressed(this::handleKeyPress);
        model.getRoot().setOnKeyReleased(this::handleKeyRelease);
    }

    private void handleKeyPress(KeyEvent e){
        KeyCode kc = e.getCode();
        if (kc == MOVE_UP_KEY) model.getUser().moveUp();
        if (kc == MOVE_DOWN_KEY) model.getUser().moveDown();
        if (kc == FIRE_KEY) fireProjectile();
    }

    private void handleKeyRelease(KeyEvent e){
        KeyCode kc = e.getCode();
        if (kc == MOVE_UP_KEY || kc == MOVE_DOWN_KEY) model.getUser().stop();
    }

    private void fireProjectile() {
        ActiveActorDestructible projectile = model.getUser().fireProjectile();
        model.getRoot().getChildren().add(projectile);
        model.getUserProjectile().add(projectile);
    }

    public void winGame(){
        model.stopGameLoop();
        view.showWinScreen();
    }

    public void loseGame(){
        model.stopGameLoop();
        view.showLoseScreen();
    }

    public void updateLevelView(){
        view.updateHeartCount(model.getUser().getHealth());
    }
}
