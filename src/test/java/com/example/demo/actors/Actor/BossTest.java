package com.example.demo.actors.Actor; // 请根据实际项目包路径调整

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

public class BossTest {

    private Boss boss;
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
        boss = new Boss(mockRoot, mockLevel);
    }

    @Test
    void testInitialHealth() {
        // Boss初始最大血量应为800（根据Boss类中定义）
        assertEquals(800, boss.getMaxHealth());
        assertEquals(800, boss.getCurrentHealth());
    }

    @Test
    void testTakeDamage() {
        // 造成100点伤害后，检查血量是否正确减少
        boss.takeDamage(100);
        assertEquals(700, boss.getCurrentHealth());

        // 继续造成700点伤害后，Boss应该死亡
        boss.takeDamage(700);
        assertEquals(0, boss.getCurrentHealth());
        assertTrue(boss.isDestroyed());
    }

    @Test
    void testDestroy() {
        // 未受伤时调用destroy()
        boss.destroy();
        assertTrue(boss.isDestroyed(), "Boss should be destroyed after destroy() call.");
    }

    @Test
    void testHealthBarUpdate() {
        ProgressBar healthBar = (ProgressBar) boss.getChildren().stream().filter(node -> node instanceof ProgressBar).findFirst().orElse(null);

        assertNotNull(healthBar, "HealthBar should be present in Boss children.");
        assertEquals(1.0, healthBar.getProgress(), 0.0001, "Initial healthbar progress should be full (1.0).");

        boss.takeDamage(400);
        Platform.runLater(() -> {
            double expectedProgress = 400.0 / 800.0;
            assertEquals(expectedProgress, healthBar.getProgress(), 0.0001,
                    "HealthBar progress should reflect current health proportion.");
        });
    }

}
