package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;
import com.example.demo.Menu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	/*private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";*/
	private Controller myController;

	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Menu menu = new Menu(stage);
		menu.ShowMenu();

	}

	public static void main(String[] args) {
		launch();
	}
}