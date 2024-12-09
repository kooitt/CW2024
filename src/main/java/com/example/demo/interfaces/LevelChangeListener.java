package com.example.demo.interfaces;

/**
 * Interface to listen for level change events in the game.
 * Implementations of this interface can be used to handle the transition between levels.
 */
public interface LevelChangeListener {

    /**
     * This method is triggered when a level change occurs in the game.
     *
     * @param nextLevelName the name of the next level to transition to. It is expected to be
     *                      a string representing the identifier or class name of the next level.
     */
    void onLevelChange(String nextLevelName);
}
