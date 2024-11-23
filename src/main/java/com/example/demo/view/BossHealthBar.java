package com.example.demo.view;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BossHealthBar extends StackPane {
    private final ProgressBar healthBar;
    private final Text healthText;
    private final int maxHealth;

    public BossHealthBar(double x, double y, int maxHealth) {
        this.maxHealth = maxHealth;

        // Create the health bar
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(300);
        healthBar.setStyle("-fx-accent: red;");

        // Create the text that shows the actual health numbers
        healthText = new Text();
        healthText.setFill(Color.WHITE);
        updateHealth(maxHealth);

        // Position the health bar
        setTranslateX(x);
        setTranslateY(y);

        // Add both the bar and text to this container
        getChildren().addAll(healthBar, healthText);
    }

    public void updateHealth(int currentHealth) {
        double healthPercentage = (double) currentHealth / maxHealth;
        healthBar.setProgress(healthPercentage);
    }
}