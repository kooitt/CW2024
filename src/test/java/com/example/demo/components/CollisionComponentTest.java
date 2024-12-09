package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionComponentTest {

    private CollisionComponent component1;
    private CollisionComponent component2;

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        Actor mockActor1 = createMockActor("bossplane.png");
        Actor mockActor2 = createMockActor("userplane.png");

        component1 = new CollisionComponent(mockActor1, 10, 20, 0, 0);
        component2 = new CollisionComponent(mockActor2, 15, 25, 5, 5);
    }

    private Actor createMockActor(String image) {
        return new Actor(image, 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
            }
        };
    }

    @Test
    public void constructor_initializesCorrectly() {
        assertEquals(10, component1.getHitboxWidth());
        assertEquals(20, component1.getHitboxHeight());
        assertEquals(0, component1.offsetX);
        assertEquals(0, component1.offsetY);
        assertTrue(component1.collisionEnabled);
    }

    @Test
    public void setHitboxSize_updatesSizeAndResetsOffsets() {
        component1.setHitboxSize(15, 25);
        assertEquals(15, component1.getHitboxWidth());
        assertEquals(25, component1.getHitboxHeight());
        assertEquals(0, component1.offsetX);
        assertEquals(0, component1.offsetY);
    }

    @Test
    public void checkCollision_noOverlap_returnsFalse() {
        component2.offsetX = 30;
        assertFalse(component1.checkCollision(component2));

        component2.offsetX = 0;
        component2.offsetY = 30;
        assertFalse(component1.checkCollision(component2));
    }

    @Test
    public void checkCollision_overlap_returnsTrue() {
        component2.offsetX = 5;
        component2.offsetY = 5;
        assertTrue(component1.checkCollision(component2));
    }

    @Test
    public void checkCollision_edgeTouch_returnsFalse() {
        component2.offsetX = 10;
        component2.offsetY = 0;
        assertFalse(component1.checkCollision(component2));

        component2.offsetX = 0;
        component2.offsetY = 20;
        assertFalse(component1.checkCollision(component2));
    }

    @Test
    public void checkCollision_beneficialItems_alwaysCollides() {
        Actor beneficialActor = createMockActor("ActorLevelUp.png");
        CollisionComponent beneficialComponent = new CollisionComponent(beneficialActor, 10, 20, 0, 0);
        assertTrue(component1.checkCollision(beneficialComponent));
    }

    @Test
    public void checkCollision_disabledCollision_returnsFalse() {
        component1.SetActorCollisionEnable(false);
        assertFalse(component1.checkCollision(component2));

        component2.SetActorCollisionEnable(false);
        component1.SetActorCollisionEnable(true);
        assertFalse(component1.checkCollision(component2));
    }
}