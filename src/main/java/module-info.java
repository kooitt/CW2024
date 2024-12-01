module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    //opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.images to javafx.fxml;
    opens com.example.demo.levelparent to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.projectile to javafx.fxml;
}