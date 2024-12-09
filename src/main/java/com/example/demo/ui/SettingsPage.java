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

public class SettingsPage {
    private StackPane root;
    private final Controller controller;
    private final KeyBindings keyBindings;
    private Runnable backAction;
    private Button upKeyBtn, downKeyBtn, leftKeyBtn, rightKeyBtn;
    private Slider bgmSlider, sfxSlider; // 音量滑动条
    private Label bgmVolumeLabel, sfxVolumeLabel; // 音量指示标签

    public SettingsPage(Controller controller) {
        this.controller = controller;
        this.keyBindings = KeyBindings.getInstance();
        initialize();
    }

    private void initialize() {
        root = new StackPane();
        // 设置根容器背景
        setSemiTransparentBackground();

        // 创建主内容容器
        VBox contentBox = new VBox(30); // 垂直布局间距为 30
        contentBox.setAlignment(Pos.CENTER); // 确保内容居中
        contentBox.setPadding(new Insets(40)); // 设置内边距
        contentBox.setMaxWidth(600); // 限制宽度，防止过于宽大
        contentBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20;"); // 半透明背景和圆角

        // 添加标题
        Label title = new Label("Settings");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", 36));
        title.setEffect(new DropShadow(5, Color.BLACK));

        // 音量调节部分
        VBox volumeBox = new VBox(30); // 设置音量部分的间距
        volumeBox.setAlignment(Pos.CENTER); // 居中对齐

        // 添加 BGM 和 SFX 音量滑动条
        bgmSlider = new Slider(0, 1, SoundComponent.getBgmVolume());
        sfxSlider = new Slider(0, 1, SoundComponent.getSfxVolume());

        // BGM 音量
        HBox bgmBox = createVolumeControl("BGM Volume:", bgmSlider, bgmVolumeLabel = new Label(String.format("%.0f%%", SoundComponent.getBgmVolume() * 100)));
        bgmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundComponent.setBgmVolume(newVal.doubleValue()); // 更新背景音乐音量
            bgmVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        // SFX 音量
        HBox sfxBox = createVolumeControl("SFX Volume:", sfxSlider, sfxVolumeLabel = new Label(String.format("%.0f%%", SoundComponent.getSfxVolume() * 100)));
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundComponent.setSfxVolume(newVal.doubleValue()); // 更新音效音量
            sfxVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        volumeBox.getChildren().addAll(bgmBox, sfxBox);

        // 键位设置部分
        VBox keyBox = new VBox(20); // 设置键位部分的间距
        keyBox.setAlignment(Pos.CENTER);

        // 添加键位按钮
        keyBox.getChildren().addAll(
                createKeySetting("Up Key:", keyBindings.getUpKey(), keyBindings::setUpKey, btn -> upKeyBtn = btn),
                createKeySetting("Down Key:", keyBindings.getDownKey(), keyBindings::setDownKey, btn -> downKeyBtn = btn),
                createKeySetting("Left Key:", keyBindings.getLeftKey(), keyBindings::setLeftKey, btn -> leftKeyBtn = btn),
                createKeySetting("Right Key:", keyBindings.getRightKey(), keyBindings::setRightKey, btn -> rightKeyBtn = btn)
        );

        // 按钮部分
        HBox buttonsBox = new HBox(30); // 设置按钮间距
        buttonsBox.setAlignment(Pos.CENTER);

        Button backBtn = createStyledButton("Back", () -> {
            if (backAction != null) backAction.run();
        });

        Button returnToMainMenuBtn = createStyledButton("Return to Main Menu", controller::returnToMainMenu);

        buttonsBox.getChildren().addAll(backBtn, returnToMainMenuBtn);

        // 将所有部分添加到内容框中
        contentBox.getChildren().addAll(title, volumeBox, keyBox, buttonsBox);

        // 将内容框添加到根容器中并居中对齐
        root.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER); // 设置内容框在 StackPane 中居中对齐

        // 确保根容器大小动态适应
        root.setPrefSize(controller.getStage().getWidth(), controller.getStage().getHeight());
    }



    private void setSemiTransparentBackground() {
        // 设置半透明黑色背景
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.rgb(0, 0, 0, 0.8), // 半透明黑色
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));
    }

    private HBox createVolumeControl(String labelText, Slider slider, Label volumeLabel) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER); // 居中对齐

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

    private VBox createKeySetting(String labelText, KeyCode currentKey, java.util.function.Consumer<KeyCode> setter, java.util.function.Consumer<Button> buttonReference) {
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 18));

        Button keyBtn = new Button(currentKey.getName());
        keyBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        keyBtn.setEffect(new DropShadow(3, Color.BLACK));
        keyBtn.setOnMouseEntered(e -> keyBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 10 20;"));
        keyBtn.setOnMouseExited(e -> keyBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;"));

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
        vbox.setAlignment(Pos.CENTER); // 居中对齐
        return vbox;
    }

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

    public void refresh() {
        upKeyBtn.setText(keyBindings.getUpKey().getName());
        downKeyBtn.setText(keyBindings.getDownKey().getName());
        leftKeyBtn.setText(keyBindings.getLeftKey().getName());
        rightKeyBtn.setText(keyBindings.getRightKey().getName());

        // 同步滑动条的值和标签
        bgmSlider.setValue(SoundComponent.getBgmVolume());
        bgmVolumeLabel.setText(String.format("%.0f%%", bgmSlider.getValue() * 100));

        sfxSlider.setValue(SoundComponent.getSfxVolume());
        sfxVolumeLabel.setText(String.format("%.0f%%", sfxSlider.getValue() * 100));
    }

    public StackPane getRoot() {
        return root;
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }
}
