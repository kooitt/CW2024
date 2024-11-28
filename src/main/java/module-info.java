module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.Levels to javafx.fxml;
    opens com.example.demo.ImageInitializers to javafx.fxml;
    opens com.example.demo.LevelViews to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.Actors to javafx.fxml;
}