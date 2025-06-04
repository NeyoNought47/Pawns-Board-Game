package cs3500.pawnsboard.controller;

/**
 * The interface defines methods that are called when a player performs game actions
 * through the view or strategy.
 * Bridges the user interface and the controller.
 * Responding to player events including, selecting a card, selecting a cell,
 * confirming a move, and passing their turn.
 * Can be triggered by both human and machine players.
 */
public interface PlayerListener {

  /**
   * This method is called when a player selects a card from their hand.
   * @param index the index of the selected card
   */
  void onCardSelected(int index);

  /**
   * Called when a player selects a cell on the game baord.
   * @param row the row index of the selected cell.
   * @param col the col index of the selecetd cell.
   */
  void onCellSelected(int row, int col);

  /**
   * Called when a player confirms their move after selecting a card and a cell.
   */
  void onConfirm();

  /**
   * Called when a player chooses to skip their turn.
   */
  void onPass();
}
