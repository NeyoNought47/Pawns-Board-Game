package cs3500.pawnsboard.model;

import java.util.List;

import cs3500.pawnsboard.controller.PlayerListener;

/**
 * Represents a player in Pawns Board.
 * This interface abstracts the common functionality required for all player types.
 * this includes human controlled and machine controlled players.
 * It defines methods for accessing deck, drawing cards, identifying player type, and
 * executing moves.
 */
public interface IPlayer {
  /**
   * Gets the player's deck.
   * @return the player's deck.
   */
  Deck getDeck();

  /**
   * Gets the player's current hand.
   * @return A list of cards in the player's hand.
   */
  List<ICard> getHand();

  /**
   * Draws a card from the players' deck.
   */
  void drawCard();

  /**
   * Discards a card after the player have placed it.
   */
  void removeCard(ICard card);

  /**
   * Returns the name of the player.
   * @return the player's name.
   */
  String getName();

  /**
   * Indicates whether this player is machine controlled or human controlled.
   * @return true if this is a machine player, false if this is a human player.
   */
  boolean isMachine();

  /**
   * Executes a move for this player based on the current game state.
   * @param model the read only model provding the current game state.
   * @param listener the listener that will receive player actions.
   */
  void move(ReadOnlyPawnsBoardModel model, PlayerListener listener);
}
