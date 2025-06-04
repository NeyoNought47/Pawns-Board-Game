package cs3500.pawnsboard.model;

import java.util.List;

/**
 * A read only interface for the Pawns Board model.
 * This interface exposes only observation methods that allow external
 * GUI or strategy agents to query the current game state without the ability to mutate it
 * All modifying poerations such as placing cards, passing turns, or drawing cards are handled
 * by a corresponding IPawnsBoardModel implementation
 * The implementation uses a 0 indexed coordinate system with row index on the y axis and column
 * index as the x axis.
 */
public interface ReadOnlyPawnsBoardModel {

  /**
   * Returns the card at the specified board coordinate.
   * @param row the row index
   * @param col the column index
   * @return the card at (row, col)
   */
  ICard getCardAt(int row, int col);

  /**
   * Returns the current hand of the given player.
   * @param player the given player
   * @return a lsit of cards in the player's hand
   */
  List<ICard> getPlayerHand(IPlayer player);

  /**
   * Returns the score for the specified player in the given row.
   * @param row the row index
   * @param player the player to score for
   * @return the score of the player in that row
   */
  int scoreAtRow(int row, IPlayer player);

  /**
   * Returns the current total score of the given player.
   * This score is based on which rows the player currently leads.
   * @param player the given player
   * @return the player's total score
   */
  int currentScore(IPlayer player);

  /**
   * Gets the current state of the game.
   * @return the current GameState of the game.
   */
  GameState getGameState();

  /**
   * Returns the game board.
   * @return the game board.
   */
  IBoard getBoard();

  /**
   * Returns a copy of the current game board.
   * @return a copy of the current game baord.
   */
  IBoard getBoardCopy();

  /**
   * Returns whether it is red player's turn.
   * @return true if it is red player's turn, false otherwise.
   */
  boolean isRedTurn();

  /**
   * Returns whether the game is over.
   * @return true if the game is in a terminal state, false otherwise.
   */
  boolean isGameOver();

  /**
   * determines and returns the winner of the game.
   * only callable once the game is over.
   * @return the GameState representing the winner or tie
   */
  GameState determineWinner();

  /**
   * Checks whether placing the given card at the specified position would be legal.
   * @param row the row index
   * @param col the column index
   * @param card the card to place
   * @param redCard true if the red player would place the card, false otherwise.
   * @return true if the move is legal, false otherwise.
   */
  boolean isMoveLegal(int row, int col, ICard card, boolean redCard);

  /**
   * Checks if the given coordinate is out of bounds of the game board.
   * @param row the row index
   * @param col the column index
   * @return true if the coordinate is out of bounds, false otherwise
   */
  boolean outOfBounds(int row, int col);

  /**
   * Returns the red player object.
   * @return the red player
   */
  IPlayer getRedPlayer();

  /**
   * returns the blue player object.
   * @return the blue player
   */
  IPlayer getBluePlayer();
}
