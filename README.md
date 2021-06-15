# 2048
CIS 120 final project

## Core Concepts

List the four core concepts, the features they implement, and why each feature is an appropriate use of the concept. Incorporate the feedback you got after submitting your proposal.

  1. 2D Arrays
  		- I used a 2D int array to represent the grid of tiles; this is a good model for the game because the graphical appearance of each tile as well as its behavior when it collides with other tiles rely solely on its value. So the 2D array of tile values can simply be updated in place according to user input (arrow key presses to slide the tiles and mouse clicks on buttons to undo moves, save one game, or import the saved game). This is also preferable to my earlier idea for implementation, which was to represent the grid as a 2D array of Tile objects, each having its own move history. This would have been much more computationally expensive and more complicated to establish logic for. 

  2. Collections
  		- I used two LinkedLists, one containing 2D int arrays to store previous tile arrangements and one containing ints to store previous scores. When the user makes a move, the new tile arrangement and score are computed and the model is updated accordingly, and if the new state is different from the previous state, then the previous tile arrangement and score get added to the head of the appropriate LinkedLists. Whenever the user undoes a move, the game state gets updated to the tile arrangement and score at the heads of the LinkedLists, and those values are then removed from the LinkedLists. The user can undo all their moves up to and including the first one, after which the LinkedLists will be empty and subsequent calls to Undo won't do anything.
  		
  3. File I/O
  		- I used a FileWriter and FileReader to save and import game states: the SavedGameOf2048.txt file gets overwritten to contain the current score and tile arrangement any time the user clicks the Save button (so only one game state can be saved at any given point of time) and the gamestate is set to the last saved gamestate whenever the user clicks the Import button (but the move history is neither saved nor imported). I had originally planned to have a number of tools (e.g. undo, tile cleaner, etc.), each in limited quantities, but since I ended up implementing the overall undo functionality such that the entire move history is recorded in LinkedLists, as opposed to only the most recent move being stored as originally planned, I decided not to implement the other tools.

  4. Testable Component
  		- I used a model-view-controller design for this implementation of 2048, so the model (which is entirely independent of the graphics) is JUnit-testable. I tested for responses of the model to user input (both keyboard and mouse click), mostly with respect to the updating of the game state.

## Your Implementation

Provide an overview of each of the classes in your code, and what their function is in the overall game.
- Game.java
	- This class implements Runnable and contains the main method that runs the game. It first gives a pop-up window (JOptionPane) with instructions. It then creates a window with the Reset, Undo, Save, and Import buttons at the top, the game status (including current score) at the bottom, and the tile grid in the center. It registers mouse clicks and calls on Board2048.java to carry out the functionality.
- Board2048.java
	- This class extends JPanel; it paints all of the tiles to the grid section of the window and handles key presses and the mouse clicks registered by Game.java, calling functions in the model to update the game state appropriately.
- Model.java
	- This class contains the model for the game, which updates the game state after every occurrence of user input, passed on by Board2048.java. Explanations for game logic are given in comments above the relevant functions within this class.
- ModelTest.java
	- This file contains the JUnit tests for Model.java.
- Tile2048.java
	- This class basically decides what each tile looks like; the paintComponent function in Board2048.java contains a nested for loop that iterates through each entry in the 2D array containing the current tile arrangement, creates a Tile2048 object for each one, and paints it at the appropriate coordinates.
- SavedGameOf2048.txt
	- This is the .txt file that game states are saved to and imported from as described earlier.

Were there any significant stumbling blocks while you were implementing your game (related to your design, or otherwise)?
- My Tile2048 class originally implemented JComponent, and the lower half of the paintComponent function in Board2048 (setting colors, drawing rectangles, etc. in the gc) used to be in the paintComponent function of Tile2048. I had stored each created tile in a JPanel that I created within Board2048, to which I applied a grid layout, after which I attempted to paint the tiles by calling paintComponents on the JPanel container, which I thought would just pass the call to the paintComponent function of Tile2048. However, since the same graphics context was being passed into each Tile2048 object upon creation, all of the tiles were being overlaid in the top left corner of the Board2048 object. After being stuck on this for a while, I switched to what I have now, which I think is less modular but works.

Evaluate your design. Is there a good separation of functionality? How well is private state encapsulated? What would you refactor, if given the chance?
- I think that overall there's reasonable separation of functionality, barring the modularity I scrapped because of the difficulty I had in getting my tiles to paint, and the private state should be entirely encapsulated (assuming that the user never gets access to SavedGameOf2048.txt and can't just arbitrarily put in tiles and scores, of course). There was a lot of structural repetition in the main drivers of the game logic (the slideTiles and shift functions) which can probably be consolidated, and there's likely a much neater way to modularize the tile objects than what I have now; these are the things that I'd focus on if I was to refactor.

## External Resources

Cite any external resources (libraries, images, tutorials, etc.) that you may have used while implementing your game.
- N/A
