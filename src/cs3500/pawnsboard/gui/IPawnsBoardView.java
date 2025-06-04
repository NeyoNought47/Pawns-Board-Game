package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.controller.PlayerListener;

/**
 * Represents the main visual view for the Pawns Board game.
 * Displays the board and player hand and handles interactions like clicks and key presses.
 */
public interface IPawnsBoardView {

  /**
   * Registers a listener to receive notifications about player generated actions.
   * @param listener the PlayerListener instance
   */
  void addPlayerListener(PlayerListener listener);

  /**
   * Resets all card and cell selections in the user interface.
   * this ensures view properly reflects the current turn state.
   */
  void resetSelections();

  /**
   * Displays a message dialog to the user.
   * @param message the text message to display to the user.
   */
  void showMessage(String message);

  /**
   * Requests keyboard focus for the board panel.
   * This method ensures that the board panel can receive keyboard events.
   */
  void requestBoardFocus();
}
