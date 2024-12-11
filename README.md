In LevelParents, added user.destroy() to goToNextLevel function
without user.destroy(), every asset from the previous level (bullets etc) was kept
In shieldimage class, changed shield.jpg to shield.png


#Problems
-invocation target exception when firing bullets ##
-enemy plane and projectile hitbox too big ##
-takes too much memory to run ##
-In LevelOne class the program kept checking if the game was over line 23 ##
-boss shield does not show when activated 
-game over message needs to be adjusted to fit the screen ## (shield still does not show)
-boss has too much health ##
-needs a pause menu and a main menu


cropped the images and changed their sizes to better fit the hitbox



-plane not registering x axis input (added the code to register x and y input in levelparent line 113-114)
(added the vector logic of horizontal movement)


-moving x upper and lower bounds makes the plane move diagonally 

-projectile does not follow plane but instead is fixed at position 110 on the x axis (the x and y coordinates on the projectile are now 
taken from the coordinates of the plane + an offset to match the plane nose)
