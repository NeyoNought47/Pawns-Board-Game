package cs3500.pawnsboard.model;

import java.util.List;

/**
 * An abstract base class for read only models of the Pawns Board game.
 * This implementation of ReadOnlyPawnsBoardModel provides default read only behaviors for
 * querying the board state, scoring, player hands, turn information, and game progress.
 * It is intended for GUI components to observe the game model without mutation.
 */
public abstract class AbstractPawnsBoardModel implements ReadOnlyPawnsBoardModel {
  protected final IBoard board;
  protected final IPlayer red;
  protected final IPlayer blue;
  protected static boolean isRed;
  protected static GameState gameState;
  protected final int handSize;

  /**
   * Constructs a new abstract read only model of the Pawns Board game.
   * @param board the board
   * @param red red player
   * @param blue blue player
   * @param handSize the number of cards each player should hold
   */
  public AbstractPawnsBoardModel(IBoard board, IPlayer red, IPlayer blue, int handSize) {
    this.board = board;
    this.red = red;
    this.blue = blue;
    this.isRed = true;
    this.handSize = handSize;
    this.gameState = GameState.NOT_STARTED;
  }

  @Override
  public ICard getCardAt(int rows, int cols) {
    if (!board.getCell(rows,cols).hasCard()) {
      throw new IllegalArgumentException("The cell doesn't have a card");
    }
    return board.getCell(rows,cols).getCard();
  }

  @Override
  public List<ICard> getPlayerHand(IPlayer p) {
    return p.getHand();
  }

  @Override
  public int scoreAtRow(int row, IPlayer p) {
    int red = 0;
    int blue = 0;
    for (int col = 0; col < board.getCols(); col++) {
      ICell cell = board.getCell(row, col);
      if (cell.hasCard()) {
        ICard card = cell.getCard();
        if (cell.isOwnedByRed()) {
          red += card.getValue();
        }
        else {
          blue += card.getValue();
        }
      }
    }
    if (p.getName().equalsIgnoreCase("red")) {
      return red;
    }
    else {
      return blue;
    }
  }

  @Override
  public int currentScore(IPlayer player) {
    int total = 0;
    for (int row = 0; row < board.getRows(); row++) {
      int redScore = scoreAtRow(row, red);
      int blueScore = scoreAtRow(row, blue);
      if (player.getName().equalsIgnoreCase("red")) {
        if (redScore > blueScore) {
          total += redScore;
        }
      }
      else {
        if (blueScore > redScore) {
          total += blueScore;
        }
      }
    }
    return total;
  }

  @Override
  public GameState getGameState() {
    return gameState;
  }

  @Override
  public IBoard getBoard() {
    return board;
  }

  @Override
  public IBoard getBoardCopy() {
    IBoard copy = new Board(board.getRows(), board.getCols());
    for (int row = 0; row < board.getRows(); row++) {
      for (int col = 0; col < board.getCols(); col++) {
        ICell ogCell = board.getCell(row, col);
        ICell copyCell = copy.getCell(row, col);
        if (ogCell.hasPawns()) {
          copyCell.addPawns(ogCell.getPawnCount(), ogCell.isOwnedByRed());
        }
        if (ogCell.hasCard()) {
          ICard ogCard = ogCell.getCard();
          copy.placeCard(row, col, ogCard, ogCell.isOwnedByRed());
        }
      }
    }
    return copy;
  }

  @Override
  public boolean isRedTurn() {
    return isRed;
  }

  @Override
  public boolean isGameOver() {
    return gameState == GameState.DONE
            || gameState == GameState.RED_WINS
            || gameState == GameState.BLUE_WINS
            || gameState == GameState.TIE;
  }

  @Override
  public GameState determineWinner() {
    if (!isGameOver()) {
      throw new IllegalStateException("Game is not done");
    }
    if (currentScore(red) > currentScore(blue)) {
      gameState = GameState.RED_WINS;
    }
    else if (currentScore(blue) > currentScore(red)) {
      gameState = GameState.BLUE_WINS;
    }
    else {
      gameState = GameState.TIE;
    }
    return gameState;
  }

  @Override
  public boolean isMoveLegal(int rows, int cols, ICard c, boolean redCard) {
    if (outOfBounds(rows, cols)) {
      return false;
    }
    if (redCard != board.getCell(rows, cols).isOwnedByRed()) {
      return false;
    }
    if (board.getCell(rows, cols).hasCard()) {
      return false;
    }
    return board.getCell(rows, cols).hasPawns()
            && board.getCell(rows, cols).getPawnCount() >= c.getCost();
  }

  @Override
  public boolean outOfBounds(int rows, int cols) {
    return rows < 0 || rows > board.getRows() - 1 || cols < 0 || cols > board.getCols() - 1;
  }

  @Override
  public IPlayer getRedPlayer() {
    return red;
  }

  @Override
  public IPlayer getBluePlayer() {
    return blue;
  }
}
