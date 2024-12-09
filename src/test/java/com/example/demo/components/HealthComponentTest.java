package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link HealthComponent} class.
 *
 * <p>This test suite verifies the behavior of the HealthComponent,
 * including health management, damage handling, healing, and reset operations.
 * The HealthComponent is used to manage the health of an {@link Actor}.
 */
class HealthComponentTest {

    /**
     * Mock instance of the {@link Actor} class.
     * Represents the actor whose health is managed by the {@link HealthComponent}.
     */
    private Actor mockActor;

    /**
     * The {@link HealthComponent} instance being tested.
     * Manages the health of the associated {@link Actor}.
     */
    private HealthComponent healthComponent;

    /**
     * Mock instance of the {@link LevelParent} class.
     * Represents the game level where the {@link Actor} resides.
     */
    private LevelParent mockLevel;

    /**
     * Initializes the JavaFX runtime.
     * This is necessary because the tests involve JavaFX components.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // Initializes JavaFX toolkit
    }

    /**
     * Sets up the test environment before each test.
     * <ul>
     * <li>Mocks a {@link LevelParent} instance.</li>
     * <li>Creates a mock {@link Actor} with predefined attributes.</li>
     * <li>Initializes the {@link HealthComponent} with a maximum health of 10.</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        mockLevel = mock(LevelParent.class);

        mockActor = new Actor("heart.png", 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
                // No behavior needed for the test
            }
        };
        healthComponent = new HealthComponent(mockActor, 10);
    }

    /**
     * Tests the {@link HealthComponent#takeDamage(int)} method.
     * <ul>
     * <li>Verifies that health decreases by the specified damage amount.</li>
     * <li>Ensures the actor is destroyed when health reaches zero.</li>
     * </ul>
     */
    @Test
    void testTakeDamage() {
        // Apply damage and verify remaining health
        healthComponent.takeDamage(3);
        assertEquals(7, healthComponent.getCurrentHealth());

        // Apply enough damage to deplete health and verify actor destruction
        healthComponent.takeDamage(10);
        assertEquals(0, healthComponent.getCurrentHealth());
        assertTrue(mockActor.isDestroyed());
    }

    /**
     * Tests the {@link HealthComponent#heal(int)} method.
     * <ul>
     * <li>Verifies that health increases by the specified amount, up to the maximum health.</li>
     * <li>Ensures healing does not exceed the maximum health.</li>
     * </ul>
     */
    @Test
    void testHeal() {
        // Reduce health, then heal
        healthComponent.takeDamage(5);
        assertEquals(5, healthComponent.getCurrentHealth());

        // Heal partially and verify
        healthComponent.heal(3);
        assertEquals(8, healthComponent.getCurrentHealth());

        // Attempt to heal beyond maximum health and verify capping at max
        healthComponent.heal(10);
        assertEquals(10, healthComponent.getCurrentHealth()); // Should not exceed maxHealth
    }

    /**
     * Tests the {@link HealthComponent#setCurrentHealth(int)} method.
     * <ul>
     * <li>Verifies that health is set to the specified value within valid bounds.</li>
     * <li>Ensures health does not exceed maximum health or drop below zero.</li>
     * <li>Checks actor destruction when health is set to zero or below.</li>
     * </ul>
     */
    @Test
    void testSetHealth() {
        // Set health within valid range
        healthComponent.setCurrentHealth(4);
        assertEquals(4, healthComponent.getCurrentHealth());

        // Set health beyond maximum and verify capping at max
        healthComponent.setCurrentHealth(15);
        assertEquals(10, healthComponent.getCurrentHealth()); // Should not exceed maxHealth

        // Set health below zero and verify actor destruction
        healthComponent.setCurrentHealth(-5);
        assertEquals(0, healthComponent.getCurrentHealth());
        assertTrue(mockActor.isDestroyed());
    }

    /**
     * Tests the {@link HealthComponent#reset()} method.
     * <ul>
     * <li>Verifies that health is restored to maximum health.</li>
     * <li>Ensures the actor is no longer destroyed after a reset.</li>
     * </ul>
     */
    @Test
    void testReset() {
        // Apply damage, then reset
        healthComponent.takeDamage(5);
        assertEquals(5, healthComponent.getCurrentHealth());

        // Reset health and verify restoration
        healthComponent.reset();
        assertEquals(10, healthComponent.getCurrentHealth());
        assertFalse(mockActor.isDestroyed());
    }
}
