package com.example.demo.actors;

import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

public abstract class Actor extends Group {
    public abstract void updateActor(double deltaTime, LevelParent level);
}