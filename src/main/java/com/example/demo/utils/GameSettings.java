package com.example.demo.utils;

/**
 * A utility class that holds global game settings for the application.
 * This class provides a centralized place to manage and modify settings that affect
 * the behavior or appearance of the game.
 * <p>
 * Currently, it contains a single boolean flag to control whether hitboxes should
 * be displayed during gameplay.
 * </p>
 */
public class GameSettings {

    /**
     * A boolean flag indicating whether to show hitboxes for game entities.
     * <p>
     * - If set to {@code true}, hitboxes for all game entities (e.g., players, enemies, projectiles)
     *   will be rendered on the screen. This is typically useful for debugging purposes to visualize
     *   collision areas.
     * - If set to {@code false}, hitboxes will not be displayed.
     * </p>
     * <p>
     * Default value: {@code false}.
     * </p>
     */
    public static boolean SHOW_HITBOXES = false;
}
