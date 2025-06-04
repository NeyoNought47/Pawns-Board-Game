package cs3500.pawnsboard.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * A concrete implementation of IInfluenceGrid.
 * This grid determines which surrounding cells on the board will be affected when
 *  * a card is placed. Each cell in the grid can be one of
 *  * - 'I' influenced
 *  * - 'X' uninfluenced space
 *  * - 'C' the center card, origin of influence
 *  * the center of the grid is always at position (2,2), using a 0-indexed coordinate system
 */
public class InfluenceGrid {
  private final char[][] grid;

  /**
   * Constructs an InfluenceGrid.
   * INVARIANT: the grid must be a 5x5 matrix.
   * @param grid The 5x5 influence grid.
   * @throws IllegalArgumentException if the grid isn't 5x5
   */
  public InfluenceGrid(String[] grid) {
    if (grid.length != 5) {
      throw new IllegalArgumentException("grid must be 5x5.");
    }
    for (int i = 0; i < grid.length; i++) {
      if (grid[i].length() != 5) {
        throw new IllegalArgumentException("grid must be 5x5.");
      }
    }
    this.grid = new char[5][5];
    for (int i = 0; i < grid.length; i++) {
      this.grid[i] = grid[i].toCharArray();
    }
  }

  /**
   * Checks if a cell is influenced.
   * @param row the row index
   * @param col the column index
   * @return true if the cell is influenced, false otherwise.
   */
  public boolean isInfluenced(int row, int col) {
    return (row >= 0 && row < 5 && col >= 0 && col < 5 && grid[row][col] == 'I');
  }

  /**
   * Returns the influence grid.
   * @return the influence grid.
   */
  public char[][] getGrid() {
    return grid;
  }

  @Override
  public boolean equals(Object that) {
    if (!(that instanceof InfluenceGrid)) {
      return false;
    }
    InfluenceGrid other = (InfluenceGrid) that;
    return Arrays.deepEquals(this.getGrid(), other.getGrid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(grid);
  }
}
