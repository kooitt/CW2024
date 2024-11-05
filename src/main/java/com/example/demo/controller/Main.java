package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class  Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			URL fxmlLocation = getClass().getClassLoader().getResource("MenuScreen.fxml");
			System.out.println("FXML Location: " + fxmlLocation);
			FXMLLoader loader = new FXMLLoader(fxmlLocation);
			Parent root = loader.load();
			MenuController controller = loader.getController();
			controller.setStage(stage);

			Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
			stage.setTitle(TITLE);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
//			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		try {
//			URL fxmlLocation = getClass().getClassLoader().getResource("MenuScreen.fxml");
//			FXMLLoader loader = new FXMLLoader(fxmlLocation);
////			Parent root = loader.load();
//			Group root = new Group();
////			MenuController controller = loader.getController();
////			controller.setStage(stage);
//
//			Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
//			stage.setTitle(TITLE);
//			Image test = new Image(getClass().getResourceAsStream("/com/example/demo/images/shield.png"));
//			ImageView piece = new ImageView(test);
//			piece.setX(10);
//			piece.setY(10);
//
//			Rectangle rct = new Rectangle(50, 150, 500, 300);
//			rct.setFill(Color.GRAY);
//
//			root.getChildren().addAll(rct, piece);
//			stage.setScene(scene);
//			stage.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) {
		launch();
	}
}