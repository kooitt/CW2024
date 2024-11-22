package com.example.demo.config;

/**
 * Configuration class that holds constant values used throughout the application.
 */
public final class GameConfig {
    // Make class non-instantiable
    private GameConfig() {}

    // Screen dimensions
    public static final double SCREEN_WIDTH = 1300;
    public static final double SCREEN_HEIGHT = 750;

    // Game title
    public static final String TITLE = "Sky Battle";

    // Player health
    public static final int PLAYER_INITIAL_HEALTH = 5;
}