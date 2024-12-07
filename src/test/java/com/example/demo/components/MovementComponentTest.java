package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovementComponentTest {

    private MovementComponent movementComponent;
    private Actor mockActor;

    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // 初始化JavaFX环境
    }

    @BeforeEach
    void setUp() {
        movementComponent = new MovementComponent(5.0, 3.0);
        mockActor = new Actor("userplane.png", 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
                // 不需要实现
            }
        };
    }

    @Test
    void testSetVelocity() {
        movementComponent.setVelocity(10.0, -5.0);
        assertEquals(10.0, movementComponent.getVelocityX());
        assertEquals(-5.0, movementComponent.getVelocityY());
    }

    @Test
    void testUpdate() {
        mockActor.setTranslateX(0);
        mockActor.setTranslateY(0);

        movementComponent.update(mockActor);

        assertEquals(5.0, mockActor.getTranslateX());
        assertEquals(3.0, mockActor.getTranslateY());
    }
}
