package com.example.demo.actors;

import com.example.demo.components.MovementComponent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ActiveActor extends Actor {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	protected ImageView imageView;
	protected MovementComponent movementComponent;

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super();
		imageView = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		imageView.setFitHeight(imageHeight);
		imageView.setPreserveRatio(true);
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.getChildren().add(imageView);

		// 初始化 MovementComponent，初始速度为 0
		movementComponent = new MovementComponent(0, 0);
	}

	public void updatePosition() {
		// 使用 MovementComponent 更新位置
		movementComponent.update(this);
	}

	public void updateActor() {
		updatePosition();
	}

	// 提供对 MovementComponent 的访问方法
	public MovementComponent getMovementComponent() {
		return movementComponent;
	}

	public void setMovementComponent(MovementComponent movementComponent) {
		this.movementComponent = movementComponent;
	}
}
