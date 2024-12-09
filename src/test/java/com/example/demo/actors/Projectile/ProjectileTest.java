package com.example.demo.actors.Projectile; // 请根据实际项目包路径调整

import com.example.demo.components.CollisionComponent;
import com.example.demo.levels.LevelParent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {

    private static class TestProjectile extends Projectile {
        public TestProjectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
            super(imageName, imageHeight, initialXPos, initialYPos);
        }

        @Override
        public void updateActor(double deltaTime, LevelParent level) {
            super.updateActor(deltaTime, level);
        }
    }

    private TestProjectile projectile;
    private LevelParent mockLevel;

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
        Platform.setImplicitExit(false);
    }

    @BeforeEach
    void setUp() {
        mockLevel = Mockito.mock(LevelParent.class);
        projectile = new TestProjectile("userfire.png", 25, 100, 150);
    }

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

    @Test
    void testReset() {
        projectile.reset();
        assertFalse(projectile.isVisible(), "Projectile should be invisible after reset.");
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityX(), "VelocityX should be 0 after reset.");
        assertEquals(0, projectile.getMovementComponent().getVelocityY(), "VelocityY should be 0 after reset.");
    }

    @Test
    void testDestroy() {
        projectile.destroy();
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after destroy() call.");
        assertFalse(projectile.isVisible(), "Projectile should be invisible after destroy() call.");
    }

    @Test
    void testTakeDamage() {
        // 由于takeDamage直接调用destroy(), 测试isDestroyed和isVisible状态
        projectile.takeDamage(1);
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after takeDamage.");
        assertFalse(projectile.isVisible(), "Projectile should be invisible after takeDamage.");
    }

    @Test
    void testUpdateActorWithoutMovement() {
        // 设置移动速度为0
        projectile.getMovementComponent().setVelocity(0.0, 0.0);
        double initialX = projectile.getLayoutX();
        double initialY = projectile.getLayoutY();

        projectile.updateActor(0.016, mockLevel); // 假设deltaTime为16ms

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

    @Test
    void testVisibilityOnResetPosition() {
        projectile.destroy();
        assertFalse(projectile.isVisible(), "Projectile should be invisible after destroy.");
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after destroy.");

        projectile.resetPosition(300, 350);
        assertTrue(projectile.isVisible(), "Projectile should be visible after resetPosition.");
        assertFalse(projectile.isDestroyed(), "Projectile should not be destroyed after resetPosition.");
    }

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
