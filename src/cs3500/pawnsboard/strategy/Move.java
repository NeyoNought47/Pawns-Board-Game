package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.ICard;

/**
 * Represents a strategic move in the Pawns Board game.
 * A move can either be a placement of a card at a specific board coordinate,
 * or a pass.
 */
public class Move {
  private final boolean isPass;
  private final int row;
  private final int col;
  private final ICard card;

  /**
   * private constructor for creating both pass and placement moves.
   * @param isPass whether this move is a pass.
   * @param card the card being placed
   * @param row the row index
   * @param col the column index
   */
  private Move(boolean isPass, ICard card, int row, int col) {
    this.isPass = isPass;
    this.card = card;
    this.row = row;
    this.col = col;
  }

  /**
   * Creates a move representing a card placement.
   * @param card the card to place
   * @param row the row index
   * @param col the column index
   * @return a placement move with the given card and coordinates
   */
  public static Move placementMove(ICard card, int row, int col) {
    return new Move(false, card, row, col);
  }

  /**
   * Creates a move representing a card placement.
   * @return a move that represent a pass turn.
   */
  public static Move pass() {
    return new Move(true, null, -1, -1);
  }

  /**
   * Returns the card involved in the move.
   *
   * @return the card being placed, or {@code null} if this is a pass
   */
  public ICard getCard() {
    return card;
  }

  /**
   * Returns the row of the board where the card would be placed.
   *
   * @return the row index, or -1 if this is a pass
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column of the board where the card would be placed.
   *
   * @return the column index, or -1 if this is a pass
   */
  public int getCol() {
    return col;
  }

  public boolean isPass() {
    return isPass;
  }

  @Override
  public String toString() {
    if (isPass) {
      return "Pass";
    } else {
      return "Place card: " + card.getName() + " on (" + row + ", " + col + ")";
    }
  }
}

