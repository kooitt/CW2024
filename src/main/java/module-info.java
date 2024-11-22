module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.Levels to javafx.fxml;
    opens com.example.demo.ImageInitializers to javafx.fxml;
}