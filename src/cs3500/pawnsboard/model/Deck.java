package cs3500.pawnsboard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of Card cards in Pawn Board.
 */
public class Deck {
  private final List<ICard> cards;

  /**
   * Constructs a deck with a given list of cards.
   * @param cards A list of Card objects
   */
  public Deck(List<ICard> cards) {
    this.cards = new ArrayList<>(cards);
  }

  /**
   * Draws the next card from the deck.
   * @return the drawn card, or null if the deck is empty.
   */
  public ICard drawCard() {
    if (!cards.isEmpty()) {
      return cards.remove(0);
    }
    return null;
  }

  /**
   * Gets the number of remaining cards in the deck.
   * @retrun The number of cards left in the deck.
   */
  public int size() {
    return cards.size();
  }
}
