package com.example.demo.UI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {

	private static final String IMAGE_PATH = "/com/example/demo/images/shield.png"; // 图片路径
	private static final int SHIELD_SIZE = 200; // 护盾大小
	private static final Image SHIELD_IMAGE = new Image(ShieldImage.class.getResource(IMAGE_PATH).toExternalForm()); // 静态加载图片

	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition); // 设置 X 坐标
		this.setLayoutY(yPosition); // 设置 Y 坐标
		this.setImage(SHIELD_IMAGE); // 设置护盾图片
		this.setVisible(false); // 初始状态为不可见
		this.setFitHeight(SHIELD_SIZE); // 设置图片高度
		this.setFitWidth(SHIELD_SIZE); // 设置图片宽度
	}

	// 显示护盾
	public void showShield() {
		this.setVisible(true);
	}

	// 隐藏护盾
	public void hideShield() {
		this.setVisible(false);

	}



}
