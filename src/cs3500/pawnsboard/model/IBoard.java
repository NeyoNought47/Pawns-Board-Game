package cs3500.pawnsboard.model;

/**
 * Represents the board in Pawns Board.
 * The board is a 2D grid composed of ICell instances that may contain cards or pawns, and
 * each cell may be owned by a player or remain neutral.
 * Implementations are responsible for managing the layout of the game, including card
 * placements and interactions through influence or pawns.
 */
public interface IBoard {
  /**
   * Returns the number of rows of the board.
   * @return the number of rows
   */
  int getRows();

  /**
   * Returns the number of columns of the board.
   * @return the number of columns
   */
  int getCols();

  /**
   * Returns the cell at the given corrdinate.
   * @param row the row index
   * @param col the column index
   * @return the cell at the given indices.
   */
  ICell getCell(int row, int col);

  /**
   * Places a card at the given coordinate.
   * @param row the row index
   * @param col the column index
   * @param card the card to be placed at the cell
   * @param isRed true if the red player placed it, false otherwise
   */
  void placeCard(int row, int col, ICard card, boolean isRed);

}
