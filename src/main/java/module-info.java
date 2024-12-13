module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    //requires javafx.media;


    exports com.example.demo.controller;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.actors.projectiles to javafx.fxml;
    opens com.example.demo.actors.shield to javafx.fxml;
    opens com.example.demo.util to javafx.fxml;
    opens com.example.demo.ui to javafx.fxml;
    opens com.example.demo.collision to javafx.fxml;
    opens com.example.demo.actors.planes to javafx.fxml;
    opens com.example.demo.actors.core to javafx.fxml;
    opens com.example.demo.actors.planes.bosses to javafx.fxml;
}