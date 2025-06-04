package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.controller.PlayerListener;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.ICell;


import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * A concrete implementation of IBoardPanel that draws the game board using Java Swing. It
 * renders each cell in the grid, including any pawns or cards present, and highlights
 * the currently selected cell (if any).
 * This panel accommodates for user interactions:
 * - Mouse clicks to select and deselect a board cell
 * - Key press to trigger confirm or pass actions
 *  - the confirm action is activated by ENTER
 *  - the pass action is activated by P
 * It utilizes IPawnsBoardStubController for user inputs so that actions are logged in the console.
 * The selected int array stores the row and column indices of the selected cell on the board.
 */
public class BoardPanel extends JPanel implements IBoardPanel {
  private final ReadOnlyPawnsBoardModel model;
  private PlayerListener listener;
  private int[] selected = null;
  private boolean listenersAdded = false;

  /**
   * Constructs a new BoardPanel.
   * @param model the read only game model providing board state.
   * @param listener .
   */
  public BoardPanel(ReadOnlyPawnsBoardModel model, PlayerListener listener) {
    this.model = model;
    this.listener = listener;
    this.setPreferredSize(new Dimension(800, 450));

  }

  /**
   * Makes this panel focusable and requests focus for key events.
   * This ensures key listeners will work when this panel is active.
   */
  public void grabFocusForKeys() {
    this.setFocusable(true);
    this.requestFocusInWindow();
  }

  /**
   * Sets the listener for userinteractions and adds mouse and key listeners if they
   * haven't been added.
   * @param listener the PlayerListener to notify of user interactions
   */
  public void setListener(PlayerListener listener) {
    this.listener = listener;
    if (!listenersAdded) {
      this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          handleMouseClick(e);
        }
      });
      this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (BoardPanel.this.listener == null) {
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            BoardPanel.this.listener.onConfirm();
          } else if (e.getKeyCode() == KeyEvent.VK_P) {
            BoardPanel.this.listener.onPass();
          }
        }
      });
      listenersAdded = true;
    }
  }

  /**
   * Clears the current cell selection and repaints the board to remove
   * any selection highlighting.
   */
  public void resetSelection() {
    this.selected = null;
    repaint();
  }

  /**
   * Process a mouse click event on the board, determining whether a cell was selected
   * or deselected. It also notifies the controller of inputs.
   * The method uses 0-indexed coordinate system to record the position of the cell.
   * @param e the mouse event.
   */
  private void handleMouseClick(MouseEvent e) {
    int rows = model.getBoard().getRows();
    int cols = model.getBoard().getCols();
    int cellSize = Math.min(getWidth() / (cols + 2), getHeight() / rows);

    int col = (e.getX() / cellSize) - 1;
    int row = e.getY() / cellSize;
    if (row >= 0 && row < rows && col >= 0 && col < cols) {
      if (selected != null && selected[0] == row && selected[1] == col) {
        selected = null;
      }
      else {
        selected = new int[]{row, col};
        listener.onCellSelected(row, col);
      }
      repaint();
      requestFocusInWindow();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    int rows = model.getBoard().getRows();
    int cols = model.getBoard().getCols();
    int cellSize = Math.min(getWidth() / (cols + 2), getHeight() / (rows));

    paintBoard(g2, rows, cols, cellSize);
    paintScores(g2, rows, cols, cellSize);
  }

  /**
   * Renders all the cells on the board, including their contents (pawns and cards) and
   * highlighting the selected cell (if any).
   * The pawns are represented by filled circles, and the number of pawns are indicated by
   * the number of circles.
   * The cards are represented by a number that shows the value of the card.
   * This method uses an x y coordinate system to draw the cells.
   * @param g2 Graphics2D
   * @param rows the row index.
   * @param cols the column index.
   * @param cellSize the length of the cells in pixels.
   */
  private void paintBoard(Graphics2D g2, int rows, int cols, int cellSize) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int x = (col + 1) * cellSize;
        int y = row * cellSize;
        ICell cell = model.getBoard().getCell(row, col);

        if (selected != null && selected[0] == row && selected[1] == col) {
          g2.setColor(new Color(0, 204, 204));
        } else {
          g2.setColor(Color.GRAY);
        }

        g2.fillRect(x, y, cellSize, cellSize);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, cellSize, cellSize);

        if (cell.hasCard()) {
          drawCard(g2, cell, cellSize, x, y);
        }
        else if (cell.hasPawns()) {
          drawPawns(g2, cell, cellSize, x, y);
        }
      }
    }
  }

  /**
   * Renders a card in a board cell with background color based on ownership.
   * If the card is owned by red player, the color is red, if the card is owned by blue player,
   * the color is blue.
   * @param g2 Graphics2D
   * @param cell the target cell
   * @param size the length of the cell
   * @param x the x coordinate of the cell
   * @param y the y coordinate of the cell
   */
  private void drawCard(Graphics2D g2, ICell cell, int size, int x, int y) {
    g2.setColor(cell.isOwnedByRed() ?
            new Color(238, 100, 100) :
            new Color(67, 124, 222));
    g2.fillRect(x, y, size, size);
    g2.setColor(Color.BLACK);
    g2.drawString(Integer.toString(cell.getCard().getValue()),
            x + size / 2, y + size / 2);
  }

  /**
   * Draws between 1 to 3 pawns in the cell depending on the count, spaced evently.
   * The pawns owned by red player will be red, and the pawns owned by blue player will be blue.
   * The method uses an x y coordinate system to draw the pawns.
   * @param g2 Graphics2D
   * @param cell the target cell
   * @param size the length of the cell
   * @param x the x coordinate of the cell
   * @param y the y coordinate of the cell
   */
  private void drawPawns(Graphics2D g2, ICell cell, int size, int x, int y) {
    g2.setColor(cell.isOwnedByRed() ?
            new Color(238, 100, 100) :
            new Color(67, 124, 222));
    if (cell.getPawnCount() == 1) {
      g2.fillOval(x + size / 2 - 10, y + size / 2 - 10,
              20, 20);
    }
    else if (cell.getPawnCount() == 2) {
      g2.fillOval(x + size / 3 - 10, y + size / 2 - 10,
              20, 20);
      g2.fillOval(x + 2 * size / 3 - 10, y + size / 2 - 10,
              20, 20);
    }
    else if (cell.getPawnCount() == 3) {
      g2.fillOval(x + size / 6 - 10, y + size / 2 - 10,
              20, 20);
      g2.fillOval(x + size / 2 - 10, y + size / 2 - 10,
              20, 20);
      g2.fillOval(x + 5 * size / 6 - 10, y + size / 2 - 10,
              20, 20);
    }
  }

  /**
   * Draws two columns on either side of the board to write the scores for each player on each
   * row. The score that is the highest of the row will be highlighted by a circle.
   * The method uses 0-indexed row and column indices.
   * @param g2 Graphics2D
   * @param rows the row index
   * @param cols the column index
   * @param cellSize the length of the cell
   */
  private void paintScores(Graphics2D g2, int rows, int cols, int cellSize) {
    for (int row = 0; row < rows; row++) {
      int redScore = model.scoreAtRow(row, model.getRedPlayer());
      int blueScore = model.scoreAtRow(row, model.getBluePlayer());

      g2.setColor(redScore > blueScore ?
              new Color(238, 100, 100) :
              new Color(255, 0, 0, 0));
      g2.setStroke(new BasicStroke(3));
      g2.drawOval( 0, row * cellSize, cellSize, cellSize);
      g2.setColor(Color.BLACK);
      g2.drawString(Integer.toString(redScore),
              0 + cellSize / 2, (row * cellSize) + cellSize / 2);

      int x = (cols + 1) * cellSize;
      g2.setColor(blueScore > redScore ?
              new Color(67, 124, 222) :
              new Color(255, 0, 0, 0));
      g2.drawOval( x, row * cellSize, cellSize, cellSize);
      g2.setColor(Color.BLACK);
      g2.drawString(Integer.toString(model.scoreAtRow(row, model.getBluePlayer())),
              x + cellSize / 2, (row * cellSize) + cellSize / 2);
    }
  }
}
