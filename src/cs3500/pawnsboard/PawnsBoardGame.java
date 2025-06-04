package cs3500.pawnsboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardController;
import cs3500.pawnsboard.gui.GuiPawnsBoardView;
import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.Deck;
import cs3500.pawnsboard.model.DeckReader;
import cs3500.pawnsboard.model.HumanPlayer;
import cs3500.pawnsboard.model.IBoard;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.MachinePlayer;
import cs3500.pawnsboard.model.PawnsBoardModelImpl;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModel;
import cs3500.pawnsboard.model.ReadOnlyPawnsBoardModelImpl;
import cs3500.pawnsboard.strategy.FillFirstStrategy;
import cs3500.pawnsboard.strategy.IStrategy;
import cs3500.pawnsboard.strategy.MaximizeRowScoreStrategy;

/**
 * Entry point for the Pawns Board game.
 * Reads a deck configuration file, initializes the game, and runs a
 * sample game.
 */
public class PawnsBoardGame {
  /**
   * Main Method.
   * @param args inputs
   */
  public static void main(String[] args) {
    if (args.length < 4) {
      System.out.println("Not enough arguments");
      return;
    }

    // red deck and blue deck have the same path in this implementation.
    String redDeckPath = args[0];
    String blueDeckPath = args[1];
    String redPlayerType = args[2];
    String bluePlayerType = args[3];

    List<ICard>[] decks;
    // use this filepath if there is trouble running
    String filePath = "docs" + File.separator + "deck.config";
    try {
      decks = DeckReader.readDeckFile(redDeckPath);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      return;
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid format");
      return;
    }
    Deck redDeck = new Deck(decks[0]);
    Deck blueDeck = new Deck(decks[1]);

    IStrategy fillStrat = new FillFirstStrategy();
    IStrategy maxStrat = new MaximizeRowScoreStrategy();

    IBoard board = new Board(5, 7);

    IPlayer redPlayer = createPlayer("red", redDeck, redPlayerType, fillStrat, maxStrat);
    IPlayer bluePlayer = createPlayer("blue", blueDeck, bluePlayerType, fillStrat, maxStrat);

    IPawnsBoardModel model = new PawnsBoardModelImpl(board, redPlayer, bluePlayer, 5);
    ReadOnlyPawnsBoardModel readOnlyModel =
            new ReadOnlyPawnsBoardModelImpl(board, redPlayer, bluePlayer, 5);

    GuiPawnsBoardView gui = new GuiPawnsBoardView(readOnlyModel);
    PawnsBoardController redController = new PawnsBoardController(model, redPlayer, gui);
    PawnsBoardController blueController = new PawnsBoardController(model, bluePlayer, gui);
    model.startGame();

    if (model.isRedTurn()) {
      redController.onMyTurn();
    }
    else {
      blueController.onMyTurn();
    }
  }

  /**
   * Creates a player based on the given player type in the inputs.
   * the player can either be human, or computer player with two types of strataegies.
   * @param name player name.
   * @param deck player's deck.
   * @param playerType the player type.
   * @param fill fillfirst strategy.
   * @param max  max row score strategy
   * @return a new player with the given specifications.
   */
  private static IPlayer createPlayer(String name, Deck deck,
                                      String playerType, IStrategy fill, IStrategy max) {
    if (playerType.equalsIgnoreCase("human")) {
      return new HumanPlayer(name, deck, 5);
    }
    else if (playerType.equalsIgnoreCase("strategy1")) {
      return new MachinePlayer(name, deck, 5, fill);
    }
    else if (playerType.equalsIgnoreCase("strategy2")) {
      return new MachinePlayer(name, deck, 5, max);
    }
    else {
      return new HumanPlayer(name, deck, 5);
    }
  }
}
