Overview
The Pawns Board Game is a strategic two player game where players use cards to gain control of a
board. Players can place cards in cells where they have pawns, and each card has an influence grid
that affects nearby cells. The game determines a winner based on the scores on each row.
The game is designed to support various board sizes and different sets of cards.

The code includes
- a model that represents the game logic, including the board, players, cards, and decks.
- a deck reader to generate cards from a configuration file
= a textual view to render the board state in textual form
- a main method to run a sample game

Some assumptions about the game is that the board is rectangular with an odd number of columns.
Players use separate decks, with the blue deck influence grid reversed when compared to the red deck
The game starts with three pawns per player spread out across the first and last column.
Players can only place cards in cells that they have pawns in. The number of pawns have to cover
the cost of the card, and the player has to own the pawns.

Key components:
Model:
PawnsBoardModelImpl: the main game logic implementation, manages turns, board updates, game state

DeckReader:
reades the deck configuration file and creates two separate decks
- red deck uses the influence grid in the file
- blue deck reverses the influence grid

View:
provides a textual visualization of the board


Key subcomponents:
Board: represents the grid where players place cards
Cell: a single square on the board, containing pawns or card
Player: represents a game participant with a hand and a deck
Card: represents a card with an influence grid, value, and cost
InfluenceGrid: a 5x5 grid that determines how a card influences the board when placed


Layout of the code

docs
- deck.config: Configuration file for the cards and decks
src
- model
    - ICard: interface for card
    - AbstractCard: abstract class for card
    - Card: implementation of card

    - IBoard: iterface for board
    - Board: implementation of baord

    - ICell: interface for cell
    - Cell: implementation of cell

    - IDeck: interface for deck
    - AbstractDeck: abstract class for deck
    - Deck: implementation of deck

    - ICard: interface for card
    - AbstractCard: abstract class for deck
    - Card: implementation of card

    - IInfluenceGrid: interface for influence grid
    - InfluenceGrid: implementation for influence grid

    - IPlayer: interface for player
    - AbstractPlayer: abstract class for player
    - Player: implementation of player

    - GameState: enum class for various game states

    - IPawnsBoardModel: interface for the game logic model
    - PawnsBoardModelImpl: implementation of game logic model

    - DeckReader: reader for deck configuration file

- view
    - ITextualView: interface for textual view
    - TextualPawnsBoardView: implementation of textual view

test
- model
    - PawnsBoardModelTest: test class for all classes and methods in the model package
- view
    - PawnsBoardViewTest: test class for all classes and methods in the view package


Future improvements
- implement AI players
- add a grpahical user interface
- extend to different board sizes and custom decks

User-Player interface design for Pawns Board Game
methods:
- startGame: initialize the game and sets up the board players and decks
- renderGameState: render the current board state
- getUserAction: asks the user for inputs (place card or pass turn)
- execute: executes the given move
- isValidMove(): checks if a move is legal according to the game logic
- isGameOver(): determine if the game is over and announces winner if it is

the interface basically needs to receive user input and execute the input onto the board, and
display the results.




CHANGES FOR PART 2

A new ReadOnlyPawnsBoardModel Impl was implemented to support the graphical view system.
This extends AbstractPawnsBoardModel
implements ReadOnlyPawnsBoardModel
it is used by the GUI to observe game state without mutation with game logic


The ReadOnlyPawnsBoardModel is a base interface with purely observational methods.
This includes:
- getCardAt: returns the card at the specified baord coordinate
- getPlayerHand: returns the current hand of the given player
- scoreAtRow: Returns the score for the specified player in the given row
- currentScore: Returns the current total score of the given player
- getGameState: Gets the current state of the game
- getBoard: Returns the game board
- isRedTurn(): Returns whether it is red player's turn
- isGameOver(): Returns whether the game is over
- determineWinner(): determines and returns the winner of the game
- isMoveLegal(): Checks whether placing the given card at the specified position would be legal
- outOfBOunds(): Checks if the given coordinate is out of bounds of the game board
- getRedPlayer(): returns the red player
- getBluePlayer(): return the blue player



The IPawnsBoardModel interface is now an extension of the interface above. It includes
methods that actually mutate the board and reinforces game logic and functionality.
This includes:
- startGame(): starts the game by initiailizing player hands and setting up the intial game state
- placeCard(): places a card on the board at the given coordinate
- passTurn(): allows the current player to pass their turn



The IPawnsBoardView is the main visual view for the Pawns Board game.
The BoardPanel and CardPanel components that make up the entire visual view

Implementations of IBoardPanel are expected to
 * - Visually render the current board state: cells, cards, pawns
 * - Highlight a selected cell
 * - Display row scores for both players
 * - Handle resizing properly to ensure responsive layout

Implementations of ICardPanel are expected to:
 * - render each card in the current player's hand
 * - highlight which card is selected
 * - support mouse interactions for selecting and deselecting cards

A stub controller, PawnsBoardStubController, was implemented for debugging the Pawns Board game GUI.
This controller logs user interactions including card selection, cell selection, and
keyboard events (confirm/pass) to the console.

the action to confirm is activated by the key ENTER
the action to pass is activate by the key P



Implemented two strategic computer player strategies for Pawns Board game. The source
code and tests are found in the strategy package.
Fill First
- scans all cards in hand and board positions from top left to bottom right
- chooses the first legal move it finds and plays there
- if no move is legal, it passes
Maximize Row Score
- Visits row from top to bottom
- if the current player's score is less than or equal to the opponent's, it attempts
to make a move that improves that row's score
- chooses the first legal card position that flips the row score


In order to reduce the number of files:
Deleted AbstractPlayer: all players in the game have the same behavior
Deleted AbstractDeck: all decks in the game have the same behavior
Deleted IDeck: no future extension of deck is needed.
Deleted IInfluenceGrid: no future extension of influence grid is needed.
Deleted AbstractCard: all cards in the game have the same behavior
Deleted textual view package:
since this is not needed in future implementation after GUI implementation
Deleted textual view tests:
since textual view classes are deleted


CHANGES FOR PART 3:
A new controller package was added to store the controller implementation.

Created ModelListener interface that allows controller to observe model state
changes
- The model notifies registered listeners when turn changes or the game ends
- Each controller reigsters as a mdoel listener for its associated player
- the IPawnsBoardModel was extended to include addModelListener functionality

Implemented a PlayerListener interface for controllers to observe user actions from
the gui
- Views publish events when players select cards, select cells, confirm moves, or pass
turns
- This creates a clear separation ebtween the view and controller logic.

The controller serves as the mediator between the model and view
- PawnsBoardController implements both ModelListener and PlayerListener interfaces
- it maintains state for the current player's selections, including the card and cell
- enforces turn based rules and prevents out  of turn actions
- provides title changes to indicate whose turn it is
- handles different behaviors of human and machine players
- when a turn ends, selections are select so that the view is clear for the next player
- controller supports machine players by automatically triggering strategies

Player implementation
- two distinct player types were implemented to support both human interaction and computer
controlled gameplay
- HumanPlayer extends AbstractPlayer to represent human controlled players.
- it has an empty move() method since human players make moves through the GUI interactions
- MachinePlayer extends AbstractPlayer to represent computer controlled players.
- its move() method compute and execute the best action using the given strategy classes.

- Both players extend the base AbstractPlayer class which handles the core player functionality

Testing
Mock classes ere created to simulate the view and controller
- MockController tracks method calls and player interactions
- McokView simulates a view without requiring actual GUI components

PawnsBoardModelImpl was altered to incorporate the listeners from the controller.
- added a map field to store listeners
- addModelListener was added to register observers
- notifyNextTurn() handles all notifications, this method is used to notify turn changes
for controllers
- placeCard() and passTurn() methods were modified to call notifyNextTurn() after
changing the active player.

BoardPanel and CardPanel
- added PlayerListener field to both panel classes
- implemented setListener() methods to recive a controller reference
- mouse handling and keyboard handling methods notify listeners of selection events
GuiPawnsBaordView
- added resetSelections() to clear selection state


Command line arguments:
java -jar Assignment05OOD.jar docs/deck.config docs/deck.config human strategy1
java -jar Assignment05OOD.jar docs/deck.config docs/deck.config human strategy2

4 Arguments
docs/deck.config docs/deck.config human strategy1
docs/deck.config docs/deck.config human strategy2