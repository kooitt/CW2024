package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the {@link MovementComponent} class.
 *
 * This class contains unit tests for verifying the functionality of the
 * MovementComponent, which is responsible for managing the movement of an {@link Actor}
 * by updating its position based on its velocity.
 */
class MovementComponentTest {

    /** The MovementComponent instance being tested. */
    private MovementComponent movementComponent;

    /** A mock {@link Actor} used for testing. */
    private Actor mockActor;

    /**
     * Initializes the JavaFX environment before all tests.
     *
     * This ensures that JavaFX components, such as the {@link JFXPanel}, can be used
     * during the testing process without errors.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // Initialize JavaFX environment
    }

    /**
     * Sets up the testing environment before each test.
     *
     * This includes initializing a new {@link MovementComponent} with predefined
     * velocity values and a mock {@link Actor}.
     */
    @BeforeEach
    void setUp() {
        movementComponent = new MovementComponent(5.0, 3.0);
        mockActor = new Actor("userplane.png", 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
                // No-op for test purposes
            }
        };
    }

    /**
     * Tests the {@link MovementComponent#setVelocity(double, double)} method.
     *
     * Verifies that the velocity values are correctly set and retrievable using the
     * {@link MovementComponent#getVelocityX()} and {@link MovementComponent#getVelocityY()} methods.
     */
    @Test
    void testSetVelocity() {
        movementComponent.setVelocity(10.0, -5.0);
        assertEquals(10.0, movementComponent.getVelocityX(), "Velocity X should be 10.0 after setting.");
        assertEquals(-5.0, movementComponent.getVelocityY(), "Velocity Y should be -5.0 after setting.");
    }

    /**
     * Tests the {@link MovementComponent#update(Actor)} method.
     *
     * Verifies that the {@link Actor}'s position is correctly updated based on the
     * velocity values set in the {@link MovementComponent}.
     */
    @Test
    void testUpdate() {
        mockActor.setTranslateX(0);
        mockActor.setTranslateY(0);

        movementComponent.update(mockActor);

        assertEquals(5.0, mockActor.getTranslateX(), "Actor's X position should be updated by velocity X.");
        assertEquals(3.0, mockActor.getTranslateY(), "Actor's Y position should be updated by velocity Y.");
    }
}
