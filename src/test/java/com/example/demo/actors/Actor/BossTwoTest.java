package com.example.demo.actors.Actor; // 请根据实际项目包路径调整

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

public class BossTwoTest {

    private BossTwo bossTwo;
    private LevelParent mockLevel;
    private Group mockRoot;

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
        Platform.setImplicitExit(false);
    }

    @BeforeEach
    void setUp() {
        mockRoot = new Group();
        mockLevel = Mockito.mock(LevelParent.class);
        bossTwo = new BossTwo(mockRoot, mockLevel);
    }

    @Test
    void testInitialHealth() {
        assertEquals(500, bossTwo.getMaxHealth());
        assertEquals(500, bossTwo.getCurrentHealth());
        assertFalse(bossTwo.isDestroyed(), "BossTwo should not be destroyed initially.");
    }

    @Test
    void testTakeDamage() {
        bossTwo.takeDamage(100);
        assertEquals(400, bossTwo.getCurrentHealth());

        bossTwo.takeDamage(400);
        assertEquals(0, bossTwo.getCurrentHealth());
        assertTrue(bossTwo.isDestroyed(), "BossTwo should be destroyed after depleting health.");
    }

    @Test
    void testDestroy() {
        bossTwo.destroy();
        assertTrue(bossTwo.isDestroyed(), "BossTwo should be destroyed after destroy() call.");
    }

    @Test
    void testHealthBarUpdate() {
        ProgressBar healthBar = (ProgressBar) bossTwo.getChildren().stream().filter(node -> node instanceof ProgressBar).findFirst().orElse(null);

        assertNotNull(healthBar, "HealthBar should be present in BossTwo children.");
        assertEquals(1.0, healthBar.getProgress(), 0.0001, "Initial healthbar progress should be full (1.0).");

        bossTwo.takeDamage(250);
        Platform.runLater(() -> {
            double expectedProgress = 250.0 / 500.0;
            assertEquals(expectedProgress, healthBar.getProgress(), 0.0001,
                    "HealthBar progress should reflect current health proportion.");
        });
    }
}
