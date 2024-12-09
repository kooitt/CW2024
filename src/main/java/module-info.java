module com.example.demo {
    // Required modules for JavaFX functionalities like controls, FXML, media, and Swing components.
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop; // For AWT or desktop-related utilities.
    requires javafx.swing; // For integrating Swing components with JavaFX.

    // Exports only necessary packages, adhering to the principles of encapsulation.
    exports com.example.demo.controller;       // Controller logic for the application.
    exports com.example.demo.actors.Actor;     // Actor-related classes and logic.
    exports com.example.demo.actors.Projectile; // Projectile-related classes and logic.
    exports com.example.demo.levels;           // Game level implementations.
    exports com.example.demo.ui;               // User interface components.
    exports com.example.demo.utils;            // Utility classes for general-purpose functionalities.
    exports com.example.demo.interfaces;       // Interface definitions for modular design.
    exports com.example.demo.components;       // Core components of the application.

    // Opens specific packages for reflection-based access, often required for FXML.
    opens com.example.demo.controller to javafx.fxml;
}
