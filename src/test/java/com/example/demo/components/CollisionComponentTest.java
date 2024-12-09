package com.example.demo.components;

import com.example.demo.actors.Actor.Actor;
import com.example.demo.levels.LevelParent;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CollisionComponent} class.
 * This class verifies the correctness of the CollisionComponent implementation,
 * ensuring proper behavior of hitbox sizing, collision checking, and other functionalities.
 */
public class CollisionComponentTest {

    private CollisionComponent component1; // A CollisionComponent instance for testing.
    private CollisionComponent component2; // Another CollisionComponent instance for testing.

    /**
     * Initializes the JavaFX toolkit. Required to run JavaFX-dependent tests.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    /**
     * Sets up test objects before each test method.
     * Creates mock {@link Actor} instances and associates them with CollisionComponents.
     */
    @BeforeEach
    void setUp() {
        Actor mockActor1 = createMockActor("bossplane.png");
        Actor mockActor2 = createMockActor("userplane.png");

        component1 = new CollisionComponent(mockActor1, 10, 20, 0, 0);
        component2 = new CollisionComponent(mockActor2, 15, 25, 5, 5);
    }

    /**
     * Creates a mock {@link Actor} instance with the specified image.
     *
     * @param image the image file name to associate with the Actor.
     * @return a mock {@link Actor} instance.
     */
    private Actor createMockActor(String image) {
        return new Actor(image, 50, 0, 0, 10) {
            @Override
            public void updateActor(double deltaTime, LevelParent level) {
                // Mock method does nothing for simplicity.
            }
        };
    }

    /**
     * Tests whether the constructor initializes the {@link CollisionComponent} correctly.
     * Verifies hitbox dimensions, offsets, and collision enable state.
     */
    @Test
    public void constructor_initializesCorrectly() {
        assertEquals(10, component1.getHitboxWidth(), "Hitbox width should be initialized correctly.");
        assertEquals(20, component1.getHitboxHeight(), "Hitbox height should be initialized correctly.");
        assertEquals(0, component1.offsetX, "Offset X should be initialized to 0.");
        assertEquals(0, component1.offsetY, "Offset Y should be initialized to 0.");
        assertTrue(component1.collisionEnabled, "Collision should be enabled by default.");
    }

    /**
     * Tests the {@link CollisionComponent#setHitboxSize} method.
     * Verifies that it updates hitbox dimensions and resets offsets to zero.
     */
    @Test
    public void setHitboxSize_updatesSizeAndResetsOffsets() {
        component1.setHitboxSize(15, 25);
        assertEquals(15, component1.getHitboxWidth(), "Hitbox width should be updated correctly.");
        assertEquals(25, component1.getHitboxHeight(), "Hitbox height should be updated correctly.");
        assertEquals(0, component1.offsetX, "Offset X should reset to 0.");
        assertEquals(0, component1.offsetY, "Offset Y should reset to 0.");
    }

    /**
     * Tests collision detection between non-overlapping components.
     * Verifies that {@link CollisionComponent#checkCollision} returns false in such cases.
     */
    @Test
    public void checkCollision_noOverlap_returnsFalse() {
        component2.offsetX = 30; // Move component2 far enough to prevent overlap.
        assertFalse(component1.checkCollision(component2), "No collision should occur when components do not overlap.");

        component2.offsetX = 0;
        component2.offsetY = 30; // Move component2 vertically to avoid overlap.
        assertFalse(component1.checkCollision(component2), "No collision should occur when components do not overlap vertically.");
    }

    /**
     * Tests collision detection between overlapping components.
     * Verifies that {@link CollisionComponent#checkCollision} returns true in such cases.
     */
    @Test
    public void checkCollision_overlap_returnsTrue() {
        component2.offsetX = 5; // Set overlapping offsets.
        component2.offsetY = 5;
        assertTrue(component1.checkCollision(component2), "Collision should occur when components overlap.");
    }

    /**
     * Tests collision detection when components only touch edges.
     * Verifies that {@link CollisionComponent#checkCollision} returns false in such cases.
     */
    @Test
    public void checkCollision_edgeTouch_returnsFalse() {
        component2.offsetX = 10; // Adjust offsets to make components touch edges.
        component2.offsetY = 0;
        assertFalse(component1.checkCollision(component2), "Collision should not occur when components only touch edges.");

        component2.offsetX = 0;
        component2.offsetY = 20;
        assertFalse(component1.checkCollision(component2), "Collision should not occur when components only touch edges vertically.");
    }

    /**
     * Tests collision detection for beneficial items.
     * Verifies that such items always collide with other components.
     */
    @Test
    public void checkCollision_beneficialItems_alwaysCollides() {
        Actor beneficialActor = createMockActor("ActorLevelUp.png"); // Create a mock beneficial item.
        CollisionComponent beneficialComponent = new CollisionComponent(beneficialActor, 10, 20, 0, 0);
        assertTrue(component1.checkCollision(beneficialComponent), "Beneficial items should always collide.");
    }

    /**
     * Tests collision detection when collision is disabled.
     * Verifies that {@link CollisionComponent#checkCollision} returns false in such cases.
     */
    @Test
    public void checkCollision_disabledCollision_returnsFalse() {
        component1.SetActorCollisionEnable(false); // Disable collision for component1.
        assertFalse(component1.checkCollision(component2), "Collision should not occur when collision is disabled.");

        component2.SetActorCollisionEnable(false); // Disable collision for component2 as well.
        component1.SetActorCollisionEnable(true); // Enable collision for component1.
        assertFalse(component1.checkCollision(component2), "Collision should not occur when both components have collision disabled.");
    }
}
