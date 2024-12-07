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
- **Development Environment**: IntelliJ IDEA, Eclipse, or other Java IDEs.

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
       java --module-path /path/to/javafx-sdk-19/lib --add-modules javafx.controls,javafx.fxml -cp out com.example.demo.controller.Main
       ```

3. **Run the Game**:

   - **From IDE**:
     - Locate the `Main` class in the `com.example.demo.controller` package.
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

### 1. `AnimationComponent` (in `com.example.demo.components` package)

- **Function**: Manages animations such as explosions and level-up effects.
- **Details**:
  - Plays explosion animations for enemies and bosses.
  - Supports callback functions to execute code after animations finish.
  - Uses `Timeline` and `KeyFrame` for frame-based animations.

### 2. `SoundComponent` (in `com.example.demo.components` package)

- **Function**: Manages all sound effects and music in the game.
- **Details**:
  - Plays background music for each level.
  - Plays sound effects for shooting, explosions, boss defeats, power-up pickups, etc.
  - Provides methods to stop and reset sounds as needed.

### 3. `KeyBindings` (in `com.example.demo.utils` package)

- **Function**: Allows customization of player control keys.
- **Details**:
  - Stores key bindings for movement controls (up, down, left, right).
  - Can be extended to include more customizable actions.
  - Singleton pattern ensures consistent key bindings throughout the game.

### 4. `ShootingComponent` (in `com.example.demo.components` package)

- **Function**: Manages shooting behavior for actors, including automatic firing.
- **Details**:
  - Handles firing rate, bullet spawning, and bullet pooling.
  - Supports upgrades such as increased fire rate and extra bullet rows.
  - Integrates with `SoundComponent` to play shooting sounds.

### 5. `ActorLevelUp` and `HeartItem` (in `com.example.demo.actors` package)

- **Function**: Represent collectible items for the player.
- **Details**:
  - `ActorLevelUp`: Power-up that upgrades the player's weapon.
  - `HeartItem`: Restores player's health upon collection.
  - Both move across the screen and can be collected by the player.

### 6. `Shield` (in `com.example.demo.actors` package)

- **Function**: Represents a shield that the boss can summon.
- **Details**:
  - Protects the boss from damage while active.
  - Can be destroyed by the player's attacks.

### 7. `ObjectPool<T>` (in `com.example.demo.utils` package)

- **Function**: Generic object pool for managing reusable objects.
- **Details**:
  - Used to manage projectiles efficiently.
  - Reduces overhead by reusing objects instead of creating new ones.

### 8. `BulletFactory` (in `com.example.demo.projectiles` package)

- **Function**: Factory class for creating bullets/projectiles.
- **Details**:
  - Creates different types of projectiles based on owner (user, enemy, boss).
  - Integrates with `ObjectPool` to manage projectile instances.

### 9. `CollisionComponent` (in `com.example.demo.components` package)

- **Function**: Handles collision detection between game objects.
- **Details**:
  - Defines hitboxes for actors.
  - Provides methods to check for collisions with other actors.

---

## Modified Java Classes

The following Java classes were modified, along with reasons for modification:

### 1. `LevelParent` (in `com.example.demo.levels` package)

- **Modifications**:
  - **Game Loop Enhancements**: Updated the game loop to support delta time for smoother animations.
  - **Actor Management**:
    - Implemented methods to manage adding and removing actors from the game scene.
    - Modified `removeAllDestroyedActors` method to account for ongoing animations (e.g., boss explosion).
  - **Collision Handling**:
    - Integrated `CollisionComponent` for collision detection.
    - Added handling for new power-up items (`ActorLevelUp`, `HeartItem`).
  - **Input Processing**:
    - Updated to use customizable key bindings from the `KeyBindings` class.
    - Implemented `processInput` method to handle player input based on active keys.
  - **Sound and Animation Integration**:
    - Integrated `SoundComponent` and `AnimationComponent` for managing sounds and animations.
  - **Game State Management**:
    - Implemented methods `winGame` and `loseGame` to handle game state transitions.
    - Added `isInputEnabled` flag to manage player input during transitions.

- **Reason**: To support new features like custom controls, power-ups, advanced collision detection, and enhanced game state management.

### 2. `UserPlane` (in `com.example.demo.actors` package)

- **Modifications**:
  - **Upgrades and Power-Ups**:
    - Implemented methods to handle weapon upgrades and plane enhancements.
    - Plane's appearance changes with upgrades.
  - **Shooting Behavior**:
    - Integrated `ShootingComponent` for automatic firing and managing fire rate.
    - Supports extra bullet rows as an upgrade.
  - **Movement Controls**:
    - Updated movement methods to use customizable key bindings.
    - Added methods to handle horizontal movement (left and right).
  - **Animations**:
    - Integrated `AnimationComponent` to play level-up animations when collecting power-ups.
  - **Health Management**:
    - Adjusted to use a health component for better health management.

- **Reason**: To enhance the player's experience with upgrades, automatic shooting, custom controls, and visual feedback.

### 3. `Boss` (in `com.example.demo.actors` package)

- **Modifications**:
  - **Health Bar**:
    - Added a health bar display above the boss.
  - **Shield Mechanics**:
    - Implemented the ability for the boss to summon a shield (`Shield` class).
  - **Shooting Behavior**:
    - Integrated `ShootingComponent` for firing projectiles.
  - **Explosion Animation**:
    - Adjusted `destroy` method to play explosion animation using `AnimationComponent`.
    - Added `isReadyToRemove` flag to prevent boss removal before animation completes.
  - **Movement Pattern**:
    - Enhanced movement with a move pattern and vertical velocity adjustments.
    - Implemented random movement patterns to make the boss's actions less predictable.

- **Reason**: To create a more challenging and engaging boss fight with proper visual feedback and unique behaviors.

### 4. `LevelTwo` (in `com.example.demo.levels` package)

- **Modifications**:
  - **Boss Integration**:
    - Adjusted to integrate the modified `Boss` class with new mechanics.
  - **Power-Up Spawning**:
    - Added logic to spawn power-ups (`ActorLevelUp`, `HeartItem`) during the level.
  - **Game State Handling**:
    - Managed game state transitions to Level Three upon boss defeat.
    - Ensured explosion animations and sounds are completed before proceeding.
    - Added flags to synchronize the end of sounds and animations.

- **Reason**: To accommodate the enhanced boss mechanics and provide a smooth transition between levels.

### 5. `ShootingComponent` (in `com.example.demo.components` package)

- **Modifications**:
  - **Automatic Firing**:
    - Handles the timing and firing of projectiles automatically.
  - **Upgrades Support**:
    - Supports increased fire rate and extra bullet rows as upgrades.
  - **Integration with Sound**:
    - Plays shooting sound when the player fires.
  - **Bullet Pooling**:
    - Utilizes `ObjectPool` for efficient projectile management.

- **Reason**: To centralize and manage the shooting behavior for both the player and enemies, and to support weapon upgrades.

### 6. `Projectile` and Derived Classes (in `com.example.demo.projectiles` package)

- **Modifications**:
  - **Pooling Integration**:
    - Adjusted to work with `ObjectPool` for efficient resource management.
  - **Collision Component**:
    - Added `CollisionComponent` for collision detection.
  - **Reset Methods**:
    - Implemented methods to reset position and state when reused from the pool.

- **Reason**: To improve performance and integrate with the new collision detection system.

### 7. `ActiveActor` (in `com.example.demo.actors` package)

- **Modifications**:
  - **Health Management**:
    - Added `HealthComponent` to manage health and destruction.
  - **Collision Detection**:
    - Integrated `CollisionComponent` to handle collisions.
  - **Movement and Updates**:
    - Standardized the `updateActor` method to include delta time.
    - Separated movement logic into `MovementComponent`.

- **Reason**: To unify the actor hierarchy and support new features like health management and collision detection.

### 8. `Main` and `Controller` Classes (in `com.example.demo.controller` package)

- **Modifications**:
  - **Game Initialization**:
    - Adjusted to accommodate the new level structure and game state management.
  - **Event Handling**:
    - Updated to work with the new input processing system.
    - Ensured proper handling of level transitions and observer patterns.

- **Reason**: To ensure proper game startup and level transitions with the updated game architecture.

---

## Unexpected Issues and Solutions

### Issue: Main Menu Interface Offset (Scaling Issue on Windows with 125% DPI)

- **Problem**: When returning to the main menu after playing a level, the main menu interface would sometimes appear offset or misaligned on systems with Windows scaling set to 125%. Initially, the first time loading the main menu had no issues, but subsequent returns caused the interface to shift. Also, attempts to reuse the same root node for the main menu scene triggered exceptions like `is already set as root of another scene`.

- **Analysis**:
  - The root node of the main menu scene was being reused across multiple scene transitions, which caused layout and scaling inconsistencies.
  - JavaFX does not allow the same node to be the root of multiple scenes. Reusing the same `StackPane` or root layout node caused `IllegalArgumentException` when setting it to a new scene.
  - Even after trying to recreate scenes dynamically, the offset issue persisted if the same main menu root node was reused.

- **Solution**:
  - To solve both the offset and node reuse issues, we unified the main menu display logic with the levels' approach.
  - Instead of using a fixed `MainMenu` root node and toggling visibility, we introduced a `MainMenuParent` class similar to a `LevelParent`-like structure.
  - Each time we return to the main menu, we create a new `MainMenuParent` instance and get a fresh `Scene`. This ensures a clean layout pass and avoids node reuse conflicts.
  
- **Implementation Details**:
  - Created a `MainMenuParent` class (similar to how levels are structured) that handles its own `Scene`.
  - In `Controller.showMainMenu()`, we now instantiate a new `MainMenuParent` each time and set its scene to the stage. This approach mirrors the process of loading a new level scene, ensuring consistency and preventing layout shifts.
  - The `SettingsPage` is still integrated, but added and removed from the new main menu root each time as needed. This ensures no conflict in root node usage.

- **Outcome**:
  - After implementing `MainMenuParent` and creating a new scene for the main menu each time, the offset and scaling issues no longer occur.
  - The `is already set as root of another scene` exception is resolved since each main menu display uses a brand-new root node and scene.
  - This unified approach to scene management for both the main menu and levels led to a consistent and stable user interface across DPI scaling settings.

---

### Issue: Sound Overlapping and Playback

- **Problem**: Sometimes, sound effects overlap or do not play as expected.
- **Solution**:
  - Modified `SoundComponent` to stop and reset media players before playing sounds.
  - Ensured that media players are managed properly to prevent resource leaks.
  - Used callbacks to synchronize sound playback with game events.

---

### Issue: Control Customization Not Reflecting Immediately

- **Problem**: Changes in key bindings do not take effect until the game restarts.
- **Solution**:
  - Implemented methods to refresh input listeners when key bindings are updated.
  - Ensured that the game reads the latest key bindings from the `KeyBindings` class.
  - Added logic to update active keys during the game loop.

---

If there are any further issues or if more detailed explanations are needed for any section, please feel free to reach out.

---

# License

This project is a coursework assignment for our institution. **Copying or plagiarizing any part of this project is strictly prohibited.**

- You are welcome to **view the code** to learn and understand how the game is implemented.
- Feel free to **download and play** the game for personal enjoyment.
- **Unauthorized distribution** of the code or claiming it as your own work is not allowed.

---

> **Contact**: For any questions or feedback, please open an issue or reach out to the maintainers.

---