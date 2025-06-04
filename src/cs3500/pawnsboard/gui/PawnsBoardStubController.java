package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.model.IPlayer;

/**
 * A stub implementation of the IPawnsBoardStubController interface used for debugging the
 * Pawns Board game GUI.
 * This controller logs user interactions including card selection, cell selection, and
 * keyboard events (confirm/pass) to the console.
 */
public class PawnsBoardStubController implements IPawnsBoardStubController {

  @Override
  public void handleCardClick(int cardIndex, IPlayer player) {
    System.out.println("Card clicked: index = " + cardIndex + ", player = " + player.getName());
  }

  @Override
  public void handleCellClick(int row, int col) {
    System.out.println("Cell clicked: row = " + row + ", col = " + col);
  }

  @Override
  public void handleConfirm() {
    System.out.println("Cell clicked: confirm");
  }

  @Override
  public void handlePass() {
    System.out.println("Player passes");
  }
}
