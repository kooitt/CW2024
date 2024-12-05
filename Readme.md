# Plane Shooter Game

A 2D side-scrolling plane shooter game developed in Java using JavaFX. Control your plane, defeat enemies and bosses across multiple levels, collect power-ups, and upgrade your plane to achieve victory.

---

## Table of Contents

- [GitHub Repository](#github-repository)
- [Compilation and Dependencies](#compilation-and-dependencies)
- [Implemented Features](#implemented-features)
- [Features with Issues](#features-with-issues)
- [Unimplemented Features](#unimplemented-features)
- [New Java Classes](#new-java-classes)
- [Modified Java Classes](#modified-java-classes)
- [Unexpected Issues and Solutions](#unexpected-issues-and-solutions)

---

## GitHub Repository

The source code for the Plane Shooter Game can be found at:

[https://github.com/fsfuzhu/CW2024](https://github.com/fsfuzhu/CW2024)

---

## Compilation and Dependencies

### Prerequisites

- **Java Development Kit (JDK)**: Version **19.0.2** or higher is required.
- **JavaFX SDK**: Ensure JavaFX is properly set up, as it is not bundled with JDK 19.
- **Development Environment**: IntelliJ IDEA.

### Steps to Compile and Run

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
       --module-path /path/to/javafx-sdk-19/lib --add-modules javafx.controls,javafx.fxml
       ```

     - Replace `/path/to/javafx-sdk-19/lib` with the actual path to your JavaFX SDK `lib` directory.

   - **For Command Line**:
     - Ensure the JavaFX SDK `lib` directory is included in your classpath.
     - Compile and run the game using the following commands:

       ```bash
       javac --module-path /path/to/javafx-sdk-19/lib --add-modules javafx.controls,javafx.fxml -d out $(find ./src -name "*.java")
       java --module-path /path/to/javafx-sdk-19/lib --add-modules javafx.controls,javafx.fxml -cp out com.example.demo.Main
       ```

3. **Run the Game**:

   - **From IDE**:
     - Locate the `Main` class in the `com.example.demo` package.
     - Run the `Main` class.

   - **From Command Line**:
     - Use the commands provided above to compile and run.

---

## Implemented Features

The following features have been correctly implemented:

### 1. Multiple Levels

- **Level One**: Basic enemies spawn, and the player must eliminate 10 enemies to advance.
- **Level Two**: Introduces the first boss with shielding mechanics.
- **Level Three**: Final boss with advanced attack patterns.

### 2. Player Plane Controls

- **Movement**: Player can move up, down, left, and right using default arrow keys.
- **Customizable Controls**: Players can customize key bindings via the `KeyBindings` class.
- **Automatic Shooting**: The player's plane fires bullets automatically at a set fire rate.

### 3. Enemies and Bosses

- **Enemy Planes**: Standard enemies with basic behaviors.
- **Bosses**: Bosses have unique behaviors, health bars, and special attacks.

### 4. Power-Ups and Upgrades

- **Weapon Power-Ups**: Collectibles that increase fire rate and add extra bullet rows.
- **Health Items**: Collectible hearts that restore player's health.
- **Plane Upgrades**: Visual changes to the player's plane upon collecting power-ups.

### 5. Sound and Animation

- **Sound Effects**: Includes shooting sounds, explosion sounds, and background music for each level.
- **Animations**: Explosion animations for enemies and bosses, level-up effects for the player.

### 6. Collision Detection

- **CollisionComponent**: Handles detection between player, enemies, bullets, and power-ups.

### 7. Object Pooling

- **Efficient Resource Management**: Uses object pools for projectiles to optimize performance.

---

## Features with Issues

The following feature has been implemented but encountered issues:

### Boss Explosion Animation in Level Two

- **Issue**: Upon defeating the boss in Level Two, the explosion animation does not play completely; it stops midway.
- **Effect**: The game proceeds to Level Three after the boss defeat sound finishes, even though the animation hasn't completed.
- **Current Status**: Adjustments were made to ensure the boss is not removed from the scene until the explosion animation finishes. However, the issue persists where the animation stops prematurely.

---

## Unimplemented Features

All required features have been implemented. No features remain unimplemented at this time.

---

## New Java Classes

The following Java classes were added to enhance functionality:

### 1. `AnimationComponent`

- **Function**: Manages animations such as explosions and level-up effects.
- **Details**:
  - Plays explosion animations for enemies and bosses.
  - Supports callback functions to execute code after animations finish.

### 2. `SoundComponent`

- **Function**: Manages all sound effects and music in the game.
- **Details**:
  - Plays background music for each level.
  - Plays sound effects for shooting, explosions, boss defeats, power-up pickups, etc.
  - Allows stopping all sounds when needed (e.g., game over).

### 3. `LevelOne`, `LevelTwo`, `LevelThree`

- **Function**: Concrete implementations of levels with specific mechanics.
- **Details**:
  - Each level class defines enemy spawning logic, power-up probabilities, and level-specific behaviors.

### 4. `Boss`, `BossTwo`

- **Function**: Represent boss enemies with unique behaviors.
- **Details**:
  - Have health bars displayed above them.
  - Use special attacks and can summon shields.

### 5. `ActorLevelUp`, `HeartItem`

- **Function**: Represent collectible items for the player.
- **Details**:
  - `ActorLevelUp`: Power-up that upgrades the player's weapon.
  - `HeartItem`: Restores player's health upon collection.

### 6. `KeyBindings`

- **Function**: Allows customization of player control keys.
- **Details**:
  - Stores key bindings for movement controls.
  - Can be extended to include more customizable actions.

---

## Modified Java Classes

The following Java classes were modified, along with reasons for modification:

### 1. `LevelParent`

- **Modifications**:
  - Added logic to handle explosion animations and ensure actors are not removed before animations complete.
  - Implemented methods to manage game state transitions (e.g., `winGame`, `loseGame`).
  - Adjusted collision handling to include new power-up items.

- **Reason**: To support new features like explosion animations, power-ups, and smooth transitions between levels.

### 2. `UserPlane`

- **Modifications**:
  - Integrated `AnimationComponent` to play level-up animations.
  - Adjusted shooting logic to include power-up effects (e.g., extra bullet rows).
  - Updated movement methods to use customizable key bindings.

- **Reason**: To enhance the player's upgrade experience and allow control customization.

### 3. `ShootingComponent`

- **Modifications**:
  - Added support for firing multiple bullet rows.
  - Modified to include automatic shooting without player input.

- **Reason**: To implement the weapon upgrade feature and automatic shooting mechanics.

### 4. `Boss`

- **Modifications**:
  - Implemented health bar display and shielding mechanics.
  - Adjusted destruction logic to handle explosion animations with callbacks.
  - Added `isReadyToRemove` flag to prevent premature removal from the scene.

- **Reason**: To create a more challenging and engaging boss fight with proper visual feedback.

---

## Unexpected Issues and Solutions

### Issue: Boss Explosion Animation Not Completing

- **Problem**: The boss's explosion animation stops midway, and the game proceeds to the next level before the animation finishes.
- **Analysis**:
  - The boss object was being removed from the scene before the animation could complete.
  - The game loop's actor removal logic did not account for ongoing animations.

- **Solution Attempted**:
  - Introduced an `isReadyToRemove` flag in the `Boss` class.
  - Modified the `removeDestroyedActors` method in `LevelParent` to check this flag before removing the boss.
  - Ensured the explosion animation has a callback that sets `isReadyToRemove` to `true` upon completion.

- **Outcome**:
  - Despite these changes, the animation still does not play completely.
  - The issue may be due to other factors such as threading, timing discrepancies, or interference from the game loop.

- **Next Steps**:
  - Investigate if the game loop is affecting the animation timeline.
  - Ensure that the animation is running on the JavaFX Application Thread.
  - Check for any potential conflicts in updating UI elements during the game loop.

---

### Issue: Sound Overlapping and Playback

- **Problem**: Sometimes, sound effects overlap or do not play as expected.
- **Solution**:
  - Modified `SoundComponent` to stop and reset media players before playing sounds.
  - Ensured that media players are managed properly to prevent resource leaks.

---

### Issue: Control Customization Not Reflecting Immediately

- **Problem**: Changes in key bindings do not take effect until the game restarts.
- **Solution**:
  - Implemented methods to refresh input listeners when key bindings are updated.
  - Ensured that the game reads the latest key bindings from the `KeyBindings` class.