package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.gui.GuiPawnsBoardView;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.IPlayer;

/**
 * This controller implements both PlayerListener and ModelListener.
 * This allows it to respond to user interactions from the view (card/cell selection,
 * confirming, and passing).
 * Also allows it to respond to game state changes from the model (turn changes,
 * and game ending).
 */
public class PawnsBoardController implements PlayerListener, ModelListener {
  private final IPawnsBoardModel model;
  private final IPlayer player;
  private final GuiPawnsBoardView gui;

  private Integer cardIndex = null;
  private int[] cellIndex = null;
  private boolean myTurn = false;

  /**
   * Constructs a PawnsBoardController that mediates between the mode, a player, and the view.
   * @param model the game model
   * @param player the player, either human or machine
   * @param gui the graphical user interface with the player's view
   */
  public PawnsBoardController(IPawnsBoardModel model, IPlayer player, GuiPawnsBoardView gui) {
    this.model = model;
    this.player = player;
    this.gui = gui;
    this.gui.addPlayerListener(this);
    this.model.addModelListener(this);
  }

  @Override
  public void onCardSelected(int index) {
    if (!myTurn) {
      return;
    }
    cardIndex = index;
  }

  @Override
  public void onCellSelected(int row, int col) {
    if (!myTurn) {
      return;
    }
    cellIndex = new int[]{row, col};
  }

  @Override
  public void onConfirm() {
    if (!myTurn) {
      return;
    }
    if (cardIndex == null || cellIndex == null) {
      gui.showMessage("Must select card and cell before confirming.");
      return;
    }

    ICard card = player.getHand().get(cardIndex);
    int row = cellIndex[0];
    int col = cellIndex[1];

    try {
      model.placeCard(row, col, card);
      resetSelections();
    } catch (IllegalArgumentException | IllegalStateException e) {
      gui.showMessage("Invalid Move: " + e.getMessage());
    }
  }

  @Override
  public void onPass() {
    if (!myTurn) {
      return;
    }

    try {
      model.passTurn();
      resetSelections();
    } catch (IllegalStateException e) {
      gui.showMessage("Cannot pass: " + e.getMessage());
    }
  }

  /**
   * Resets all card and cell selection after a move is completed or passed.
   */
  private void resetSelections() {
    cardIndex = null;
    cellIndex = null;
    gui.resetSelections();
  }

  @Override
  public void onMyTurn() {
    myTurn = true;
    System.out.println(player.getName());
    gui.addPlayerListener(this);
    gui.setTitle(player.getName() + ": Your Turn");
    gui.setVisible(true);
    gui.requestBoardFocus();
    gui.repaint();
    if (player.isMachine()) {
      player.move(model, this);
    }
  }

  @Override
  public void onGameOver(String message) {
    myTurn = false;
    resetSelections();
    gui.showMessage(message);
  }

  @Override
  public IPlayer getPlayer() {
    return player;
  }
}