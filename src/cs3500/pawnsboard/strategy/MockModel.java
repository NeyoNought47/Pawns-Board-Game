package cs3500.pawnsboard.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;

import cs3500.pawnsboard.model.GameState;
import cs3500.pawnsboard.model.IBoard;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.ICell;
import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;



/**
 * A mock model for testing strategy logic without needing full board setup.
 * This class simulates legal moves and row scores based on manually configuered data,
 * and logs every method call that a strategy might make.
 * This allow tests to inspect which coordinates were checked, and what decisions
 * were made.
 * This model uses a 0-indexed coordinate system with row and column index.
 */
public class MockModel implements ReadOnlyPawnsBoardModel {
  private final List<String> log = new ArrayList<>();
  private final int rows;
  private final int cols;
  private final IPlayer red;
  private final IPlayer blue;

  private final Set<String> legalMoves = new HashSet<>();
  private final Map<String, Integer> rowScores = new HashMap<>();

  /**
   * Constructs a mock model for a board with the given dimensions and players.
   * @param rows the number of rows
   * @param cols the number of columbs
   * @param red the red player
   * @param blue the blue player
   */
  public MockModel(int rows, int cols, IPlayer red, IPlayer blue) {
    this.rows = rows;
    this.cols = cols;
    this.red = red;
    this.blue = blue;
  }

  /**
   * Returns the lsit of method calls logged during strategy evaluation.
   * This is useful for verifying access order in tests.
   * @return the log of recorded method calls
   */
  public List<String> getLog() {
    return log;
  }

  /**
   * Declares a move as legal given the card, row, col index.
   * @param card the card being palced
   * @param row the row index
   * @param col the column index
   * @param isRed true if red is placing the card, false otherwise
   */
  public void allowMove(ICard card, int row, int col, boolean isRed) {
    legalMoves.add(validMove(card, row, col, isRed));
  }

  /**
   * Creates a unique key for a move used internally for legality checking.
   * @param card the card being placed
   * @param row row index
   * @param col column index
   * @param isRed true if red is placing the card, false otherwise.
   * @return A String containing the move.
   */
  private String validMove(ICard card, int row, int col, boolean isRed) {
    return card.getName() + ":" + row + ":" + col + ":" + isRed;
  }

  /**
   * Sets the simulated row score for a specific player and row.
   * @param player the given player
   * @param row the row index
   * @param score the score number
   */
  public void setRowScore(IPlayer player, int row, int score) {
    rowScores.put(player.getName() + ":" + row, score);
  }

  @Override
  public boolean isMoveLegal(int x, int y, ICard card, boolean isRed) {
    if (isRed) {
      log.add("checked isMoveLegal at " + x + "," + y + " for card "
              + card.getName() + " for player " + red.getName());
    }
    else {
      log.add("checked isMoveLegal at " + x + "," + y + " for card "
              + card.getName() + " for player " + blue.getName());
    }
    return legalMoves.contains(validMove(card, x, y, isRed));
  }

  @Override
  public int scoreAtRow(int row, IPlayer player) {
    log.add("checked scoreAtRow for " + player.getName() + " at row " + row);
    return rowScores.getOrDefault(player.getName() + ":" + row, 0);
  }

  @Override
  public List<ICard> getPlayerHand(IPlayer p) {
    log.add("getPlayerHand for " + p.getName());
    return p.getHand();
  }

  @Override
  public int currentScore(IPlayer p) {
    return 0;
  }

  @Override
  public GameState getGameState() {
    return GameState.ONGOING;
  }

  @Override
  public IBoard getBoard() {
    return new IBoard() {

      public int getRows() {
        return rows;
      }

      public int getCols() {
        return cols;
      }

      public ICell getCell(int row, int col) {
        return null;
      }

      public void placeCard(int row, int col, ICard card, boolean isRed) {
        // mock model doesn't place card
      }
    };
  }

  @Override
  public IBoard getBoardCopy() {
    return new IBoard() {

      public int getRows() {
        return rows;
      }

      public int getCols() {
        return cols;
      }

      public ICell getCell(int row, int col) {
        return null;
      }

      public void placeCard(int row, int col, ICard card, boolean isRed) {
        // mock model doens't place card
      }
    };
  }

  @Override
  public boolean isRedTurn() {
    return true;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public GameState determineWinner() {
    return GameState.TIE;
  }

  @Override
  public boolean outOfBounds(int x, int y) {
    return false;
  }

  @Override
  public IPlayer getRedPlayer() {
    return red;
  }

  @Override
  public IPlayer getBluePlayer() {
    return blue;
  }

  @Override
  public ICard getCardAt(int x, int y) {
    return null;
  }
}
