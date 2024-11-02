module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo.controller;
}