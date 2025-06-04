package cs3500.pawnsboard.model;

/**
 * A concrete implementation of the ReadOnlyPawnsBoardModel interface,
 * provoding a read only view of the Pawns Board game model.
 * This class extends AbstractPawnsBoardModel and delegates all observation based method calls
 * to it. It is intended to be used by the GUI to read game state without mutation.
 */
public class ReadOnlyPawnsBoardModelImpl extends AbstractPawnsBoardModel
        implements ReadOnlyPawnsBoardModel {


  /**
   * Constructs a new read only model instance given the board and players.
   * @param board the game board
   * @param red red player
   * @param blue blue player
   * @param handSize the expected hand size
   */
  public ReadOnlyPawnsBoardModelImpl(IBoard board, IPlayer red, IPlayer blue, int handSize) {
    super(board, red, blue, handSize);
  }
}
