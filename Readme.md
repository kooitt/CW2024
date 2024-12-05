# Plane Shooter Game

A feature-rich 2D side-scrolling plane shooter game developed in Java using JavaFX. Fly through challenging levels, upgrade your plane and weapons, and defeat powerful bosses to achieve victory.

---

## Table of Contents

- [Plane Shooter Game](#plane-shooter-game)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Features](#features)
  - [Gameplay](#gameplay)
    - [Controls](#controls)
    - [Levels](#levels)
    - [Upgrades](#upgrades)
  - [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Steps to Install](#steps-to-install)
  - [Running the Game](#running-the-game)
    - [From IDE](#from-ide)
    - [From Command Line](#from-command-line)
  - [Project Structure](#project-structure)
    - [Core Classes](#core-classes)
    - [Assets](#assets)
  - [Development Roadmap](#development-roadmap)
  - [Contributing](#contributing)
    - [Contribution Guidelines](#contribution-guidelines)
  - [License](#license)

---

## Introduction

**Plane Shooter Game** is a classic arcade-style shooter where you control a plane to combat enemy waves and powerful bosses across multiple levels. Designed to be fun and engaging, the game offers a mix of strategic upgrades, fast-paced automatic shooting, and exciting challenges for players of all ages. Customize your controls to suit your playstyle and dive into the action!

---

## Features

- 🎮 **Dynamic Gameplay**: Battle diverse enemy types with unique attack patterns.
- 🚀 **Upgradable Planes**: Enhance your plane with collectible power-ups and unlock special weapons.
- 💥 **Challenging Boss Fights**: Test your skills with epic boss battles featuring advanced mechanics.
- 🎶 **Immersive Audio**: Background music and sound effects tailored for an exciting experience.
- 🖼️ **Custom Graphics**: Handcrafted visuals, including planes, backgrounds, and animations.
- 🛠️ **Optimized Performance**: Object pooling ensures smooth gameplay even during intense scenes.
- 🎛️ **Customizable Controls**: Default controls are set to arrow keys but can be customized to your preference.
- 🔫 **Automatic Shooting**: Focus on maneuvering your plane while it fires automatically.

---

## Gameplay

### Controls

- **Movement**:
  - **Default Keys**:
    - Move Up: `Up Arrow`
    - Move Down: `Down Arrow`
    - Move Left: `Left Arrow`
    - Move Right: `Right Arrow`
  - **Customizable**: You can change the key bindings to your preferred keys.

- **Shooting**:
  - **Automatic Shooting**: Your plane fires continuously without the need to press any buttons.

### Levels

1. **Level One**:
   - **Objective**: Eliminate 10 enemy planes.
   - **Boss**: None.
   - **Rewards**: Weapon upgrades and health items.
   - **Enemies**: Standard enemy planes with basic attack patterns.

2. **Level Two**:
   - **Objective**: Defeat the first boss with shielding mechanics.
   - **Boss**: A shielded enemy plane with advanced attacks.
   - **Rewards**: Advanced power-ups.
   - **Enemies**: Increased difficulty with more aggressive behavior.

3. **Level Three**:
   - **Objective**: Face off against the final boss.
   - **Boss**: A highly challenging enemy with dynamic attack patterns.
   - **Rewards**: Game completion bonus.
   - **Enemies**: Most challenging enemy planes and obstacles.

### Upgrades

- **Weapon Power-Ups**:
  - **Increase Fire Rate**: Collect power-ups to fire bullets faster.
  - **Extra Bullet Rows**: Upgrade to shoot multiple rows of bullets simultaneously.

- **Plane Enhancements**:
  - **Visual Upgrades**: Your plane's appearance changes with upgrades, reflecting its enhanced capabilities.
  - **Speed Boosts**: Enhance your plane's movement speed.

- **Health Items**:
  - **Heart Collectibles**: Restore your plane's health by picking up hearts.

---

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Version **19.0.2** or higher is required.
- **JavaFX SDK**: (if not bundled with your JDK).
- **Development Environment**: IntelliJ IDEA, or other IDEs.

### Steps to Install

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/fsfuzhu/CW2024.git
   cd CW2024
   git checkout 20241205
   ```

2. **Configure JavaFX**:
   - **For IDEs**:
     - Add the JavaFX library to your project settings.
     - In **VM options**, include:
       ```
       --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
       ```
     - Replace `/path/to/javafx-sdk/lib` with the actual path to your JavaFX SDK `lib` directory.

   - **For Command Line**:
     - Ensure the JavaFX SDK `lib` directory is included in your classpath.

3. **Build and Run**:
   - Open the project in your IDE.
   - Locate the `Main` class in the `com.example.demo` package.
   - Run the `Main` class to start the game.

---

## Running the Game

### From IDE

- **Step 1**: Open `Main.java` in the `com.example.demo` package.
- **Step 2**: Run the `Main` class.

### From Command Line

Compile and run the game using the following commands (ensure you replace `/path/to/javafx-sdk/lib` with the actual path):

```bash
javac -cp .:/path/to/javafx-sdk/lib/* com/example/demo/Main.java
java -cp .:/path/to/javafx-sdk/lib/* --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml com.example.demo.Main
```

---

## Project Structure

### Core Classes

| Class Name            | Description                                                                 |
|-----------------------|-----------------------------------------------------------------------------|
| `Main`                | Entry point of the application.                                             |
| `LevelParent`         | Abstract class containing shared level logic.                               |
| `LevelOne`, `LevelTwo`, `LevelThree` | Concrete classes representing each level in the game.         |
| `UserPlane`           | Represents the player's plane with movement and shooting capabilities.      |
| `EnemyPlane`          | Standard enemy units with basic behaviors.                                  |
| `Boss`, `BossTwo`     | Boss enemies with unique behaviors and attack patterns.                     |
| `ActiveActor`         | Base class for all game actors with health and movement components.         |
| `Projectile`          | Represents bullets fired by planes.                                         |
| `ShootingComponent`   | Manages shooting behavior for actors, including automatic firing.           |
| `CollisionComponent`  | Handles collision detection between game objects.                           |
| `SoundComponent`      | Manages background music and sound effects.                                 |
| `AnimationComponent`  | Manages animations such as explosions and power-up effects.                 |
| `KeyBindings`         | Allows customization of key bindings for player controls.                   |

### Assets

- **Images**: Stored in `resources/com/example/demo/images/`.
  - **Planes**: Player and enemy plane images (`userplane.png`, `enemyplane.png`, etc.).
  - **Bullets**: Images for different bullet types.
  - **Backgrounds**: Unique background images for each level.
  - **Animations**: Frames for explosion and level-up animations.

- **Audio**: Stored in `resources/com/example/demo/sounds/`.
  - **Background Music**: `level1.wav`, `level2.mp3`, etc.
  - **Sound Effects**: `bullet.wav`, `explosion.wav`, `bossdown.wav`, etc.

---

## Development Roadmap

- ✅ **Current Features**:
  - Basic gameplay mechanics with three levels.
  - Automatic shooting and customizable controls.
  - Power-ups and plane upgrades.
  - Collision detection and health system.
  - Sound effects and background music.

- 🛠️ **Planned Features**:
  - **New Enemy Types**: Introduce enemies with different behaviors and attack patterns.
  - **Additional Levels**: Expand the game with more challenging levels and environments.
  - **Multiplayer Mode**: Implement local co-op or competitive modes.
  - **Enhanced UI**: Improve game menus and in-game HUD for better user experience.
  - **Graphics Improvements**: Add more detailed sprites and animations.
  - **Settings Menu**: Provide an interface for players to customize controls and game settings.

- 📢 **Feedback Request**:
  - If you have any suggestions or feature ideas, please create an issue on GitHub or reach out to the maintainers.

---

## Contributing

We welcome contributions from the community! Please follow the guidelines below:

1. **Fork the Repository** and create a new branch:
   ```bash
   git checkout -b feature/your-feature
   ```
2. **Implement your feature or bug fix**.
3. **Test your changes thoroughly** to ensure they do not break existing functionality.
4. **Commit your changes** with clear and descriptive messages.
5. **Submit a Pull Request** with detailed information about your changes.

### Contribution Guidelines

- **Code Style**: Follow Java naming conventions and maintain consistent formatting.
- **Comments and Documentation**:
  - Use **Javadoc** comments for classes, methods, and significant code blocks.
  - Update the README or other documentation if your changes affect them.
- **Testing**:
  - Ensure existing tests pass.
  - Write new tests for any new functionality.

---

## License

This project is a coursework assignment for our institution. **Copying or plagiarizing any part of this project is strictly prohibited.**

- You are welcome to **view the code** to learn and understand how the game is implemented.
- Feel free to **download and play** the game for personal enjoyment.
- **Unauthorized distribution** of the code or claiming it as your own work is not allowed.

---

> **Contact**: For any questions or feedback, please open an issue or reach out to the maintainers.

---