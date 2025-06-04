package cs3500.pawnsboard.strategy;

import java.util.List;

import cs3500.pawnsboard.model.IBoard;
import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;

/**
 * Strategy that tries to win the earliest possible row by outscoring the opponent.
 * The strategy visits the board top down and returns the first move that will
 * allow the player to outscore their opponent on the particular row.
 * This strategy uses a 0-indexed coordinate system with row and column index.
 */
public class MaximizeRowScoreStrategy implements IStrategy {
  @Override
  public Move chooseMove(ReadOnlyPawnsBoardModel model, IPlayer player) {
    boolean isRed = player.getName().equalsIgnoreCase(model.getRedPlayer().getName());
    IPlayer opponent = isRed ? model.getBluePlayer() : model.getRedPlayer();
    List<ICard> hand = model.getPlayerHand(player);
    IBoard board = model.getBoard();

    int rows = board.getRows();
    int cols = board.getCols();

    for (int row = 0; row < rows; row++) {
      int playerScore = model.scoreAtRow(row, player);
      int opponentScore = model.scoreAtRow(row, opponent);

      if (playerScore <= opponentScore) {
        for (ICard card : hand) {
          for (int col = 0; col < cols; col++) {
            if (model.isMoveLegal(row, col, card, isRed)) {
              int newScore = playerScore + card.getValue();
              if (newScore > opponentScore) {
                return Move.placementMove(card, row, col);
              }
            }
          }
        }
      }
    }
    return Move.pass();
  }
}
