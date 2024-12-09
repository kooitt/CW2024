package com.example.demo.actors.Projectile;

import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Projectile} class and its behavior.
 */
public class ProjectileTest {

    /**
     * A test subclass of {@link Projectile} for testing purposes.
     */
    private static class TestProjectile extends Projectile {
        /**
         * Constructs a TestProjectile with specified parameters.
         *
         * @param imageName    the name of the image for the projectile
         * @param imageHeight  the height of the projectile image
         * @param initialXPos  the initial X position of the projectile
         * @param initialYPos  the initial Y position of the projectile
         */
        public TestProjectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
            super(imageName, imageHeight, initialXPos, initialYPos);
        }

        @Override
        public void updateActor(double deltaTime, LevelParent level) {
            super.updateActor(deltaTime, level);
        }
    }

    private TestProjectile projectile; // An instance of TestProjectile for testing.
    private LevelParent mockLevel;    // A mocked instance of LevelParent.

    /**
     * Initializes the JavaFX runtime environment before all tests.
     */
    @BeforeAll
    static void initJfx() {
        new JFXPanel(); // Ensures JavaFX components can be initialized in tests.
        Platform.setImplicitExit(false); // Prevents application exit during tests.
    }

    /**
     * Sets up the test environment before each test.
     * Initializes a mocked LevelParent and a TestProjectile instance.
     */
    @BeforeEach
    void setUp() {
        mockLevel = Mockito.mock(LevelParent.class);
        projectile = new TestProjectile("userfire.png", 25, 100, 150);
    }

    /**
     * Verifies the initial state of a {@link Projectile} after construction.
     */
    @Test
    void testConstructor() {
        assertEquals(100, projectile.getLayoutX(), "Initial X position should be set correctly.");
        assertEquals(150, projectile.getLayoutY(), "Initial Y position should be set correctly.");
        assertEquals(0, projectile.getTranslateX(), "Initial translateX should be 0.");
        assertEquals(0, projectile.getTranslateY(), "Initial translateY should be 0.");
        assertFalse(projectile.isDestroyed(), "Projectile should not be destroyed upon initialization.");
        assertTrue(projectile.isVisible(), "Projectile should be visible upon initialization.");

        CollisionComponent collision = projectile.getCollisionComponent();
        assertNotNull(collision, "CollisionComponent should be initialized.");
        assertEquals(projectile.getImageView().getFitWidth(), collision.getHitboxWidth(), "Collision hitbox width should match ImageView width.");
        assertEquals(projectile.getImageView().getFitHeight(), collision.getHitboxHeight(), "Collision hitbox height should match ImageView height.");
    }

    /**
     * Tests the {@link Projectile#resetPosition(double, double)} method
     * to ensure it correctly updates the position and state of the projectile.
     */
    @Test
    void testResetPosition() {
        projectile.resetPosition(200, 250);
        assertEquals(200, projectile.getLayoutX(), "X position should be updated after resetPosition.");
        assertEquals(250, projectile.getLayoutY(), "Y position should be updated after resetPosition.");
        assertEquals(0, projectile.getTranslateX(), "translateX should be reset to 0.");
        assertEquals(0, projectile.getTranslateY(), "translateY should be reset to 0.");
        assertFalse(projectile.isDestroyed(), "Projectile should not be destroyed after resetPosition.");
        assertTrue(projectile.isVisible(), "Projectile should be visible after resetPosition.");
    }

    /**
     * Tests the {@link Projectile#reset()} method to ensure it resets
     * the visibility and destruction state of the projectile.
     */
    @Test
    void testReset() {
        projectile.reset();
        assertFalse(projectile.isVisible(), "Projectile should be invisible after reset.");
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityX(), "VelocityX should be 0 after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityY(), "VelocityY should be 0 after reset.");
    }

    /**
     * Tests the {@link Projectile#destroy()} method to ensure it updates
     * the destruction state and visibility of the projectile.
     */
    @Test
    void testDestroy() {
        projectile.destroy();
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after destroy() call.");
        assertFalse(projectile.isVisible(), "Projectile should be invisible after destroy() call.");
    }

    /**
     * Tests the {@link Projectile#takeDamage(int)} method to ensure it behaves
     * as expected by destroying the projectile upon taking damage.
     */
    @Test
    void testTakeDamage() {
        projectile.takeDamage(1);
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after takeDamage.");
        assertFalse(projectile.isVisible(), "Projectile should be invisible after takeDamage.");
    }

    /**
     * Tests the {@link Projectile#updateActor(double, LevelParent)} method
     * to ensure it maintains position when velocity is zero.
     */
    @Test
    void testUpdateActorWithoutMovement() {
        projectile.getMovementComponent().setVelocity(0.0, 0.0);
        double initialX = projectile.getLayoutX();
        double initialY = projectile.getLayoutY();

        projectile.updateActor(0.016, mockLevel);

        assertEquals(initialX, projectile.getLayoutX(), 0.0001, "X position should remain unchanged.");
        assertEquals(initialY, projectile.getLayoutY(), 0.0001, "Y position should remain unchanged.");

        CollisionComponent collision = projectile.getCollisionComponent();
        assertEquals(projectile.getLayoutX() + collision.offsetX,
                collision.getHitboxX(),
                0.0001,
                "CollisionComponent X position should remain unchanged.");
        assertEquals(projectile.getLayoutY() + collision.offsetY,
                collision.getHitboxY(),
                0.0001,
                "CollisionComponent Y position should remain unchanged.");
    }

    /**
     * Tests the behavior of {@link Projectile#resetPosition(double, double)} after calling {@link Projectile#destroy()}.
     */
    @Test
    void testVisibilityOnResetPosition() {
        projectile.destroy();
        assertFalse(projectile.isVisible(), "Projectile should be invisible after destroy.");
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after destroy.");

        projectile.resetPosition(300, 350);
        assertTrue(projectile.isVisible(), "Projectile should be visible after resetPosition.");
        assertFalse(projectile.isDestroyed(), "Projectile should not be destroyed after resetPosition.");
    }

    /**
     * Tests the combined behavior of {@link Projectile#reset()} and {@link Projectile#destroy()}.
     */
    @Test
    void testResetAfterDestroy() {
        projectile.destroy();
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after destroy.");
        assertFalse(projectile.isVisible(), "Projectile should be invisible after destroy.");

        projectile.reset();
        assertFalse(projectile.isVisible(), "Projectile should remain invisible after reset.");
        assertTrue(projectile.isDestroyed(), "Projectile should remain destroyed after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityX(), "VelocityX should be 0 after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityY(), "VelocityY should be 0 after reset.");
    }
}
