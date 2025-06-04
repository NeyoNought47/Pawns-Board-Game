package cs3500.pawnsboard.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.pawnsboard.controller.PlayerListener;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;



/**
 * A graphical implementation of the IPawnsBoardView interface that displays the full
 * Pawns Board game window using Java swing.
 * This view creates and displays a main game window.
 * Constructs a ViewFrame which contains all interactive board and card panel.
 * Attaches a IPawnsBoardStubController to respond to user actions, including cell/card selection,
 * select confirm and player pass.
 * Ensures the view is initialized with a ReadOnlyPawnsBoardModel.
 */
public class GuiPawnsBoardView extends JFrame implements IPawnsBoardView {
  private ViewFrame frame;

  /**
   * Constructs a new GUI-based view for the Pawns Board game.
   * @param model the read only model providing game state information for rendering.
   */
  public GuiPawnsBoardView(ReadOnlyPawnsBoardModel model) {
    super("Pawns Board");

    this.frame = new ViewFrame(model);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setContentPane(frame);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  @Override
  public void addPlayerListener(PlayerListener listener) {
    if (frame != null) {
      frame.addPlayerListener(listener);
    }
  }

  @Override
  public void resetSelections() {
    if (frame != null) {
      frame.resetSelections();
      repaint();
    }
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void requestBoardFocus() {
    this.frame.requestBoardFocus();
  }
}
