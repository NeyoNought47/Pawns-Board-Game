package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.model.IPlayer;

/**
 * Represents a stub controller interface used for handling user interactions in
 * the Pawns Board game's GUI.
 * This controller is used during GUI development to capture and respond user inputs events
 * including
 * - card selection
 * - cell selection
 * - keyboard actions (confirm/pass)
 *  - the confirm action is activated by ENTER
 *  - the pass action is activiated by P
 * Implementations are expected to print or log information about user actions.
 */
public interface IPawnsBoardStubController {
  /**
   * Handles the event where the user selects a card in the player's hand.
   * @param cardIndex the index of the card that was clicked (0-indexed)
   * @param player the player whose hand the card belongs to (red/blue)
   */
  void handleCardClick(int cardIndex, IPlayer player);

  /**
   * Handles the event where the user selects a cell in the board grid.
   * The method uses a 0-indexed coordinate system in row and column indicies.
   * @param row the row index of the clicked cell
   * @param col the column index of the clicked cell
   */
  void handleCellClick(int row, int col);

  /**
   * Handles the event where the user presses the confirm key (ENTER).
   */
  void handleConfirm();

  /**
   * Handles the event where the user presses the pass key (P).
   */
  void handlePass();
}