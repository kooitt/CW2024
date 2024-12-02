package com.example.demo.levels;

import com.example.demo.ui.GameOverImage;
import com.example.demo.ui.HeartDisplay;
import com.example.demo.ui.WinImage;

import javafx.scene.Group;
import javafx.scene.text.Text;

/**
 * LevelView manages the visual elements of a game level,
 * including the heart display, score display, win screen, and game over screen.
 */
public class LevelView {

    // Constants for positioning UI elements
    private static final double HEART_DISPLAY_X_POSITION = 5;
    private static final double HEART_DISPLAY_Y_POSITION = 25;
    private static final int WIN_IMAGE_X_POSITION = 355;
    private static final int WIN_IMAGE_Y_POSITION = 175;
    private static final int LOSS_SCREEN_X_POSITION = -160;
    private static final int LOSS_SCREEN_Y_POSISITION = -375;

    // Root group for adding and managing UI elements
    private final Group root;

    // UI components for level-specific visuals
    private final WinImage winImage;           // Image shown on level win
    private final GameOverImage gameOverImage; // Image shown on game over
    private final HeartDisplay heartDisplay;   // Heart display for player health
    private Text scoreDisplay;                 // UI element for score display

    /**
     * Constructor for LevelView.
     *
     * @param root            The root group for the level.
     * @param heartsToDisplay The initial number of hearts to display for player health.
     */
    public LevelView(Group root, int heartsToDisplay) {
        this.root = root;
        
        // Initialize UI elements with specific positions
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
        this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
        this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
        
        // Initialize and style the score display
        this.scoreDisplay = new Text(10, 50, "Score: 0");
        scoreDisplay.setStyle("-fx-font-size: 20px; -fx-fill: white;"); // Set font size and color
        
        // Add the score display to the root group
        root.getChildren().add(scoreDisplay);
    }

    /**
     * Displays the heart container (health display) on the screen.
     */
    public void showHeartDisplay() {
        root.getChildren().add(heartDisplay.getContainer()); // Add heart display to root
    }

    /**
     * Displays the win image on the screen.
     */
    public void showWinImage() {
        root.getChildren().add(winImage); // Add win image to root
        winImage.showWinImage();          // Trigger win image animation or effects
    }

    /**
     * Displays the game over image on the screen.
     */
    public void showGameOverImage() {
        root.getChildren().add(gameOverImage); // Add game over image to root
    }

    /**
     * Updates the heart display to reflect the remaining hearts.
     *
     * @param heartsRemaining The number of hearts remaining for the player.
     */
    public void removeHearts(int heartsRemaining) {
        int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size(); // Get current heart count
        for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
            heartDisplay.removeHeart(); // Remove hearts one by one until the count matches
        }
    }

    /**
     * Updates the score display to reflect the current score.
     *
     * @param score The player's current score.
     */
    public void updateScore(int score) {
        scoreDisplay.setText("Score: " + score); // Update the score display text
    }
}
