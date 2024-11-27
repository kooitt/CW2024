package com.example.demo.components;

// 移除与盾牌相关的import和变量

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnimationComponent {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";
    private static final int TOTAL_EXPLOSION_FRAMES = 13;
    private static final double EXPLOSION_FRAME_DURATION = 50; // 毫秒

    private static final List<Image> explosionFrames = new ArrayList<>();

    static {
        for (int i = 1; i <= TOTAL_EXPLOSION_FRAMES; i++) {
            String imageName = "explosion" + i + ".png";
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + imageName);
            if (imgUrl == null) {
                continue;
            }
            Image img = new Image(imgUrl.toExternalForm());
            explosionFrames.add(img);
        }
    }

    private Group parentGroup;

    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
        // 初始化盾牌逻辑已移至 ShieldDisplay
    }

    /**
     * 播放爆炸动画
     *
     * @param x     爆炸中心的X坐标
     * @param y     爆炸中心的Y坐标
     * @param scale 爆炸动画的缩放比例
     */
    public void playExplosion(double x, double y, double scale) {
        Platform.runLater(() -> {
            ImageView explosionImageView = new ImageView();
            explosionImageView.setPreserveRatio(true);
            explosionImageView.setFitWidth(100 * scale);
            explosionImageView.setFitHeight(100 * scale);
            explosionImageView.setLayoutX(-50 * scale); // 使爆炸中心对齐
            explosionImageView.setLayoutY(-50 * scale);
            Group explosionGroup = new Group(explosionImageView);
            explosionGroup.setLayoutX(x);
            explosionGroup.setLayoutY(y);
            parentGroup.getChildren().add(explosionGroup);

            Timeline explosionTimeline = new Timeline();
            for (int i = 0; i < TOTAL_EXPLOSION_FRAMES && i < explosionFrames.size(); i++) {
                final int frame = i;
                KeyFrame keyFrame = new KeyFrame(Duration.millis((frame + 1) * EXPLOSION_FRAME_DURATION), e -> {
                    explosionImageView.setImage(explosionFrames.get(frame));
                });
                explosionTimeline.getKeyFrames().add(keyFrame);
            }

            explosionTimeline.setOnFinished(e -> {
                parentGroup.getChildren().remove(explosionGroup);
            });

            explosionTimeline.play();
        });
    }

    // 移除盾牌相关的方法
}
