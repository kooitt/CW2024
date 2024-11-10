#Project Title: Sky Battle Game

**GitHub**
[GitHub Repository Link](https://github.com/YourUsername/YourRepository)

**Compilation Instructions**
To compile and run the application, follow these steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/YourRepository.git

2. Navigate to the project directory:
   cd YourRepository

3. Ensure you have JavaFX set up as it is required for this project. You may need to add JavaFX as a library in your IDE or configure your command line to include JavaFX modules.

4. Run the application from your IDE (e.g., IntelliJ or Eclipse), ensuring that the necessary JavaFX configurations are set up. If running from the command line, use the following:
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar SkyBattle.jar

**Implemented and Working Properly**
1. UserPlane Control: The UserPlane responds to UP and DOWN arrow keys, allowing smooth vertical movement within screen boundaries, providing responsive gameplay.
2. EnemyProjectile Behavior: EnemyProjectiles are fired by enemy planes, moving toward the UserPlane. Projectiles are removed on collision or when off-screen, with accurate collision detection for player damage.
3. EnemyPlane Display: EnemyPlanes appear at intervals, moving towards the UserPlane. They are correctly removed upon destruction, updating the player’s score and maintaining gameplay flow.
4. Health Display: Player's health will only decrease when the UserPlane collides with the EnemyProjectile.

**Implemented but Not Working Properly**
1. Enemy AI: The enemy planes’ behavior might not always respond correctly or may occasionally overlap with the user plane in an unintended way.
2. Projectile Collision Boundaries: In certain cases, projectiles might miss collisions with planes or objects due to boundary detection issues.

**Features Not Implemented**
1. Power-Ups: Originally intended to include power-ups that enhance the user's abilities, but these were not implemented due to time constraints.
2. Multiplayer Mode: A feature to enable multiplayer gameplay was considered but not developed.

**New Java Class**
1. UserProjectile: This class represents projectiles fired by the user plane, defining its behavior and appearance.
2. LevelView: Manages the display of elements like health, providing a visual interface for game progress.

**Modified Java Classes**
1. LevelParent: Modified to include methods for starting the game, switching levels, and handling game win/loss scenarios.
2. Controller: Updated to handle user input, allowing the UserPlane to move up and down in response to arrow key presses.
3. UserPlane: Adjusted to respond to movement commands and updated its collision detection logic.
4. EnemyProjectile: Modified to adjust movement speed and set collision behavior upon contact with the UserPlane.

**Unexpected Problems**
1. Input Handling for UserPlane Movement: Initially, the UserPlane was not responding to arrow key inputs. The issue was resolved by setting up key listeners in Controller.java.
2. Collision Detection Precision: Challenges were encountered with precise collision detection for projectiles. Adjustments were made, but further refinement may be necessary to improve accuracy.
3. JavaFX Compatibility: Configuring JavaFX with the project setup required additional configuration, especially in terms of module path setup.