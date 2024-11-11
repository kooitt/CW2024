package com.example.demo;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class KillCountDisplay {

    private static final int LABEL_FONT_SIZE = 24;
    private static final String LABEL_FONT_FAMILY = "Arcade";
    private static final String LABEL_TEXT_COLOR = "-fx-text-fill: white;";
    private HBox container;
    private double containerXPosition;
    private double containerYPosition;
    private int currentKills;
    private final int maxKills;
    private Label killCountLabel;

    public KillCountDisplay(double xPosition, double yPosition, int maxKills) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.maxKills = maxKills;
        this.currentKills = 0;
        initializeContainer();
        initializeKillCountLabel();
    }

    private void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

    private void initializeKillCountLabel() {
        killCountLabel = new Label("Kills: " + currentKills + "/" + maxKills);
        killCountLabel.setFont(Font.font(LABEL_FONT_FAMILY, FontWeight.BOLD, LABEL_FONT_SIZE));
        killCountLabel.setStyle(LABEL_TEXT_COLOR);
        container.getChildren().add(killCountLabel);
    }

    public void updateKillCount(int kills) {
        this.currentKills = kills;
        killCountLabel.setText("Kills: " + currentKills + "/" + maxKills);
    }

    public HBox getContainer() {
        return container;
    }
}