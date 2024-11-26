module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires transitive java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
    exports com.example.demo;
}
