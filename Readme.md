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
   git checkout 20241208
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

- **Level One**: Basic enemies spawn, and the player must eliminate enemies to advance.
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
- **Animations**: Explosion animations for enemies and bosses, level-up effects for the player that follow the player's movement.

### 6. Collision Detection

- **CollisionComponent**: Handles detection between player, enemies, bullets, and power-ups.

### 7. Object Pooling

- **Efficient Resource Management**: Uses object pools for projectiles to optimize performance.

---

## Features with Issues

The following features have been implemented but encountered issues:

### 1. Boss Explosion Animation in Level Two

- **Issue**: Upon defeating the boss in Level Two, the explosion animation does not play completely; it stops midway.
- **Effect**: The game proceeds to Level Three after the boss defeat sound finishes, even though the animation hasn't completed.
- **Current Status**: Adjustments were made to ensure the boss is not removed from the scene until the explosion animation finishes. However, the issue persists where the animation stops prematurely.

### 2. Shield Refreshing After Game Over

- **Issue**: After the player dies and the `GameOver` screen is displayed, the boss in Level Two continues to refresh and display shields.
- **Effect**: Unwanted shield animations and potential resource leaks after the game has ended.
- **Solution**: Modified `LevelTwo` to stop the shield refreshing `Timeline` when the player dies, ensuring that no further shield updates occur post `GameOver`.

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
  - **New Addition**: `playLevelUpRelative` method to allow level-up animations to follow the player's position.

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

### 8. `BulletFactory` (in `com.example.demo.actors.Projectile` package)

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
    - **New Change**: Level-up animations now follow the player's movement by being added as child nodes to the player's `Group`.
  - **Health Management**:
    - Adjusted to use a health component for better health management.

- **Reason**: To enhance the player's experience with upgrades, automatic shooting, custom controls, and visual feedback.

### 3. `Boss` (in `com.example.demo.actors` package)

- **Modifications**:
  - **Health Bar**:
    - Added a health bar display above the boss.
  - **Shield Mechanics**:
    - Implemented the ability for the boss to summon a shield (`Shield` class).
    - **New Change**: Added `stopShieldTimeline` method to allow external control of the shield refreshing timeline.
  - **Shooting Behavior**:
    - Integrated `ShootingComponent` for firing projectiles.
  - **Explosion Animation**:
    - Adjusted `destroy` method to play explosion animation using `AnimationComponent`.
    - Added `isReadyToRemove` flag to prevent boss removal before animation completes.
  - **Movement Pattern**:
    - Enhanced movement with a move pattern and vertical velocity adjustments.
    - Implemented random movement patterns to make the boss's actions less predictable.

- **Reason**: To create a more challenging and engaging boss fight with proper visual feedback and unique behaviors, and to fix the bug where shields continued to refresh after game over.

### 4. `LevelTwo` (in `com.example.demo.levels` package)

- **Modifications**:
  - **Boss Integration**:
    - Adjusted to integrate the modified `Boss` class with new mechanics.
    - Implemented `checkIfGameOver` to stop the boss's shield refreshing timeline upon player death.
  - **Power-Up Spawning**:
    - Added logic to spawn power-ups (`ActorLevelUp`, `HeartItem`) during the level.
  - **Game State Handling**:
    - Managed game state transitions to Level Three upon boss defeat.
    - Ensured explosion animations and sounds are completed before proceeding.
    - Added flags to synchronize the end of sounds and animations.
  - **Bug Fix**:
    - **New Addition**: In `checkIfGameOver`, added logic to stop the boss's shield refreshing timeline when the player dies to prevent shields from appearing post `GameOver`.

- **Reason**: To accommodate the enhanced boss mechanics, provide a smooth transition between levels, and fix the shield refreshing bug after `GameOver`.

### 5. `AnimationComponent` (in `com.example.demo.components` package)

- **Modifications**:
  - **New Method**: Added `playLevelUpRelative` to allow level-up animations to follow the player's position by adding them as child nodes to the player's `Group`.
  - **Enhanced Functionality**:
    - Ensured animations can be attached to any parent node, facilitating dynamic positioning.
    - Modified existing animation methods to support relative positioning.

- **Reason**: To enable level-up animations to follow the player's movement, enhancing visual feedback and preventing animations from remaining static when the player moves.

### 6. `CollisionComponent` (in `com.example.demo.components` package)

- **Modifications**:
  - **Enhancements**:
    - Improved collision checking logic to account for beneficial objects like `ActorLevelUp` and `HeartItem`.
    - Ensured collision visualizations are accurately updated based on the actor's position.
  
- **Reason**: To support accurate and efficient collision detection with new power-up items and maintain game integrity.

### 7. `Controller` (in `com.example.demo.controller` package)

- **Modifications**:
  - **Main Menu Management**:
    - Ensured that upon transitioning to `GameOver`, all timelines and animations are properly stopped.
    - Added logic to handle returning to the main menu cleanly without residual animations or timelines.

- **Reason**: To manage game state transitions effectively and prevent unwanted behaviors post `GameOver`.

### 8. `LevelOne` (in `com.example.demo.levels` package)

- **Modifications**:
  - **Dynamic Enemy Waves**:
    - Enemies now spawn in waves of 3 (`ENEMIES_PER_SPAWN`).
    - New waves are generated only when all enemies from the previous wave are destroyed.
    - Ensured synchronization of enemy spawning to avoid premature wave creation.

  - **Total Enemy Count**:
    - Introduced `TOTAL_ENEMIES` to define the total number of enemies for the level.
    - Removed the previous `KILLS_TO_ADVANCE` parameter to simplify level progression logic.

  - **Enemy Spawn Probabilities**:
    - Adjusted the spawn probabilities for different enemy types:
      - **EnemyPlaneOne**: Higher probability of spawning.
      - **EnemyPlaneTwo**: Lower probability of spawning.

  - **Level Completion Logic**:
    - Level transitions to `LevelTwo` only after all enemies (`TOTAL_ENEMIES`) have been eliminated, and no enemies remain on the screen.
    - Added `checkIfGameOver` logic to handle seamless transition once all conditions are met.

  - **Power-Up Spawning**:
    - Integrated existing power-up logic without changes to probabilities.

- **Reason**: To provide a more structured and intuitive enemy spawning system, ensuring controlled gameplay progression while maintaining balance and challenge.


---

## Unexpected Issues and Solutions

### Issue: Shield Refreshing After Game Over

- **Problem**: After the player dies and the `GameOver` screen is displayed, the boss in Level Two continues to refresh and display shields.
- **Solution**:
  - **Implemented**: Added a method `stopShieldTimeline()` in the `Boss` class to allow external control over the shield refreshing `Timeline`.
  - **Modified**: Updated `LevelTwo`'s `checkIfGameOver` method to call `boss.stopShieldTimeline()` when the player dies.
  - **Outcome**: Ensures that once the player is dead and `GameOver` is triggered, the boss no longer refreshes or displays shields, preventing unwanted animations and resource usage post-game termination.

### Issue: LevelUp Animation Not Following Player

- **Problem**: The LevelUp animation remained static on the screen when the player moved, breaking immersion and visual consistency.
- **Solution**:
  - **Implemented**: Modified the `AnimationComponent` to include the `playLevelUpRelative` method, which attaches the LevelUp animation as a child node to the player's `Group`.
  - **Modified**: Updated the `UserPlane` class to use the new `playLevelUpRelative` method when a power-up is collected, ensuring the animation follows the player's movement.
  - **Outcome**: LevelUp animations now dynamically follow the player's position, maintaining visual consistency even as the player moves across the screen.

### Issue: Main Menu Interface Offset (Scaling Issue on Windows with 125% DPI)

- **Problem**: When returning to the main menu after playing a level, the main menu interface would sometimes appear offset or misaligned on systems with Windows scaling set to 125%. Initially, the first time loading the main menu had no issues, but subsequent returns caused the interface to shift. Also, attempts to reuse the same root node for the main menu scene triggered exceptions like `is already set as root of another scene`.
- **Solution**:
  - **Implemented**: Created a new `MainMenuParent` class similar to `LevelParent`, ensuring each main menu display uses a fresh root node and scene.
  - **Modified**: Updated the `Controller.showMainMenu()` method to instantiate a new `MainMenuParent` each time the main menu is displayed.
  - **Outcome**: Resolved the offset and scaling issues by avoiding root node reuse and ensuring consistent scene initialization, leading to a stable and properly aligned main menu interface across different DPI settings.

### Issue: Sound Overlapping and Playback

- **Problem**: Sometimes, sound effects overlap or do not play as expected.
- **Solution**:
  - **Implemented**: Modified `SoundComponent` to stop and reset media players before playing sounds.
  - **Modified**: Ensured that media players are managed properly to prevent resource leaks.
  - **Outcome**: Sound playback is now more reliable, with reduced overlapping and improved synchronization with game events.

### Issue: Control Customization Not Reflecting Immediately

- **Problem**: Changes in key bindings do not take effect until the game restarts.
- **Solution**:
  - **Implemented**: Added methods to refresh input listeners when key bindings are updated.
  - **Modified**: Ensured that the game reads the latest key bindings from the `KeyBindings` class during the game loop.
  - **Outcome**: Key binding changes now take effect immediately without needing to restart the game, enhancing user experience and control flexibility.

---

# License

This project is a coursework assignment for our institution. **Copying or plagiarizing any part of this project is strictly prohibited.**

- You are welcome to **view the code** to learn and understand how the game is implemented.
- Feel free to **download and play** the game for personal enjoyment.
- **Unauthorized distribution** of the code or claiming it as your own work is not allowed.

---