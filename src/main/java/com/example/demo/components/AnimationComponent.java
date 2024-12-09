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

/**
 * Handles animated effects for game components, such as explosions and level-up animations.
 */
public class AnimationComponent {

    /**
     * Base directory for loading image assets.
     */
    private static final String IMAGE_LOCATION = "/com/example/demo/images/";

    /**
     * Total number of frames for the explosion animation.
     */
    private static final int TOTAL_EXPLOSION_FRAMES = 13;

    /**
     * Duration of each frame in the explosion animation, in milliseconds.
     */
    private static final double EXPLOSION_FRAME_DURATION = 50;

    /**
     * Total number of frames for the level-up animation.
     */
    private static final int TOTAL_LEVELUP_FRAMES = 6;

    /**
     * Duration of each frame in the level-up animation, in milliseconds.
     */
    private static final double LEVELUP_FRAME_DURATION = 50;

    /**
     * Preloaded frames for the explosion animation.
     */
    protected static final List<Image> explosionFrames = loadFrames("explosion", TOTAL_EXPLOSION_FRAMES);

    /**
     * Preloaded frames for the level-up animation.
     */
    protected static final List<Image> levelupFrames = loadFrames("levelup", TOTAL_LEVELUP_FRAMES);

    /**
     * Parent group where animations are rendered.
     */
    private final Group parentGroup;

    /**
     * Constructs an {@code AnimationComponent} instance.
     *
     * @param parentGroup the parent {@link Group} where animations will be displayed
     */
    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    /**
     * Loads animation frames from the specified directory.
     *
     * @param prefix      the prefix of the image file names
     * @param totalFrames the total number of frames to load
     * @return a list of {@link Image} objects representing the animation frames
     */
    protected static List<Image> loadFrames(String prefix, int totalFrames) {
        List<Image> frames = new ArrayList<>();
        for (int i = 1; i <= totalFrames; i++) {
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + prefix + i + ".png");
            if (imgUrl != null) {
                frames.add(new Image(imgUrl.toExternalForm()));
            }
        }
        return frames;
    }

    /**
     * Plays an explosion animation at the specified location.
     *
     * @param x          the x-coordinate of the explosion
     * @param y          the y-coordinate of the explosion
     * @param scale      the scale of the explosion
     * @param onFinished a callback to be executed when the animation finishes, or {@code null}
     */
    public void playExplosion(double x, double y, double scale, Runnable onFinished) {
        playAnimation(x, y, scale, onFinished);
    }

    /**
     * Plays an explosion animation at the specified location with no callback.
     *
     * @param x     the x-coordinate of the explosion
     * @param y     the y-coordinate of the explosion
     * @param scale the scale of the explosion
     */
    public void playExplosion(double x, double y, double scale) {
        playExplosion(x, y, scale, null);
    }

    /**
     * Plays a level-up animation relative to a parent node.
     *
     * @param parent   the parent node where the animation will be anchored
     * @param offsetX  the x-offset relative to the parent node
     * @param offsetY  the y-offset relative to the parent node
     * @param scale    the scale of the animation
     */
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

    /**
     * Plays a generic animation using the specified frames and settings.
     *
     * @param x          the x-coordinate where the animation will be displayed
     * @param y          the y-coordinate where the animation will be displayed
     * @param scale      the scale of the animation
     * @param onFinished a callback to be executed when the animation finishes, or {@code null}
     */
    private void playAnimation(double x, double y, double scale, Runnable onFinished) {
        Platform.runLater(() -> {
            ImageView imageView = createImageView(scale, 0, 0);
            setImageViewSize(imageView, scale);

            Group animationGroup = new Group(imageView);
            animationGroup.setLayoutX(x);
            animationGroup.setLayoutY(y);
            parentGroup.getChildren().add(animationGroup);

            Timeline timeline = createTimeline(AnimationComponent.explosionFrames, imageView, AnimationComponent.EXPLOSION_FRAME_DURATION, () -> {
                parentGroup.getChildren().remove(animationGroup);
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            timeline.play();
        });
    }

    /**
     * Creates an {@link ImageView} for displaying animation frames.
     *
     * @param scale    the scale of the image
     * @param offsetX  the x-offset of the image
     * @param offsetY  the y-offset of the image
     * @return the initialized {@link ImageView}
     */
    private ImageView createImageView(double scale, double offsetX, double offsetY) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50 * scale);
        imageView.setFitHeight(50 * scale);
        imageView.setLayoutX(-25 * scale + offsetX);
        imageView.setLayoutY(-25 * scale + offsetY);
        return imageView;
    }

    /**
     * Sets the size and position of an {@link ImageView}.
     *
     * @param imageView the {@link ImageView} to modify
     * @param scale     the scale factor
     */
    private void setImageViewSize(ImageView imageView, double scale) {
        imageView.setFitWidth((double) 100 * scale);
        imageView.setFitHeight((double) 100 * scale);
        imageView.setLayoutX(-(double) 100 / 2 * scale);
        imageView.setLayoutY(-(double) 100 / 2 * scale);
    }

    /**
     * Creates a {@link Timeline} for animating a sequence of frames.
     *
     * @param frames        the list of {@link Image} frames
     * @param imageView     the {@link ImageView} to update
     * @param frameDuration the duration of each frame in milliseconds
     * @param onFinished    a callback to be executed when the animation finishes
     * @return the constructed {@link Timeline}
     */
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
