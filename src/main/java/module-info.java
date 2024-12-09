/**
 * This module descriptor defines the module `com.example.demo` and its dependencies,
 * as well as the exported packages and specific packages opened for reflection.
 * The module utilizes JavaFX for UI and multimedia functionalities.
 */
module com.example.demo {

    /*
      Requires the JavaFX Controls module, which provides UI controls and other features.
     */
    requires javafx.controls;

    /*
      Requires the JavaFX FXML module, used for loading and managing FXML files in the application.
     */
    requires javafx.fxml;

    /*
      Requires the JavaFX Media module, enabling the use of audio and video media in the application.
     */
    requires javafx.media;

    /*
      Requires the Java Desktop module for AWT-based or desktop-specific utilities.
     */
    requires java.desktop;

    /*
      Requires the JavaFX Swing module, enabling integration between JavaFX and Swing components.
     */
    requires javafx.swing;

    /*
      Exports the `com.example.demo.controller` package, which contains the controller logic for the application.
     */
    exports com.example.demo.controller;

    /*
      Exports the `com.example.demo.actors.Actor` package, which contains classes and logic
      related to the actors (game entities) in the application.
     */
    exports com.example.demo.actors.Actor;

    /*
      Exports the `com.example.demo.actors.Projectile` package, which manages projectile-related
      logic and classes used in the game.
     */
    exports com.example.demo.actors.Projectile;

    /*
      Exports the `com.example.demo.levels` package, which implements various game levels
      and their corresponding functionality.
     */
    exports com.example.demo.levels;

    /*
      Exports the `com.example.demo.ui` package, which includes user interface components
      such as buttons, overlays, and other visual elements.
     */
    exports com.example.demo.ui;

    /*
      Exports the `com.example.demo.utils` package, which provides utility classes for general-purpose
      functionalities, such as pooling, key bindings, and settings management.
     */
    exports com.example.demo.utils;

    /*
      Exports the `com.example.demo.interfaces` package, which defines interfaces for modular and
      flexible design, ensuring proper abstraction and dependency management.
     */
    exports com.example.demo.interfaces;

    /*
      Exports the `com.example.demo.components` package, which houses the core components of the application,
      including collision detection, health systems, and other reusable game elements.
     */
    exports com.example.demo.components;

    /*
      Opens the `com.example.demo.controller` package to the JavaFX FXML module.
      This allows reflection-based access required for loading and interacting with FXML files.
     */
    opens com.example.demo.controller to javafx.fxml;
}
