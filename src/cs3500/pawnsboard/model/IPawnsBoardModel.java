package cs3500.pawnsboard.model;

import cs3500.pawnsboard.controller.ModelListener;

/**
 * Represents the mutable interface for the Pawns Board game model.
 * This interface extends ReadOnlyPawnsBoardModel and adds methods that allow
 * game progression, including starting the game, placing cards, and passing turns.
 * implementation of this interface are responsible for maintaining game rules and states.
 */
public interface IPawnsBoardModel extends ReadOnlyPawnsBoardModel {
  /**
   * Starts the game by initializing player hands and setting up
   * the initial game state.
   * GameState should be ONGOING.
   * Each side of the board should start with three pawns each.
   * Each player should have their hands of cards filled.
   * @throws IllegalStateException if the game has already started
   */
  void startGame();

  /**
   * Places a card on the board at the given coordinate.
   * uses a 0-indexed coordinate system.
   * @param row the row index
   * @param col the column index
   * @param card the given card to be placed
   * @throws IllegalArgumentException if the player doesn't have the card in hand
   * @throws IllegalStateException if the game hasn't started
   * @throws IllegalStateException if the game has already ended
   */
  void placeCard(int row, int col, ICard card);

  /**
   * Allows the current player to pass their turn.
   * If both players pass consecutively then the game ends.
   * @throws IllegalStateException if the game hasn't started
   * @throws IllegalStateException if the game has ended
   */
  void passTurn();

  /**
   * Registers a listener to receive notifications about turn changes and game end.
   * This allows the model to notify the appropriate controller when it's that player's turn.
   * @param listener the ModelListener to register for game state notifications.
   */
  void addModelListener(ModelListener listener);

}
