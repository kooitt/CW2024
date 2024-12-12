module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    exports com.example.demo.controller;
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.images to javafx.fxml;
    opens com.example.demo.levelparent to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.projectile to javafx.fxml;
    opens com.example.demo.actors.enemies to javafx.fxml;
    opens com.example.demo.actors.obstacles to javafx.fxml;
    opens com.example.demo.actors.player to javafx.fxml;
}