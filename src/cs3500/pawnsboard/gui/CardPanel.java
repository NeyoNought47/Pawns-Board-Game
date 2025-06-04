package cs3500.pawnsboard.gui;

import cs3500.pawnsboard.controller.PlayerListener;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IPlayer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

/**
 * A concrete implementation of ICardPanel that displays the hand of the current player
 * in the Pawns Board game. Cards are rendered horizontally with textual and grid based
 * visual information.
 * This panel allows players to:
 * - lick on a card to select or deselect it
 * - visually see which card is selected through highlights
 * - utilizes the IPawnsBoardStubController to log card selections
 * the selected Integer variable stores the index of the card highlighted
 */
public class CardPanel extends JPanel implements ICardPanel {
  private final ReadOnlyPawnsBoardModel model;
  private PlayerListener listener;
  private Integer selected = null;

  /**
   * Constructs a new CardPanel given the read only model and stub controller.
   * @param model the read only game model providing player hand information
   * @param listener .
   */
  public CardPanel(ReadOnlyPawnsBoardModel model, PlayerListener listener) {
    this.model = model;
    this.listener = listener;
    this.setPreferredSize(new Dimension(800, 300));
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleMouseClick(e);
      }
    });
  }

  /**
   * Sets the listener for user interactions.
   * @param listener the PlayerListener to notify of user interactions
   */
  public void setListener(PlayerListener listener) {
    this.listener = listener;
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
   * Handles mouse clicks to determine which card was selected.
   * Deselects if the clicked card is already selected. Otherwise, selects it and
   * notifies the controller.
   * @param e the mouse click event.
   */
  private void handleMouseClick(MouseEvent e) {
    IPlayer currentPlayer = model.isRedTurn() ? model.getRedPlayer() : model.getBluePlayer();
    List<ICard> hand = model.getPlayerHand(currentPlayer);
    int cardWidth = getWidth() / hand.size();
    int clicked = e.getX() / cardWidth;

    if (clicked >= 0 && clicked < hand.size()) {
      if (selected != null && selected == clicked) {
        selected = null;
      } else {
        selected = clicked;
        listener.onCardSelected(clicked);
      }
      repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    IPlayer currentPlayer;
    if (model.isRedTurn()) {
      currentPlayer = model.getRedPlayer();
    }
    else {
      currentPlayer = model.getBluePlayer();
    }
    List<ICard> hand = model.getPlayerHand(currentPlayer);

    int cardWidth = getWidth() / hand.size();
    int cardHeight = getHeight();
    paintCards(g2, cardWidth, cardHeight, hand);
  }


  /**
   * Paints all cards in the player's hand, including the name, ownership, cost, value,
   * and a 5x5 influence grid.
   * @param g2 Graphics2D
   * @param cardWidth width allocated for the card
   * @param cardHeight height allocated for the card
   * @param hand the list of cards to display
   */
  private void paintCards(Graphics2D g2, int cardWidth, int cardHeight, List<ICard> hand) {
    for (int i = 0; i < hand.size(); i++) {
      int x = i * (cardWidth);

      if (selected != null && selected == i) {
        g2.setColor(new Color(0, 204, 204));
      } else {
        g2.setColor(model.isRedTurn() ? new Color(238, 100, 100) :
                new Color(67, 124, 222));
      }
      g2.fillRect(x, 0, cardWidth, cardHeight);
      g2.setColor(Color.BLACK);
      g2.drawRect(x, 0, cardWidth, cardHeight);

      ICard card = hand.get(i);
      g2.drawString(card.getName(), x + 5, 20);
      g2.drawString("cost: " + card.getCost(), x + 5, 40);
      g2.drawString("value: " + card.getValue(), x + 5, 60);

      drawGrid(g2, x, card, cardWidth, cardHeight);
    }
  }

  /**
   * Draws a 5x5 influene grid for the card.
   * - dark green squares for influence cells
   * - orange square in the center representing the card
   * - light gray for uninfluenced cells
   * padding is added to the grid to center the influence grid in the card box.
   * @param g2 Graphics2D
   * @param x x position of the card
   * @param card the card to draw
   * @param cardWidth width of the card
   * @param cardHeight height of the card
   */
  private void drawGrid(Graphics2D g2, int x, ICard card, int cardWidth, int cardHeight) {
    char[][] grid = card.getInfluenceGrid().getGrid();
    int gridX = cardWidth / 7;
    int gridY = cardHeight / 8;
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (card.getInfluenceGrid().isInfluenced(row, col)) {
          g2.setColor(new Color(0, 102, 0));
        }
        else {
          g2.setColor(Color.BLACK);
        }
        int padding = (int)((cardWidth / 2) - (2.5 * gridX));
        g2.fillRect(x + padding + col * gridX,
                80 + row * gridY, gridX, gridY);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(x + padding + col * gridX,
                80 + row * gridY, gridX, gridY);
        if (row == 2 && col == 2) {
          g2.setColor(new Color(204, 102, 0));
          g2.fillRect(x + padding + col * gridX,
                  80 + row * gridY, gridX, gridY);
        }
      }
    }
  }
}
