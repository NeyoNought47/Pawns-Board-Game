package cs3500.pawnsboard.controller;

import java.util.ArrayList;
import java.util.List;

import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.IPlayer;

/**
 * A mock implementation of both ModelListener and PlayerListener interfaces for testing purposes.
 * This class records all method calls and their parameters, allowing tests to verify
 * the correct sequence of interactions between components.
 * Use this mock for both unit tests and integration tests to verify the
 * correct sequencing and handling of events throughout the game flow.
 */
public class MockController implements ModelListener, PlayerListener {

  private final IPlayer player;
  private final IPawnsBoardModel model;
  private boolean isMyTurn = false;
  private Integer cardIndex = null;
  private int[] cellIndex = null;

  private final List<String> callLog = new ArrayList<>();
  private final List<String> errorLog = new ArrayList<>();


  /**
   * Constructs a new mock controller for testing.
   *
   * @param player the player associated with this controller
   * @param model the game model to interact with
   */
  public MockController(IPlayer player, IPawnsBoardModel model) {
    this.player = player;
    this.model = model;
    model.addModelListener(this);
  }

  @Override
  public void onCardSelected(int index) {
    callLog.add("onCardSelected: " + index);

    if (!isMyTurn) {
      errorLog.add("Card selected when not player's turn");
      return;
    }
    cardIndex = index;
  }


  @Override
  public void onCellSelected(int row, int col) {
    callLog.add("onCellSelected: " + row + ", " + col);

    if (!isMyTurn) {
      errorLog.add("Cell selected when not player's turn");
      return;
    }
    cellIndex = new int[]{row, col};
  }


  @Override
  public void onConfirm() {
    callLog.add("onConfirm");
    if (!isMyTurn) {
      errorLog.add("Move confirmed when not player's turn");
      return;
    }
    if (cardIndex == null || cellIndex == null) {
      errorLog.add("Attempted to confirm move without selected card or cell");
      return;
    }

    ICard card = player.getHand().get(cardIndex);
    int row = cellIndex[0];
    int col = cellIndex[1];

    try {
      model.placeCard(row, col, card);
      cardIndex = null;
      cellIndex = null;
    } catch (Exception e) {
      errorLog.add("Error confirming move: " + e.getMessage());
    }
  }

  @Override
  public void onPass() {
    callLog.add("onPass");

    if (!isMyTurn) {
      errorLog.add("Attempted to pass when not player's turn");
      return;
    }

    try {
      model.passTurn();
      cardIndex = null;
      cellIndex = null;
    } catch (Exception e) {
      errorLog.add("Error passing turn: " + e.getMessage());
    }
  }


  @Override
  public void onMyTurn() {
    callLog.add("onMyTurn");
    isMyTurn = true;
    if (player.isMachine()) {
      callLog.add("Triggering machine player move");
      player.move(model, this);
    }
  }


  @Override
  public void onGameOver(String msg) {
    callLog.add("onGameOver: " + msg);
    isMyTurn = false;
    cardIndex = null;
    cellIndex = null;
  }


  @Override
  public IPlayer getPlayer() {
    return player;
  }


  /**
   * Returns the log of calls to the controller.
   * protected method used for testing.
   * @return an arraylist of the call log.
   */
  protected List<String> getCallLog() {
    return new ArrayList<>(callLog);
  }

  /**
   * Returns the log of errors raised by the controller.
   * protected method used for testing.
   * @return an arraylist of the error log.
   */
  public List<String> getErrorLog() {
    return new ArrayList<>(errorLog);
  }
}