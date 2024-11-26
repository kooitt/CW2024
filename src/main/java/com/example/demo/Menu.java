import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import com.example.demo.controller.Controller;
import com.example.demo.controller.Main;
import com.example.demo.LevelOne;


public class Menu extends Main{

    private static final String MENU_BACKGROUND = "/com/example/demo/images/menu_background.png";

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;
    private static final String TITLE = "Sky Battle";
    private Group root;
    private Stage stage;
    private Scene scene;
    private Controller controller;

    public Menu (Stage stage){
        this.stage = stage;
        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setWidth(SCREEN_WIDTH);
        root= new Group();
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);

    }

    public void BuildMenu(){
        
        Image menu_background = new Image(getClass().getResource(MENU_BACKGROUND).toExternalForm());

        //Creating buttons 
        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(e -> controller.launchGame());

        Button exitButton = new Button("Exit");
        exitButton.setOnMouseClicked(e -> System.exit(0));

        //Centering buttons
        StackPane stack = new StackPane();
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.add(startButton, 0, 0);
        grid.add(exitButton, 0, 1);

        stack.getChildren().add(grid);
        root.getChildren().add(stack);

    }

    public void ShowMenu(){
        stage.show();
    }


}