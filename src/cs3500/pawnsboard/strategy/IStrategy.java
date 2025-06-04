package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Represents a strategy for selecting the best possible move for a player in a given
 * game state.
 */
public interface IStrategy {
  /**
   * Returns a list of possible best moves based on this strategy.
   *
   * @param model  the current game state
   * @param player the player making the move
   * @return a list of move candidates
   */
  Move chooseMove(ReadOnlyPawnsBoardModel model, IPlayer player);
}
