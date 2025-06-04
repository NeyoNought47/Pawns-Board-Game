package cs3500.pawnsboard.model;


import cs3500.pawnsboard.controller.ModelListener;

/**
 * A full implementation of the mutable IPawnsBoardModel interface for the Pawns
 * Board game.
 * This class extends AbstractPawnsBoardModel to support full game functionality.
 * including turn transition, card placement, pawn influence, game over detection.
 * It tracks turn state, player hands, and passing logic.
 * This implementation uses a 0-indexed row and column coordinate.
 * The origin is at the top left of the board, the row index represent the y-axis, while the
 * column index represent the x-axis.
 * Some invariants include
 * - the red player always takes the first turn
 * - the game state is one of NOT_STARTED, ONGOING, DONE, RED_WINS, BLUE_WINS,TIE
 * - A player cannot play a card they don't possess
 * - The game ends when there are no valid moves left for the players, or both player passes
 * The board never contains a card on a cell that is already occupied by another card.
 */
public class PawnsBoardModelImpl extends AbstractPawnsBoardModel implements IPawnsBoardModel {
  private boolean redPassed;
  private boolean bluePassed;
  private final java.util.Map<IPlayer, ModelListener> modellisteners = new java.util.HashMap<>();

  /**
   * Constructs a new Pawns Board game model with the specified board and players.
   * The game starts with red player.
   * @param board the board on which the game is played on
   * @param red red player
   * @param blue blue player
   * @param handSize number of cards in the player's hand
   */
  public PawnsBoardModelImpl(IBoard board, IPlayer red, IPlayer blue, int handSize) {
    super(board, red, blue, handSize);
    this.redPassed = false;
    this.bluePassed = false;
  }

  @Override
  public void startGame() {
    if (gameState != GameState.NOT_STARTED) {
      throw new IllegalStateException("Game has already started");
    }
    for (int i = 0; i < handSize; i++) {
      red.drawCard();
      blue.drawCard();
    }
    for (int row = 0; row < board.getRows(); row++) {
      board.getCell(row, 0).addPawns(1, true);
      board.getCell(row, board.getCols() - 1).addPawns(1, false);
    }
    gameState = GameState.ONGOING;
  }

  @Override
  public void placeCard(int row, int col, ICard card) {
    if (gameState == GameState.NOT_STARTED) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (gameState != GameState.ONGOING) {
      throw new IllegalStateException("Game has ended");
    }

    IPlayer current = isRed ? red : blue;
    if (!current.getHand().contains(card)) {
      throw new IllegalArgumentException("Current player doesn't have the card");
    }

    try {
      board.placeCard(row,col,card,isRed);
      applyInfluence(row,col,card);
      current.removeCard(card);
      current.drawCard();
    } catch (Exception e) {
      System.out.println("Invalid Move: " + e.getMessage());
      throw e;
    }

    if (isRed) {
      redPassed = false;
    }
    else {
      bluePassed = false;
    }
    isRed = !isRed;
    notifyNextTurn();
  }

  /**
   * Applies the influence pattern of the placed card to the board.
   * This method uses a 0-indexed coordinate system.
   * The offsetX and offsetY represent a coordinate system with the card's position as the origin.
   * @param row the row index
   * @param col the column index
   * @param card the card being placed
   */
  private void applyInfluence(int row, int col, ICard card) {
    InfluenceGrid grid = card.getInfluenceGrid();
    for (int offsetY = -2; offsetY <= 2; offsetY++) {
      for (int offsetX = -2; offsetX <= 2; offsetX++) {
        int targetX = row + offsetY;
        int targetY = col + offsetX;
        if (outOfBounds(targetX,targetY)) {
          continue;
        }

        boolean isEffected = grid.isInfluenced(offsetY + 2, offsetX + 2);
        if (isEffected) {
          ICell targetCell = board.getCell(targetX, targetY);
          if (!targetCell.hasCard()) {
            if (!targetCell.hasPawns()) {
              targetCell.addPawns(1, isRed);
            }
            else {
              if ((isRed && !targetCell.isOwnedByRed()) ||
                      (!isRed && targetCell.isOwnedByRed())) {
                targetCell.switchPawnsOwnership();
              }
              else {
                targetCell.addPawns(1, isRed);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Determines if a given player is done, meaning no more valid moves.
   * package-scoped because of testing
   * @param player the given player
   * @return false if the player can't move anymore, true otherwise
   */
  protected boolean isPlayerDone(IPlayer player) {
    for (ICard card : player.getHand()) {
      for (int row = 0; row < board.getRows(); row++) {
        for (int col = 0; col < board.getCols(); col++) {
          ICell cell = board.getCell(row, col);
          if (cell.isEmpty()
                  || (cell.hasPawns() && cell.getPawnCount() >= card.getCost())) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public void passTurn() {
    if (gameState == GameState.NOT_STARTED) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (gameState != GameState.ONGOING) {
      throw new IllegalStateException("Game has ended");
    }

    if (isRed) {
      redPassed = true;
    }
    else {
      bluePassed = true;
    }

    if (redPassed && bluePassed) {
      gameState = GameState.DONE;
      determineWinner();
    }
    isRed = !isRed;
    notifyNextTurn();
  }

  @Override
  public void addModelListener(ModelListener listener) {
    modellisteners.put(listener.getPlayer(), listener);
  }

  /**
   * Notifies the appropriate listener about turn changes or game end.
   * This method is called after any action that change whose turn it is or end the game.
   */
  private void notifyNextTurn() {
    if (gameState != GameState.ONGOING) {
      String message = "";
      if (gameState == GameState.RED_WINS) {
        message = "Red wins!";
      }
      else if (gameState == GameState.BLUE_WINS) {
        message = "Blue wins!";
      }
      else if (gameState == GameState.TIE) {
        message = "Tie!";
      }
      else if (gameState == GameState.DONE) {
        message = "Game over: no moves left.";
      }
      for (ModelListener l : modellisteners.values()) {
        l.onGameOver(message);
      }
      return;
    }
    IPlayer next = isRed ? red : blue;
    ModelListener nextListener = modellisteners.get(next);
    if (nextListener != null) {
      nextListener.onMyTurn();
    }
  }
}
