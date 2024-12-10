module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.Actorpackage to javafx.fxml;
    opens com.example.demo.Projectile to javafx.fxml;
    opens com.example.demo.UI to javafx.fxml;
}