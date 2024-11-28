module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    // opens com.example.demo to javafx.fxml; // to be removed later
    exports com.example.demo.controller;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.images to javafx.fxml;
    opens com.example.demo.levelviews to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.audio to javafx.fxml;
    opens com.example.demo.projectiles to javafx.fxml;
    opens com.example.demo.planes to javafx.fxml;
}