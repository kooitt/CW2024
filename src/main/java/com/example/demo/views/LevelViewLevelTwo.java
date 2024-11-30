package com.example.demo.views;

import javafx.scene.Group;

/**
 * Specialized LevelView for Level Two.
 */
public class LevelViewLevelTwo extends LevelView {

	/**
	 * Constructs a LevelViewLevelTwo with specified root and hearts.
	 *
	 * @param root          the root group.
	 * @param heartsToDisplay number of hearts to display.
	 */
	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
	}
}
