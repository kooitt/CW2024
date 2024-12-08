package com.example.demo.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnimationComponent {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";
    private static final int TOTAL_EXPLOSION_FRAMES = 13;
    private static final double EXPLOSION_FRAME_DURATION = 50;
    private static final int TOTAL_LEVELUP_FRAMES = 6;
    private static final double LEVELUP_FRAME_DURATION = 50;

    private static final List<Image> explosionFrames = loadFrames("explosion", TOTAL_EXPLOSION_FRAMES);
    private static final List<Image> levelupFrames = loadFrames("levelup", TOTAL_LEVELUP_FRAMES);

    private Group parentGroup;

    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    private static List<Image> loadFrames(String prefix, int totalFrames) {
        List<Image> frames = new ArrayList<>();
        for (int i = 1; i <= totalFrames; i++) {
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + prefix + i + ".png");
            if (imgUrl != null) {
                frames.add(new Image(imgUrl.toExternalForm()));
            }
        }
        return frames;
    }

    public void playLevelUp(double x, double y, double scale) {
        playAnimation(levelupFrames, x, y, scale, LEVELUP_FRAME_DURATION);
    }

    public void playExplosion(double x, double y, double scale, Runnable onFinished) {
        playAnimation(explosionFrames, x, y, scale, EXPLOSION_FRAME_DURATION, onFinished);
    }

    public void playExplosion(double x, double y, double scale) {
        playExplosion(x, y, scale, null);
    }

    private void playAnimation(List<Image> frames, double x, double y, double scale, double frameDuration) {
        playAnimation(frames, x, y, scale, frameDuration, null);
    }
    public void playLevelUpRelative(Node parent, double offsetX, double offsetY, double scale) {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(50 * scale);
            imageView.setFitHeight(50 * scale);
            imageView.setLayoutX(-25 * scale + offsetX);
            imageView.setLayoutY(-25 * scale + offsetY);

            Group animationGroup = new Group(imageView);
            parent.getParent().getChildrenUnmodifiable().forEach(node -> {
                // 确保 parent 是 Player (UserPlane)
            });

            // 这里的关键是把 animationGroup 加到用户飞机这个 Node 的子级中
            if (parent instanceof Group) {
                ((Group) parent).getChildren().add(animationGroup);
            }

            Timeline timeline = new Timeline();
            for (int i = 0; i < levelupFrames.size(); i++) {
                final int frame = i;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis((frame + 1) * LEVELUP_FRAME_DURATION),
                        e -> imageView.setImage(levelupFrames.get(frame))));
            }

            timeline.setOnFinished(e -> {
                ((Group) parent).getChildren().remove(animationGroup);
            });

            timeline.play();
        });
    }

    private void playAnimation(List<Image> frames, double x, double y, double scale, double frameDuration, Runnable onFinished) {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);

            // 为不同的动画类型设置不同的基准大小
            if (frames == explosionFrames) {
                imageView.setFitWidth(100 * scale); // 爆炸动画使用更大的基准大小
                imageView.setFitHeight(100 * scale);
                imageView.setLayoutX(-50 * scale);
                imageView.setLayoutY(-50 * scale);
            } else {
                imageView.setFitWidth(50 * scale); // 升级动画使用较小的基准大小
                imageView.setFitHeight(50 * scale);
                imageView.setLayoutX(-25 * scale);
                imageView.setLayoutY(-25 * scale);
            }

            Group animationGroup = new Group(imageView);
            animationGroup.setLayoutX(x);
            animationGroup.setLayoutY(y);
            parentGroup.getChildren().add(animationGroup);

            Timeline timeline = new Timeline();
            for (int i = 0; i < frames.size(); i++) {
                final int frame = i;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis((frame + 1) * frameDuration), e -> {
                    imageView.setImage(frames.get(frame));
                }));
            }

            timeline.setOnFinished(e -> {
                parentGroup.getChildren().remove(animationGroup);
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            timeline.play();
        });
    }
}
