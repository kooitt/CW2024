#Project Title: Sky Battle Game (rmb to put table content here)


**GitHub**
[GitHub Repository Link](https://github.com/YourUsername/YourRepository)


**Compilation Instructions**
To compile and run the application, follow these steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/YourRepository.git

2. Navigate to the project directory:
   cd YourRepository

3. Add dependencies:
   Ensure you have JavaFX set up as it is required for this project.
   If using IntelliJ, go to File > Project Structure > Libraries and add the path to JavaFX SDK.

4. Run the application from your IDE (e.g., IntelliJ or Eclipse), ensuring that the necessary JavaFX configurations are set up. If running from the command line, use the following:
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar SkyBattle.jar


**Implemented and Working Properly**
##UserPlane:
1. UserPlane Element: UserPlane element is display when player runs the game.
2. UserPlane Control: The UserPlane responds to **UP AND DOWN arrow keys**, allowing smooth **vertical movement** within screen boundaries, providing reponsive gameplay.
3. UserPlane Projectile Firing: When the UserPlane fires a projectile by **pressing spacebar in keyboard**, it can **hit and destroy EnemyPlane** instances. Upon impact, the **EnemyPlane disappears**, simulating a successful hit and providing **visual feedback** to the player.

##Enemy:
1. EnemyPlane Display: EnemyPlanes appear at intervals when player runs the game, **moving towards the UserPlane**. They are correctly removed upon destruction, updating the player’s score and maintaining gameplay flow.
2. EnemyProjectile Behavior: EnemyProjectiles are **fired by enemy planes**, moving toward the UserPlane. Projectiles are **removed** on collision or when off-screen, with accurate collision detection for player damage.

##UserPlane and enemy interactions:
1. Enemy Interaction: Projectile **fired by UserPlane** correctly **distroyed EnemyPlane**. Then, **EnemyPlane disappears**.
2. Health Display: Health level **decrease** when EnemyPlane and EnemyProjectile **collides with the Userplane**.

##Level Transition:
1. Players successfully transition from Level One to Level Two upon achieving the required kill count.
2. Boss Behavior in Level Two: The boss in Level Two has unique movement patterns and a shield activation mechanism.


**Implemented but Not Working Properly**
1. Projectile Collision Detection
   Issue: The collision detection between UserPlane projectiles and EnemyPlane instances, as well as between Boss projectiles and the UserPlane, sometimes fails to register correctly, causing some projectiles to pass through enemies or the player without triggering a collision.
   Steps Taken: Reviewed the bounding box properties and collision detection logic to ensure proper hit registration. Adjusted the positioning and size parameters of projectiles and enemies to improve accuracy. Also attempted to debug with logs to identify missed collision events.
   Current Status: Although improved, occasional missed collisions still occur. Further investigation into the timing of updates and JavaFX’s rendering cycle might be needed to fully resolve this issue.

2. Boss Shield Activation Timing
   Issue: The Boss shield sometimes activates or deactivates at unintended intervals, causing inconsistent behavior in gameplay. The shield may stay active longer than intended or fail to activate in time, making the boss either too difficult or too easy to defeat.
   Steps Taken: Modified the random shield activation probability and adjusted timing logic for shield duration. Added logs to monitor activation frequency and ensure it aligns with game balance goals.
   Current Status: The shield timing is now more consistent, but minor inconsistencies remain. Further fine-tuning of the shield probability and frame count settings may be necessary for optimal performance.

**Features Not Implemented**
1. Power-Ups: Originally intended to include power-ups that enhance the user's abilities, but these were not implemented due to time constraints.
2. Multiplayer Mode: A feature to enable multiplayer gameplay was considered but not developed.
3. Advanced AI for Boss: Planned to implement advanced AI that adapts based on the player's movement, but this feature was left out due to time constraints.


**New Java Class**
1. UserProjectile
   Purpose: Represents the projectiles fired by the UserPlane. This class defines the behavior, movement, and appearance of the user's projectiles. The UserProjectile moves horizontally across the screen and can collide with enemy planes, destroying them upon impact.
   Location: com.example.demo

2. LevelView
   Purpose: Manages the display of UI elements such as health indicators, win/lose screens, and other visual components. LevelView provides a visual interface for game progress, updating the player on their health status and displaying end-game messages.
   Location: com.example.demo

3. BossProjectile
   Purpose: Represents the projectiles fired by the boss in Level Two. It moves horizontally towards the UserPlane and reduces the player's health upon collision.
   Location: com.example.demo

4. LevelViewLevelTwo
   Purpose: Extends LevelView to add unique UI elements for Level Two, such as the boss's shield display or additional indicators specific to the boss battle.
   Location: com.example.demo

**Modified Java Classes**
1. Controller:
   Modifications: 
   - Updated to handle user input, specifically to allow the UserPlane to move up and down based on arrow key presses.
   - Included event listeners to respond to keyboard input dynamically.
   Reason for Modifications:
   - These changes were made to give the player control over the UserPlane, allowing it to dodge enemy projectiles and navigate through the game environment. The modifications improve gameplay interactivity and responsiveness.

2. UserPlane
   Modifications:
   - Enhanced to respond to movement commands from the Controller class, enabling vertical movement within the game window.
   - Updated collision detection logic to interact with enemy projectiles and enemy planes more accurately.
   Reason for Modifications: 
   - The adjustments were essential for implementing user control and ensuring accurate health deduction upon collisions with enemy elements. This improves the game's realism and the player's ability to engage in combat.

3. EnemyProjectile
   Modifications:
   - Adjusted movement speed to provide a balanced challenge for the player.
   - Updated collision behavior to reduce the UserPlane's health upon contact and destroy the projectile upon impact.
   Reason for Modifications: 
   - These changes were made to make enemy projectiles a more significant threat, requiring the player to avoid them to maintain health. By refining the collision handling, the game offers a better challenge and encourages players to strategically maneuver the UserPlane.

4. LevelParent
   Modifications:
   - Updated to handle background initialization using getClass().getResource() with Objects.requireNonNull to prevent potential NullPointerException when loading resources.
   - Added collision handling between user projectiles and enemy planes, as well as between enemy projectiles and the player (UserPlane).
   - Implemented methods for transitioning to the next level and managing game-over and win states.
   Reason for Modifications: 
   - These changes ensure that resource loading is error-free and that essential game mechanics (like collisions, level transitions, and win/loss handling) operate seamlessly, providing a stable gameplay experience.

5. LevelOne.java
   Modifications:
   - Adjusted the spawning logic for enemy units to include a probability-based spawning mechanism, controlling the frequency and number of enemies on the screen.
   - Integrated collision handling so that UserPlane projectiles can destroy EnemyPlane instances, enabling a functional shooting mechanic.
   Reason for Modifications: 
   - These changes enhance the primary gameplay experience by adding a dynamic element to enemy spawning and enabling players to engage with enemies by shooting them.

6. LevelTwo.java
   Modifications: 
   - Added specific behavior for the Boss in LevelTwo, including unique movement patterns, shield activation, and interactions with the player.
   - Configured the level to spawn the Boss only once, making it a unique challenge for this stage, and set up win conditions based on the destruction of the Boss.
   - Set up LevelViewLevelTwo to display UI elements specific to this level, such as boss health.
   Reason for Modifications:
   - These modifications introduce a more challenging boss fight, adding variety and complexity to gameplay in Level Two, which differentiates it from Level One.

7. Boss.java
   Modifications:
   - Enhanced the boss's movement patterns by incorporating randomized vertical movements, making the boss less predictable and harder to hit.
   - Added a shield activation mechanism that randomly activates and deactivates, requiring players to strategize around the shield timing.
   - Introduced the fireProjectile() method, allowing the boss to launch BossProjectile instances at intervals, increasing the difficulty by having the boss actively attack the player.
    Reason for Modifications: 
   - These adjustments make the boss more challenging by adding movement unpredictability, a defensive shield, and offensive capabilities, thereby creating an engaging and intense final challenge for players.


**Unexpected Problems**
1. Inconsistent Collision Detection in JavaFX
   Issue: Projectiles occasionally missed collisions due to JavaFX timing issues.
   Fix: Refined bounding boxes and added logging to track missed collisions; some inconsistencies remain.

2. UserPlane Safe Area Visualization
   Issue: Players needed visual cues for the safe area.
   Fix: Added a virtual boundary around the UserPlane to indicate a safe zone, under review for user experience improvement.