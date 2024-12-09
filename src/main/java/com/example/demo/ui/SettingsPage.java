package com.example.demo.ui;

import com.example.demo.controller.Controller;
import com.example.demo.components.SoundComponent;
import com.example.demo.utils.KeyBindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The SettingsPage class represents a user interface for managing game settings such as key bindings
 * and audio volume levels for background music (BGM) and sound effects (SFX).
 */
public class SettingsPage {

    /**
     * Root container for the settings page.
     */
    private StackPane root;

    /**
     * Controller to manage interactions with the main application.
     */
    private final Controller controller;

    /**
     * Key bindings for the game, allowing customization of input keys.
     */
    private final KeyBindings keyBindings;

    /**
     * Runnable action to execute when the back button is clicked.
     */
    private Runnable backAction;

    /**
     * Buttons for configuring key bindings for up, down, left, and right controls.
     */
    private Button upKeyBtn, downKeyBtn, leftKeyBtn, rightKeyBtn;

    /**
     * Sliders for adjusting the volume of background music (BGM) and sound effects (SFX).
     */
    private Slider bgmSlider, sfxSlider;

    /**
     * Labels to display the current volume percentage for BGM and SFX.
     */
    private Label bgmVolumeLabel, sfxVolumeLabel;

    /**
     * Constructs a new SettingsPage with the specified controller.
     *
     * @param controller The controller managing the application.
     */
    public SettingsPage(Controller controller) {
        this.controller = controller;
        this.keyBindings = KeyBindings.getInstance();
        initialize();
    }

    /**
     * Initializes the settings page layout and UI components.
     */
    private void initialize() {
        root = new StackPane();
        setSemiTransparentBackground();

        VBox contentBox = new VBox(30); // Vertical layout with spacing of 30
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setMaxWidth(600);
        contentBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20;");

        Label title = new Label("Settings");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", 36));
        title.setEffect(new DropShadow(5, Color.BLACK));

        VBox volumeBox = new VBox(30);
        volumeBox.setAlignment(Pos.CENTER);

        bgmSlider = new Slider(0, 1, SoundComponent.getBgmVolume());
        sfxSlider = new Slider(0, 1, SoundComponent.getSfxVolume());

        HBox bgmBox = createVolumeControl("BGM Volume:", bgmSlider, bgmVolumeLabel = new Label(String.format("%.0f%%", SoundComponent.getBgmVolume() * 100)));
        bgmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundComponent.setBgmVolume(newVal.doubleValue());
            bgmVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        HBox sfxBox = createVolumeControl("SFX Volume:", sfxSlider, sfxVolumeLabel = new Label(String.format("%.0f%%", SoundComponent.getSfxVolume() * 100)));
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundComponent.setSfxVolume(newVal.doubleValue());
            sfxVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        volumeBox.getChildren().addAll(bgmBox, sfxBox);

        VBox keyBox = new VBox(20);
        keyBox.setAlignment(Pos.CENTER);

        keyBox.getChildren().addAll(
                createKeySetting("Up Key:", keyBindings.getUpKey(), keyBindings::setUpKey, btn -> upKeyBtn = btn),
                createKeySetting("Down Key:", keyBindings.getDownKey(), keyBindings::setDownKey, btn -> downKeyBtn = btn),
                createKeySetting("Left Key:", keyBindings.getLeftKey(), keyBindings::setLeftKey, btn -> leftKeyBtn = btn),
                createKeySetting("Right Key:", keyBindings.getRightKey(), keyBindings::setRightKey, btn -> rightKeyBtn = btn)
        );

        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);

        Button backBtn = createStyledButton("Back", () -> {
            if (backAction != null) backAction.run();
        });

        Button returnToMainMenuBtn = createStyledButton("Return to Main Menu", controller::returnToMainMenu);

        buttonsBox.getChildren().addAll(backBtn, returnToMainMenuBtn);

        contentBox.getChildren().addAll(title, volumeBox, keyBox, buttonsBox);
        root.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);
        root.setPrefSize(controller.getStage().getWidth(), controller.getStage().getHeight());
    }

    /**
     * Sets a semi-transparent black background for the root container.
     */
    private void setSemiTransparentBackground() {
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.rgb(0, 0, 0, 0.8),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));
    }

    /**
     * Creates a volume control UI element with a slider and a label.
     *
     * @param labelText   The label text for the control.
     * @param slider      The slider to adjust the volume.
     * @param volumeLabel The label to display the current volume percentage.
     * @return An HBox containing the volume control elements.
     */
    private HBox createVolumeControl(String labelText, Slider slider, Label volumeLabel) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 18));

        slider.setPrefWidth(200);
        slider.setStyle("-fx-control-inner-background: #ffffff;");

        volumeLabel.setTextFill(Color.WHITE);
        volumeLabel.setFont(Font.font("Arial", 18));
        volumeLabel.setMinWidth(50);

        hbox.getChildren().addAll(label, slider, volumeLabel);
        return hbox;
    }

    /**
     * Creates a key binding configuration UI element.
     *
     * @param labelText       The label text for the control.
     * @param currentKey      The current key binding.
     * @param setter          A consumer to update the key binding.
     * @param buttonReference A consumer to update the button reference.
     * @return A VBox containing the key binding configuration elements.
     */
    private VBox createKeySetting(String labelText, KeyCode currentKey, java.util.function.Consumer<KeyCode> setter, java.util.function.Consumer<Button> buttonReference) {
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 18));

        Button keyBtn = new Button(currentKey.getName());
        keyBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        keyBtn.setEffect(new DropShadow(3, Color.BLACK));
        buttonReference.accept(keyBtn);

        keyBtn.setOnAction(e -> {
            Stage keyStage = new Stage();
            keyStage.setTitle("Set " + labelText);
            Label instruction = new Label("Press any key to set " + labelText);
            instruction.setTextFill(Color.WHITE);
            instruction.setFont(Font.font("Arial", 16));
            VBox vbox = new VBox(20, instruction);
            vbox.setAlignment(Pos.CENTER);
            vbox.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
            Scene keyScene = new Scene(vbox, 300, 100);

            keyScene.setOnKeyPressed(event -> {
                KeyCode newKey = event.getCode();
                setter.accept(newKey);
                keyBtn.setText(newKey.getName());
                keyStage.close();
            });

            keyStage.setScene(keyScene);
            keyStage.show();
        });

        VBox vbox = new VBox(5, label, keyBtn);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    /**
     * Creates a styled button with hover effects.
     *
     * @param text   The text to display on the button.
     * @param action The action to perform when the button is clicked.
     * @return A styled Button.
     */
    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 18));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-border-color: white; -fx-border-width: 2px; -fx-padding: 10 20;");
        button.setEffect(new DropShadow(3, Color.BLACK));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-border-color: white; -fx-border-width: 2px; -fx-padding: 10 20; -fx-text-fill: black;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-border-color: white; -fx-border-width: 2px; -fx-padding: 10 20; -fx-text-fill: white;"));
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Refreshes the settings page UI components to reflect the current state.
     */
    public void refresh() {
        upKeyBtn.setText(keyBindings.getUpKey().getName());
        downKeyBtn.setText(keyBindings.getDownKey().getName());
        leftKeyBtn.setText(keyBindings.getLeftKey().getName());
        rightKeyBtn.setText(keyBindings.getRightKey().getName());

        bgmSlider.setValue(SoundComponent.getBgmVolume());
        bgmVolumeLabel.setText(String.format("%.0f%%", bgmSlider.getValue() * 100));

        sfxSlider.setValue(SoundComponent.getSfxVolume());
        sfxVolumeLabel.setText(String.format("%.0f%%", sfxSlider.getValue() * 100));
    }

    /**
     * Returns the root container for the settings page.
     *
     * @return The root container.
     */
    public StackPane getRoot() {
        return root;
    }

    /**
     * Sets the action to be performed when the back button is clicked.
     *
     * @param backAction The back action.
     */
    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }
}
