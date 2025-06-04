package cs3500.pawnsboard.model;

/**
 * Represents a card in the Pawns Board game.
 * Each card has
 * - name
 * - cost, representing the number of pawns required to play
 * - value, represent its scoring power
 * - influence grid, describing how it affects surrounding cells
 * Cards are used during a player's turn and affect the board according to the influence grid.
 */
public interface ICard {

  /**
   * Returns the name of the card.
   * @return the name of the card.
   */
  String getName();

  /**
   * Returns the cost of the card.
   * @return the cost of the card.
   */
  int getCost();

  /**
   * Returns the value of the card.
   * @return the value of the card.
   */
  int getValue();

  /**
   * Returns the influence grid of the card.
   * @return the influence grid of the card.
   */
  InfluenceGrid getInfluenceGrid();

  /**
   * Checks if this card is equal to given card.
   * @param other the given ICard
   * @return true if they are the same, false otherwise.
   */
  boolean equals(Object other);

}
