// Actor.java
package com.example.demo.actors;

import com.example.demo.levels.LevelParent;
import javafx.scene.Group;

/**
 * Abstract base class for all actors in the game.
 * An actor is a game entity that can be updated and rendered.
 */
public abstract class Actor extends Group {

    /**
     * Updates the actor's state. Called once per frame.
     *
     * @param deltaTime time elapsed since last update in seconds.
     * @param level     current game level.
     */
    public abstract void updateActor(double deltaTime, LevelParent level);
}
