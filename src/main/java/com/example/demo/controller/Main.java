package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class  Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

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

	public static void main(String[] args) {
		launch();
	}
}