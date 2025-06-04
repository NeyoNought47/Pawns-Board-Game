package cs3500.pawnsboard.model;

import java.util.List;

import cs3500.pawnsboard.controller.PlayerListener;
import cs3500.pawnsboard.strategy.IStrategy;
import cs3500.pawnsboard.strategy.Move;

/**
 * MachinePlayer represents an AI controlled player in the Pawns Board game.
 * This class extends AbstractPlayer and uses a strategy pattern to determine
 * its moves.
 * The machine player delegates to its strategy to compute the best action.
 * It differs from HumanPlayer as it doesn't wait for user input through the gui.
 */
public class MachinePlayer extends AbstractPlayer {
  IStrategy strategy;

  /**
   * Constructs a new machine player with the specified name, deck, hand size, and strategy.
   * @param name the name of the player
   * @param deck the deck from which this player draws card
   * @param handSize the number of cards this player holds
   * @param strategy the strategy implemntation that will determine this player's moves.
   */
  public MachinePlayer(String name, Deck deck, int handSize, IStrategy strategy) {
    super(name, deck, handSize);
    this.strategy = strategy;
  }

  @Override
  public void move(ReadOnlyPawnsBoardModel model, PlayerListener listener) {
    Move move = strategy.chooseMove(model, this);
    if (move.isPass()) {
      listener.onPass();
    }
    else {
      ICard card = move.getCard();
      int row = move.getRow();
      int col = move.getCol();

      List<ICard> hand = model.getPlayerHand(this);
      int cardIndex = hand.indexOf(card);
      if (cardIndex == -1) {
        listener.onPass();
        return;
      }

      listener.onCardSelected(cardIndex);
      listener.onCellSelected(row, col);
      listener.onConfirm();
    }
  }

  @Override
  public boolean isMachine() {
    return true;
  }
}
