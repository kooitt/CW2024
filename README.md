COMP2042 Coursework

Github repository link:https://github.com/slowying/CW2024/tree/master

Open the project in IntelliJ: Open the project using IntelliJ.
1.Go to File > Project Structure and set the SDK to Java 21 or higher and the language level to 11 or higher.
2.Install and configure the JavaFX library.
3.Find the Main class in the src folder and run the main method.
4.Build the project using Maven and start the application.

Fixed Bugs:
1.Fixed progression issues to level 2: Changed the shield image format from .jpg to .png and added timeline.stop() when the level is completed.
2.Ensured the shield follows the boss in level 2: Tracked the boss's x and y positions using getLayoutX() + getTranslateX() and getLayoutY() + getTranslateY() and set the shield's x and y positions to the boss's coordinates using setLayoutX() and setLayoutY().

Features Implemented:
1.User plane movement: Enabled movement of the user plane both horizontally and vertically.
2.Kill count display: Displayed the number of kills in level 1 and level 3.
3.Boss health bar: Displayed the boss's health with a progress bar and text.


Main menu: Displayed the main menu with options to start the game and exit the game.
Loading page: Inserted a loading page between levels to hide latency during level transitions.
New levels: Level 3: The user defeats 15 new enemies and wins, with no restrictions other than lives.
New ememyplane: similar to the old ememyplane .
