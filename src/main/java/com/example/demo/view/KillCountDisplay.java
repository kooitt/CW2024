package com.example.demo.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Represents a display for the kill count in the game.
 */
public class KillCountDisplay {

    private static final int LABEL_FONT_SIZE = 24;
    private static final String LABEL_FONT_FAMILY = "Arcade";
    private static final String LABEL_TEXT_COLOR = "-fx-text-fill: black;";
    private HBox container;
    private final double containerXPosition;
    private final double containerYPosition;
    private int currentKills;
    private final int maxKills;
    private Label killCountLabel;

    /**
     * Constructs a KillCountDisplay with the specified position and maximum kills.
     *
     * @param xPosition the x-coordinate position of the kill count display.
     * @param yPosition the y-coordinate position of the kill count display.
     * @param maxKills the maximum number of kills to display.
     */
    public KillCountDisplay(double xPosition, double yPosition, int maxKills) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.maxKills = maxKills;
        this.currentKills = 0;
        initializeContainer();
        initializeKillCountLabel();
    }

    /**
     * Initializes the container for the kill count display.
     */
    private void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

    /**
     * Initializes the kill count label.
     */
    private void initializeKillCountLabel() {
        killCountLabel = new Label("Kills: " + currentKills + "/" + maxKills);
        killCountLabel.setFont(Font.font(LABEL_FONT_FAMILY, FontWeight.BOLD, LABEL_FONT_SIZE));
        killCountLabel.setStyle(LABEL_TEXT_COLOR);
        container.getChildren().add(killCountLabel);
    }

    /**
     * Updates the kill count display with the specified number of kills.
     *
     * @param kills the current number of kills.
     */
    public void updateKillCount(int kills) {
        this.currentKills = kills;
        killCountLabel.setText("Kills: " + currentKills + "/" + maxKills);
    }

    /**
     * Returns the container for the kill count display.
     *
     * @return the container for the kill count display.
     */
    public HBox getContainer() {
        return container;
    }
}