package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.IPlayer;

/**
 * Listener for game model updates.
 * This interface allows controllers to react to model updates, particularly
 * turn transitions and game endings.
 * Implementation of this interface serve as observers of the game model and can
 * respond to notifications about whose turn it is and when game ended.
 */
public interface ModelListener {

  /**
   * Called when it becomes this listern's player's turn to make a move.
   * This method update the corresponding view and enable user interface elements
   * for human players, and strategy to execute a move for machine players.
   */
  void onMyTurn();

  /**
   * Called when the game has ended, displaying a message on the outcome.
   * @param msg the message describing the game outcome.
   */
  void onGameOver(String msg);

  /**
   * Returns the player associated with this model listener.
   * @return the player that this listener is associated with.
   */
  IPlayer getPlayer();
}
