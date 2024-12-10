package com.example.demo.controller;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartMenu {
    private final Stage stage;

    public StartMenu(Stage stage) {
        this.stage = stage;
    }

    public void showMenu(Controller controller) {
        // 加载背景图片
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/background1.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(stage.getWidth());
        backgroundView.setFitHeight(stage.getHeight());
        backgroundView.setPreserveRatio(false);

        // 创建标题
        Text title = createTitleText("SKY BATTLE");

        // 创建按钮
        Button startButton = new Button("Start");
        Button outButton = new Button("Exit");

        // 设置按钮样式
        startButton.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-background-radius: 50px; " +
                        "-fx-padding: 10 30; " +
                        "-fx-background-color: #FFFFFF; " +  // 蓝色背景
                        "-fx-text-fill: darkblue; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);"
        );

        outButton.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-background-radius: 50px; " +
                        "-fx-padding: 10 30; " +
                        "-fx-background-color: #00317B; " +  // 绿色背景
                        "-fx-text-fill: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);"
        );

        // 添加按钮动作
        startButton.setOnAction(e -> controller.launchGame());
        outButton.setOnAction(e -> stage.close());

        // 创建水平布局（HBox）来放置按钮
        HBox buttonLayout = new HBox(300); // 按钮水平间距300
        buttonLayout.setAlignment(Pos.CENTER); // 按钮居中
        buttonLayout.getChildren().addAll(startButton, outButton);

        // 创建垂直布局（VBox）来控制标题和按钮的垂直位置
        VBox verticalLayout = new VBox(20); // 设置按钮垂直间距
        verticalLayout.setAlignment(Pos.CENTER);
        verticalLayout.setSpacing(100); // 设置按钮与背景之间的垂直间距
        verticalLayout.getChildren().addAll(title, buttonLayout); // 把标题和按钮添加到布局中

        // 将背景和布局叠加
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, verticalLayout);

        // 创建场景并显示
        Scene menuScene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(menuScene);
        stage.show();
    }

    // 创建大标题文本
    private Text createTitleText(String skyBattle) {
        Text title = new Text(skyBattle); // 使用 skyBattle 作为标题文本
        title.setFont(Font.font("Arial", 80));  // 设置字体大小
        title.setStyle("-fx-fill: white; -fx-font-weight: bold;");  // 设置字体颜色为白色，字体加粗
        return title;
    }
}

