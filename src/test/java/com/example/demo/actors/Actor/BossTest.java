package com.example.demo.actors.Actor; // Adjust package path according to the project structure

import com.example.demo.levels.LevelParent;
import com.example.demo.components.SoundComponent;
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
 * Unit test class for testing the Boss actor in the game.
 * This class includes tests for Boss's health, damage handling, destruction,
 * and health bar updates. It uses JUnit 5 for testing.
 */
public class BossTest {

    /**
     * The Boss instance being tested.
     */
    private Boss boss;

    /**
     * A mocked instance of LevelParent, representing the game level.
     */
    private LevelParent mockLevel;

    /**
     * A mocked root node for testing, used to simulate the game scene graph.
     */
    private Group mockRoot;

    /**
     * Initializes the JavaFX environment required for testing.
     * This method ensures that JavaFX-related components can be tested
     * in a headless environment.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // Required to initialize JavaFX toolkit
        Platform.setImplicitExit(false);
    }

    /**
     * Sets up the testing environment by initializing the mock objects and
     * the Boss instance before each test.
     */
    @BeforeEach
    void setUp() {
        mockRoot = new Group();
        mockLevel = Mockito.mock(LevelParent.class);
        boss = new Boss(mockRoot, mockLevel);
    }

    /**
     * Tests the initial health of the Boss.
     * Ensures that the Boss starts with the maximum health of 800.
     */
    @Test
    void testInitialHealth() {
        assertEquals(800, boss.getMaxHealth(), "Boss's initial max health should be 800.");
        assertEquals(800, boss.getCurrentHealth(), "Boss's initial current health should be 800.");
    }

    /**
     * Tests the behavior of the Boss when taking damage.
     * Ensures that the health decreases correctly and that the Boss is marked
     * as destroyed when its health reaches zero.
     */
    @Test
    void testTakeDamage() {
        boss.takeDamage(100); // Deal 100 damage
        assertEquals(700, boss.getCurrentHealth(), "Boss's health should decrease correctly after taking damage.");

        boss.takeDamage(700); // Deal enough damage to destroy the Boss
        assertEquals(0, boss.getCurrentHealth(), "Boss's health should be zero when destroyed.");
        assertTrue(boss.isDestroyed(), "Boss should be marked as destroyed when its health reaches zero.");
    }

    /**
     * Tests the destruction of the Boss.
     * Ensures that the Boss is marked as destroyed and all associated behaviors
     * are triggered when the destroy() method is called.
     */
    @Test
    void testDestroy() {
        boss.destroy();
        assertTrue(boss.isDestroyed(), "Boss should be destroyed after destroy() call.");
    }

    /**
     * Tests the health bar update behavior of the Boss.
     * Ensures that the health bar reflects the current health proportion of the Boss.
     */
    @Test
    void testHealthBarUpdate() {
        ProgressBar healthBar = (ProgressBar) boss.getChildren()
                .stream()
                .filter(node -> node instanceof ProgressBar)
                .findFirst()
                .orElse(null);

        assertNotNull(healthBar, "HealthBar should be present in Boss children.");
        assertEquals(1.0, healthBar.getProgress(), 0.0001, "Initial health bar progress should be full (1.0).");

        boss.takeDamage(400); // Deal damage to reduce health
        Platform.runLater(() -> {
            double expectedProgress = 400.0 / 800.0; // Remaining health proportion
            assertEquals(expectedProgress, healthBar.getProgress(), 0.0001,
                    "HealthBar progress should reflect the current health proportion.");
        });
    }
}
