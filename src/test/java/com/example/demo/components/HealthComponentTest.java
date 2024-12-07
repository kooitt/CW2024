package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthComponentTest {

    private Actor mockActor;
    private HealthComponent healthComponent;
    private LevelParent mockLevel;

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        mockLevel = mock(LevelParent.class);

        mockActor = new Actor("heart.png", 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
                // 不需要实现
            }
        };
        healthComponent = new HealthComponent(mockActor, 10);
    }

    @Test
    void testTakeDamage() {
        healthComponent.takeDamage(3);
        assertEquals(7, healthComponent.getCurrentHealth());

        healthComponent.takeDamage(10);
        assertEquals(0, healthComponent.getCurrentHealth());
        assertTrue(mockActor.isDestroyed());
    }

    @Test
    void testHeal() {
        healthComponent.takeDamage(5);
        assertEquals(5, healthComponent.getCurrentHealth());

        healthComponent.heal(3);
        assertEquals(8, healthComponent.getCurrentHealth());

        healthComponent.heal(10);
        assertEquals(10, healthComponent.getCurrentHealth()); // 不超过maxHealth
    }

    @Test
    void testSetHealth() {
        healthComponent.setCurrentHealth(4);
        assertEquals(4, healthComponent.getCurrentHealth());

        healthComponent.setCurrentHealth(15);
        assertEquals(10, healthComponent.getCurrentHealth()); // 不超过maxHealth

        healthComponent.setCurrentHealth(-5);
        assertEquals(0, healthComponent.getCurrentHealth());
        assertTrue(mockActor.isDestroyed());
    }

    @Test
    void testReset() {
        healthComponent.takeDamage(5);
        assertEquals(5, healthComponent.getCurrentHealth());

        healthComponent.reset();
        assertEquals(10, healthComponent.getCurrentHealth());
        assertFalse(mockActor.isDestroyed());
    }
}
