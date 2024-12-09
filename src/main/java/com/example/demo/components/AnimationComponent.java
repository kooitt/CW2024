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

    protected final List<Image> explosionFrames = loadFrames("explosion", TOTAL_EXPLOSION_FRAMES);
    protected final List<Image> levelupFrames = loadFrames("levelup", TOTAL_LEVELUP_FRAMES);

    private final Group parentGroup;

    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    protected List<Image> loadFrames(String prefix, int totalFrames) {
        List<Image> frames = new ArrayList<>();
        for (int i = 1; i <= totalFrames; i++) {
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + prefix + i + ".png");
            if (imgUrl != null) {
                frames.add(new Image(imgUrl.toExternalForm()));
            }
        }
        return frames;
    }

    public void playExplosion(double x, double y, double scale, Runnable onFinished) {
        playAnimation(explosionFrames, x, y, scale, EXPLOSION_FRAME_DURATION, onFinished);
    }

    public void playExplosion(double x, double y, double scale) {
        playExplosion(x, y, scale, null);
    }

    public void playLevelUpRelative(Node parent, double offsetX, double offsetY, double scale) {
        Platform.runLater(() -> {
            ImageView imageView = createImageView(scale, offsetX, offsetY);
            Group animationGroup = new Group(imageView);
            if (parent instanceof Group) {
                ((Group) parent).getChildren().add(animationGroup);
            }

            Timeline timeline = createTimeline(levelupFrames, imageView, LEVELUP_FRAME_DURATION, () -> {
                if (parent instanceof Group) {
                    ((Group) parent).getChildren().remove(animationGroup);
                }
            });
            timeline.play();
        });
    }

    private void playAnimation(List<Image> frames, double x, double y, double scale) {
        playAnimation(frames, x, y, scale, AnimationComponent.LEVELUP_FRAME_DURATION, null);
    }

    private void playAnimation(List<Image> frames, double x, double y, double scale, double frameDuration, Runnable onFinished) {
        Platform.runLater(() -> {
            ImageView imageView = createImageView(scale, 0, 0);
            setImageViewSize(imageView, frames == explosionFrames ? 100 : 50, scale);

            Group animationGroup = new Group(imageView);
            animationGroup.setLayoutX(x);
            animationGroup.setLayoutY(y);
            parentGroup.getChildren().add(animationGroup);

            Timeline timeline = createTimeline(frames, imageView, frameDuration, () -> {
                parentGroup.getChildren().remove(animationGroup);
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            timeline.play();
        });
    }

    private ImageView createImageView(double scale, double offsetX, double offsetY) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50 * scale);
        imageView.setFitHeight(50 * scale);
        imageView.setLayoutX(-25 * scale + offsetX);
        imageView.setLayoutY(-25 * scale + offsetY);
        return imageView;
    }

    private void setImageViewSize(ImageView imageView, double baseSize, double scale) {
        imageView.setFitWidth(baseSize * scale);
        imageView.setFitHeight(baseSize * scale);
        imageView.setLayoutX(-baseSize / 2 * scale);
        imageView.setLayoutY(-baseSize / 2 * scale);
    }

    private Timeline createTimeline(List<Image> frames, ImageView imageView, double frameDuration, Runnable onFinished) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < frames.size(); i++) {
            final int frame = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis((frame + 1) * frameDuration), e -> imageView.setImage(frames.get(frame))));
        }
        timeline.setOnFinished(e -> onFinished.run());
        return timeline;
    }
}