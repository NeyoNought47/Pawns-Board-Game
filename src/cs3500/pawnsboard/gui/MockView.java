package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.controller.PlayerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the GuiPawnsBoardView for testing purposes.
 * This mock records view interactions and can simulate user actions to facilitate
 * integration testing without requiring a GUI.
 */
public class MockView implements IPawnsBoardView {
  private PlayerListener listener;
  private final List<String> logs = new ArrayList<>();

  /**
   * Constructs a new MockView with the given model.
   *
   */
  public MockView() {
    // constructor
  }

  @Override
  public void addPlayerListener(PlayerListener listener) {
    logs.add("addPlayerListener");
    this.listener = listener;
  }

  /**
   * Records a request to reset selections in the view.
   */
  public void resetSelections() {
    logs.add("resetSelections");
  }

  /**
   * Records a request to show a message to the user.
   *
   * @param message the message that would be displayed to the user
   */
  public void showMessage(String message) {
    logs.add("showMessage: " + message);
  }

  /**
   * Records a request to focus the board panel for keyboard input.
   */
  public void requestBoardFocus() {
    logs.add("requestBoardFocus");
  }

  /**
   * Simulates a user selecting a card from their hand.
   *
   * @param index the index of the card in the player's hand
   */
  public void simulateCardSelected(int index) {
    logs.add("userSelectCard: " + index);
    if (listener != null) {
      listener.onCardSelected(index);
    }
  }

  /**
   * Simulates a user selecting a cell on the game board.
   *
   * @param row the row index of the selected cell
   * @param col the column index of the selected cell
   */
  public void simulateCellSelected(int row, int col) {
    logs.add("userSelectCell: " + row + ", " + col);
    if (listener != null) {
      listener.onCellSelected(row, col);
    }
  }

  /**
   * Simulates a user confirming their move.
   */
  public void simulateConfirm() {
    logs.add("userConfirm");
    if (listener != null) {
      listener.onConfirm();
    }
  }

  /**
   * Simulates a user passing their turn.
   */
  public void simulatePass() {
    logs.add("userPass");
    if (listener != null) {
      listener.onPass();
    }
  }

  /**
   * Returns the log of all actions performed on this view.
   *
   * @return a list of logged actions
   */
  public List<String> getLogs() {
    return new ArrayList<>(logs);
  }
}