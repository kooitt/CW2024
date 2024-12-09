package com.example.demo.actors.Actor; // Please adjust according to your actual package path

import com.example.demo.levels.LevelParent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BossTwo} class.
 * This class verifies the functionality of the BossTwo actor,
 * including its health mechanics, destruction behavior, and health bar updates.
 */
public class BossTwoTest {

    /** Instance of the BossTwo actor being tested. */
    private BossTwo bossTwo;

    /** Mock instance of {@link LevelParent} representing the game level. */
    private LevelParent mockLevel;

    /** Mock root group for JavaFX components. */
    private Group mockRoot;

    /**
     * Initializes the JavaFX toolkit for testing purposes.
     * This method ensures that JavaFX components can be created in a test environment.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // Initializes JavaFX environment
        Platform.setImplicitExit(false); // Prevents the application from exiting after tests
    }

    /**
     * Sets up the test environment before each test.
     * Creates mock objects and initializes the {@link BossTwo} instance.
     */
    @BeforeEach
    void setUp() {
        mockRoot = new Group(); // Mocked JavaFX root node
        mockLevel = Mockito.mock(LevelParent.class); // Mocked game level
        bossTwo = new BossTwo(mockRoot, mockLevel); // BossTwo instance under test
    }

    /**
     * Tests the initial health values of the {@link BossTwo} instance.
     * Ensures that the maximum and current health are correctly set upon initialization.
     */
    @Test
    void testInitialHealth() {
        assertEquals(500, bossTwo.getMaxHealth(), "BossTwo's max health should be initialized to 500.");
        assertEquals(500, bossTwo.getCurrentHealth(), "BossTwo's current health should be initialized to 500.");
        assertFalse(bossTwo.isDestroyed(), "BossTwo should not be destroyed initially.");
    }

    /**
     * Tests the {@link BossTwo#takeDamage(int)} method.
     * Ensures that health decreases correctly and the actor is marked as destroyed when health reaches zero.
     */
    @Test
    void testTakeDamage() {
        bossTwo.takeDamage(100); // Inflicts 100 damage
        assertEquals(400, bossTwo.getCurrentHealth(), "BossTwo's health should decrease by the damage taken.");

        bossTwo.takeDamage(400); // Inflicts another 400 damage, reducing health to zero
        assertEquals(0, bossTwo.getCurrentHealth(), "BossTwo's health should be zero after taking lethal damage.");
        assertTrue(bossTwo.isDestroyed(), "BossTwo should be destroyed after its health is depleted.");
    }

    /**
     * Tests the {@link BossTwo#destroy()} method.
     * Ensures that the actor is correctly marked as destroyed when this method is called.
     */
    @Test
    void testDestroy() {
        bossTwo.destroy(); // Explicitly destroys the BossTwo instance
        assertTrue(bossTwo.isDestroyed(), "BossTwo should be destroyed after destroy() call.");
    }

    /**
     * Tests the health bar update mechanism of {@link BossTwo}.
     * Ensures that the progress bar correctly reflects the actor's current health.
     */
    @Test
    void testHealthBarUpdate() {
        // Retrieves the health bar from BossTwo's children
        ProgressBar healthBar = (ProgressBar) bossTwo.getChildren().stream()
                .filter(node -> node instanceof ProgressBar)
                .findFirst()
                .orElse(null);

        assertNotNull(healthBar, "HealthBar should be present in BossTwo's children.");
        assertEquals(1.0, healthBar.getProgress(), 0.0001, "Initial healthbar progress should be full (1.0).");

        // Inflicts damage and checks if the progress bar reflects the current health
        bossTwo.takeDamage(250);
        Platform.runLater(() -> {
            double expectedProgress = 250.0 / 500.0; // Expected progress is proportional to remaining health
            assertEquals(expectedProgress, healthBar.getProgress(), 0.0001,
                    "HealthBar progress should reflect the proportion of remaining health.");
        });
    }
}
