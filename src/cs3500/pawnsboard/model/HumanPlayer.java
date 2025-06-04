package cs3500.pawnsboard.model;

import cs3500.pawnsboard.controller.PlayerListener;

/**
 * HumanPlayer represents a human controlled player in the Pawns Board game.
 * this class extends AbstractPlayer and is designed to be controlled through
 * user interactions with the gui.
 * The moves are driven by GUI events rather than programmed strategies.
 */
public class HumanPlayer extends AbstractPlayer {
  /**
   * Constructs a new human player with the specified name, deck, and hand size.
   * @param name the name of the player
   * @param deck the deck from which this player draws card
   * @param handSize the number of cards this player holds.
   */
  public HumanPlayer(String name, Deck deck, int handSize) {
    super(name, deck, handSize);
  }

  @Override
  public void move(ReadOnlyPawnsBoardModel model, PlayerListener listener) {
    // Human player wait for gui interactions.
  }

  @Override
  public boolean isMachine() {
    return false;
  }
}
