package com.example.demo.view;

import javafx.scene.Group;

public class LevelBossView extends LevelView {
	private static final double HEALTH_BAR_X = 550; // Adjust position as needed
	private static final double HEALTH_BAR_Y = 50;  // Adjust position as needed
	private static final int BOSS_MAX_HEALTH = 100; // Set this to match your boss's max health

	private final Group root;
	private final BossHealthBar bossHealthBar;

	public LevelBossView(Group root, int heartsToDisplay, int maxKills) {
		super(root, heartsToDisplay, maxKills);
		this.root = root;
		this.bossHealthBar = new BossHealthBar(HEALTH_BAR_X, HEALTH_BAR_Y, BOSS_MAX_HEALTH);
		addBossHealthBar();
	}

	private void addBossHealthBar() {
		root.getChildren().add(bossHealthBar);
	}

	@Override
	public void showKillCountDisplay() {
		// Empty override to prevent kill count from being displayed
	}

	@Override
	public void updateKillCount(int kills) {
		// Empty override to prevent kill count updates
	}
}