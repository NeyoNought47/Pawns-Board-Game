package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.controller.PlayerListener;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;

/**
 * The central container panel for the Pawns Board GUI.
 * This class combines and lays out the key visual components of the game, which includes the
 * game board and the player's hand of cards.
 * Using a vertical layout to display the board above the hand. It delegates interaction
 * handling to a stub controller and draws its content based on a read only game model.
 */
public class ViewFrame extends JPanel {
  BoardPanel boardPanel;
  CardPanel handPanel;

  /**
   * Constructs a new ViewFrame using the given model and stub controller.
   * @param model the read only game model that provides state for rendering
   */
  public ViewFrame(ReadOnlyPawnsBoardModel model) {
    this.setLayout(new BorderLayout());

    boardPanel = new BoardPanel(model, null);
    handPanel = new CardPanel(model, null);

    JPanel stacked = new JPanel();
    stacked.setLayout(new BoxLayout(stacked, BoxLayout.Y_AXIS));

    stacked.add(boardPanel);
    stacked.add(handPanel);

    this.add(stacked, BorderLayout.CENTER);

  }

  /**
   * Resets all selections in both the board and card panels.
   * Clears any highlighted states in both panels, ensuring
   * that no cards or cells appear selected after a turn change.
   *
   */
  protected void resetSelections() {
    boardPanel.resetSelection();
    handPanel.resetSelection();
    repaint();
  }

  /**
   * Registers a PlayerListener to receive notifications about user interactions with
   * both the board and card panels.
   * @param listener the PlayerListener instance
   */
  protected void addPlayerListener(PlayerListener listener) {
    boardPanel.setListener(listener);
    handPanel.setListener(listener);
  }

  /**
   * Requests keyboard focus for the board panel.
   */
  protected void requestBoardFocus() {
    this.boardPanel.grabFocusForKeys();
  }
}
