module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo.controller;
}