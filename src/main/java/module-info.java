module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    // 仅导出需要暴露的包，符合封装原则
    exports com.example.demo.controller;
    exports com.example.demo.actors;
    exports com.example.demo.projectiles;
    exports com.example.demo.levels;
    exports com.example.demo.views;
    exports com.example.demo.ui;
    exports com.example.demo.utils;
    exports com.example.demo.interfaces;
    exports com.example.demo.components;

    // 如果有反射需求，例如与 FXML 配合，需要开放部分包
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.views to javafx.fxml;
}
