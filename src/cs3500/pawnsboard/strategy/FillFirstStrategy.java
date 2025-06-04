package cs3500.pawnsboard.strategy;

import java.util.List;

import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IBoard;
import cs3500.pawnsboard.model.IPlayer;

/**
 * Strategy that selects the first legal move by scanning cards and position.
 * the strategy uses a 0-indexed coordinate system with row and column indexes.
 */
public class FillFirstStrategy implements IStrategy {
  @Override
  public Move chooseMove(ReadOnlyPawnsBoardModel model, IPlayer player) {
    IBoard board = model.getBoard();
    boolean isRed = player.getName().equalsIgnoreCase("red");

    List<ICard> hand = model.getPlayerHand(player);

    for (ICard card : hand) {
      for (int row = 0; row < board.getRows(); row++) {
        for (int col = 0; col < board.getCols(); col++) {
          if (model.isMoveLegal(row, col, card, isRed)) {
            return Move.placementMove(card, row, col);
          }
        }
      }
    }
    return Move.pass();
  }
}
