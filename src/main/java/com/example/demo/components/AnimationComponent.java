// AnimationComponent.java

package com.example.demo.components;

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
    public static final int SHIELD_SIZE = 100; // 公开以便Boss使用

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
    private ImageView shieldImageView;
    private Timeline shieldTimeline;
    private boolean isShieldActive;

    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
        initializeShield();
    }

    /**
     * 初始化盾牌图像
     */
    private void initializeShield() {
        shieldImageView = new ImageView();
        URL shieldImageUrl = getClass().getResource(IMAGE_LOCATION + "shield.png");
        if (shieldImageUrl == null) {
            return;
        }
        Image shieldImage = new Image(shieldImageUrl.toExternalForm());
        shieldImageView.setImage(shieldImage);
        shieldImageView.setFitWidth(SHIELD_SIZE);
        shieldImageView.setFitHeight(SHIELD_SIZE);
        shieldImageView.setPreserveRatio(true);
        shieldImageView.setVisible(false); // 初始隐藏
        parentGroup.getChildren().add(shieldImageView);
        isShieldActive = false;
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

    /**
     * 初始化盾牌激活逻辑
     *
     * @param intervalSeconds 盾牌激活的间隔时间（秒）
     * @param durationSeconds 盾牌持续的时间（秒）
     */
    public void initializeShieldLogic(double intervalSeconds, double durationSeconds) {
        shieldTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> activateShield()),
                new KeyFrame(Duration.seconds(durationSeconds), e -> deactivateShield()),
                new KeyFrame(Duration.seconds(intervalSeconds), e -> {}) // 空操作，用于设定周期长度
        );
        shieldTimeline.setCycleCount(Timeline.INDEFINITE);
        shieldTimeline.play();
    }

    /**
     * 激活盾牌
     */
    private void activateShield() {
        isShieldActive = true;
        shieldImageView.setVisible(true);
        shieldImageView.toFront(); // 确保盾牌在最前端
    }

    /**
     * 禁用盾牌
     */
    private void deactivateShield() {
        isShieldActive = false;
        shieldImageView.setVisible(false);
    }

    /**
     * 设置盾牌的位置
     *
     * @param x 盾牌中心的X坐标
     * @param y 盾牌中心的Y坐标
     */
    public void setShieldPosition(double x, double y) {
        Platform.runLater(() -> {
            shieldImageView.setLayoutX(x + SHIELD_SIZE * 1.5);
            shieldImageView.setLayoutY(y - SHIELD_SIZE / 2.0);
        });
    }

    /**
     * 检查盾牌是否激活
     *
     * @return 是否激活
     */
    public boolean isShieldActive() {
        return isShieldActive;
    }

    /**
     * 停止盾牌逻辑
     */
    public void stopShieldLogic() {
        if (shieldTimeline != null) {
            shieldTimeline.stop();
        }
        deactivateShield();
    }
}
