# Overview

The Pawns Board Game is a strategic two-player game where players use cards to gain control of a board. Players can place cards in cells where they have pawns, and each card has an influence grid that affects nearby cells. The game determines a winner based on the scores on each row. It supports various board sizes and different sets of cards.

## The code includes

- A model that represents the game logic, including the board, players, cards, and decks.
- A deck reader to generate cards from a configuration file.
- A textual view to render the board state in textual form.
- A main method to run a sample game.

## Game Assumptions

- The board is rectangular with an odd number of columns.
- Players use separate decks; the blue deck reverses the influence grid.
- The game starts with three pawns per player, spread across the first and last columns.
- Players can only place cards in cells where they have pawns.
- The number of pawns must cover the cost of the card, and the player must own those pawns.

# Key Components

## Model

- `PawnsBoardModelImpl`: The main game logic implementation, manages turns, board updates, and game state.

## DeckReader

- Reads the deck configuration file and creates two separate decks:
  - Red deck uses the file's influence grid.
  - Blue deck reverses the grid.

## View

- Provides a textual visualization of the board.

# Key Subcomponents

- **Board**: Represents the grid where players place cards.
- **Cell**: A single square on the board, containing pawns or a card.
- **Player**: Represents a game participant with a hand and a deck.
- **Card**: Represents a card with an influence grid, value, and cost.
- **InfluenceGrid**: A 5x5 grid that determines how a card influences the board.

# Layout of the Code

## `docs/`
- `deck.config`: Configuration file for the cards and decks.

## `src/model/`
- `ICard`, `AbstractCard`, `Card`
- `IBoard`, `Board`
- `ICell`, `Cell`
- `IDeck`, `AbstractDeck`, `Deck`
- `IInfluenceGrid`, `InfluenceGrid`
- `IPlayer`, `AbstractPlayer`, `Player`
- `GameState`: Enum for game states
- `IPawnsBoardModel`, `PawnsBoardModelImpl`
- `DeckReader`

## `view/`
- `ITextualView`, `TextualPawnsBoardView`

## `test/`
- `PawnsBoardModelTest`: tests for the model
- `PawnsBoardViewTest`: tests for the view

# Future Improvements

- Implement AI players
- Add a graphical user interface
- Extend to different board sizes and custom decks

# User-Player Interface Design

### Methods:
- `startGame`: Initializes the game and sets up players and board.
- `renderGameState`: Renders current board state.
- `getUserAction`: Gets input (place card or pass).
- `execute`: Executes the move.
- `isValidMove`: Checks if move is valid.
- `isGameOver`: Checks if game has ended.

# Changes for Part 2

- Implemented `ReadOnlyPawnsBoardModelImpl` to support GUI without altering game state.
- `ReadOnlyPawnsBoardModel` includes methods like:
  - `getCardAt`, `getPlayerHand`, `scoreAtRow`, `currentScore`, `getGameState`, `getBoard`, `isRedTurn`, `isGameOver`, `determineWinner`, `isMoveLegal`, `outOfBounds`, `getRedPlayer`, `getBluePlayer`
- `IPawnsBoardModel` extends the above and adds:
  - `startGame`, `placeCard`, `passTurn`

## GUI View

### `IPawnsBoardView`, `BoardPanel`, `CardPanel` handle:
- Visual rendering of board/cards
- Selection highlights
- Responsive layout
- Mouse and keyboard interaction

### `PawnsBoardStubController`
- Logs user interaction (selection, confirmation, pass)

## AI Strategies

### Fill First
- Scan from top-left, play first legal move, else pass.

### Maximize Row Score
- Improve row score if losing/tied; else pass.

## Deleted Components (Simplified Design)

- `AbstractPlayer`, `AbstractDeck`, `IDeck`, `IInfluenceGrid`, `AbstractCard`
- Textual view package and tests (replaced by GUI)

# Changes for Part 3

### Controller Package

- `ModelListener` interface: Notifies turn change/end.
- `PlayerListener` interface: Observes GUI actions.
- `PawnsBoardController`: Implements both interfaces, mediates between model and view.

### Player Types

- `HumanPlayer`: Interacts via GUI.
- `MachinePlayer`: Uses strategies to make moves.

### Testing

- `MockController`, `MockView` simulate UI logic.
- `PawnsBoardModelImpl` modified:
  - Added listeners, `addModelListener`, `notifyNextTurn`

### GUI Panel Updates

- `setListener` methods in `BoardPanel` and `CardPanel`
- `GuiPawnsBoardView` supports `resetSelections`

# Command Line Usage

```bash
java -jar Assignment05OOD.jar docs/deck.config docs/deck.config human strategy1
java -jar Assignment05OOD.jar docs/deck.config docs/deck.config human strategy2