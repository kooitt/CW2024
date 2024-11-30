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

/**
 * Handles animations such as explosions.
 */
public class AnimationComponent {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";
    private static final int TOTAL_EXPLOSION_FRAMES = 13;
    private static final double EXPLOSION_FRAME_DURATION = 50;
    //添加了一个levelup动画特效
    private static final int TOTAL_LEVELUP_FRAMES = 6;
    private static final double LEVELUP_FRAME_DURATION = 50;

    private static final List<Image> explosionFrames = new ArrayList<>();

    static {
        for (int i = 1; i <= TOTAL_EXPLOSION_FRAMES; i++) {
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + "explosion" + i + ".png");
            if (imgUrl != null) {
                explosionFrames.add(new Image(imgUrl.toExternalForm()));
            }
        }
    }

    private static final List<Image> levelupFrames = new ArrayList<>();

    static {
        for (int i = 1; i <= TOTAL_LEVELUP_FRAMES; i++) {
            URL imgUrl = AnimationComponent.class.getResource(IMAGE_LOCATION + "levelup" + i + ".png");
            if (imgUrl != null) {
                levelupFrames.add(new Image(imgUrl.toExternalForm()));
            }
        }
    }

    private Group parentGroup;

    /**
     * Constructs an AnimationComponent with the specified parent group.
     *
     * @param parentGroup the parent group for animations.
     */
    public AnimationComponent(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    /**
     * Plays a level-up animation at the specified position and scale.
     *
     * @param x     X position.
     * @param y     Y position.
     * @param scale scaling factor.
     */
    public void playLevelUp(double x, double y, double scale) {
        Platform.runLater(() -> {
            ImageView levelupView = new ImageView();
            levelupView.setPreserveRatio(true);
            levelupView.setFitWidth(50 * scale);
            levelupView.setFitHeight(50 * scale);
            levelupView.setLayoutX(-25 * scale);
            levelupView.setLayoutY(-25 * scale);
            Group levelupGroup = new Group(levelupView);
            levelupGroup.setLayoutX(x);
            levelupGroup.setLayoutY(y);
            parentGroup.getChildren().add(levelupGroup);

            Timeline timeline = new Timeline();
            for (int i = 0; i < levelupFrames.size(); i++) {
                final int frame = i;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis((frame + 1) * LEVELUP_FRAME_DURATION), e -> {
                    levelupView.setImage(levelupFrames.get(frame));
                }));
            }

            timeline.setOnFinished(e -> parentGroup.getChildren().remove(levelupGroup));
            timeline.play();
        });
    }


    /**
     * Plays an explosion animation at the specified position and scale.
     *
     * @param x     X position.
     * @param y     Y position.
     * @param scale scaling factor.
     */
    public void playExplosion(double x, double y, double scale) {
        Platform.runLater(() -> {
            ImageView explosionView = new ImageView();
            explosionView.setPreserveRatio(true);
            explosionView.setFitWidth(100 * scale);
            explosionView.setFitHeight(100 * scale);
            explosionView.setLayoutX(-50 * scale);
            explosionView.setLayoutY(-50 * scale);
            Group explosionGroup = new Group(explosionView);
            explosionGroup.setLayoutX(x);
            explosionGroup.setLayoutY(y);
            parentGroup.getChildren().add(explosionGroup);

            Timeline timeline = new Timeline();
            for (int i = 0; i < explosionFrames.size(); i++) {
                final int frame = i;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis((frame + 1) * EXPLOSION_FRAME_DURATION), e -> {
                    explosionView.setImage(explosionFrames.get(frame));
                }));
            }

            timeline.setOnFinished(e -> parentGroup.getChildren().remove(explosionGroup));
            timeline.play();
        });
    }
}
