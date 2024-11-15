package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that serves as the entry point for the JavaFX application.
 */
public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	/**
	 * The main entry point for all JavaFX applications.
	 *
	 * @param stage the primary stage for this application, onto which
	 *              the application scene can be set.
	 * @throws ClassNotFoundException if the class cannot be located.
	 * @throws NoSuchMethodException if a particular method cannot be found.
	 * @throws SecurityException if a security violation occurs.
	 * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
	 * @throws IllegalAccessException if the underlying constructor is inaccessible.
	 * @throws IllegalArgumentException if the method is invoked with incorrect arguments.
	 * @throws InvocationTargetException if the underlying constructor throws an exception.
	 */
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

	/**
	 * The main method which launches the JavaFX application.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		launch();
	}
}