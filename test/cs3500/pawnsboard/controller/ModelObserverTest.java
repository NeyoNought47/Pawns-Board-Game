package cs3500.pawnsboard.controller;


import cs3500.pawnsboard.gui.MockView;

import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.Deck;
import cs3500.pawnsboard.model.DeckReader;
import cs3500.pawnsboard.model.HumanPlayer;
import cs3500.pawnsboard.model.IBoard;
import cs3500.pawnsboard.model.IPawnsBoardModel;
import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.MachinePlayer;
import cs3500.pawnsboard.model.PawnsBoardModelImpl;
import cs3500.pawnsboard.strategy.FillFirstStrategy;
import cs3500.pawnsboard.strategy.IStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Integration tests for the Pawns Board game, testing the interactions between
 * the model and controller components without the GUI.
 */
public class ModelObserverTest {

  private IPawnsBoardModel model;
  private IPlayer redMachine;
  private IPlayer blueMachine;
  private MockView redView;
  private MockView blueView;
  private MockController redController;
  private MockController blueController;

  /**
   * Sets up the test environment before each test case.
   */
  @Before
  public void setUp() {
    List[] decks = new List[]{};
    try {
      String filePath = "docs" + File.separator + "deck.config";
      decks = DeckReader.readDeckFile(filePath);
    } catch (FileNotFoundException e) {
      Assert.fail("Deck file not found.");
    }

    Deck redDeck = new Deck(decks[0]);
    Deck blueDeck = new Deck(decks[1]);

    IStrategy fillFirst = new FillFirstStrategy();

    IPlayer redPlayer = new HumanPlayer("Red", redDeck, 5);
    IPlayer bluePlayer = new HumanPlayer("Blue", blueDeck, 5);
    redMachine = new MachinePlayer("Red", redDeck, 5, fillFirst);
    blueMachine = new MachinePlayer("Blue", blueDeck, 5, fillFirst);


    IBoard board = new Board(3, 5);

    model = new PawnsBoardModelImpl(board, redPlayer, bluePlayer, 5);

    redView = new MockView();
    blueView = new MockView();

    redController = new MockController(redPlayer, model);
    blueController = new MockController(bluePlayer, model);

    redView.addPlayerListener(redController);
    blueView.addPlayerListener(blueController);
  }

  @Test
  public void testBasicGameFlow() {
    model.startGame();
    redController.onMyTurn();

    redView.simulateCardSelected(0);
    redView.simulateCellSelected(2, 0);
    redView.simulateConfirm();

    assertEquals("addPlayerListener", redView.getLogs().get(0));
    assertEquals("userSelectCard: 0", redView.getLogs().get(1));
    assertEquals("userSelectCell: 2, 0", redView.getLogs().get(2));
    assertEquals("userConfirm", redView.getLogs().get(3));

    assertEquals("onMyTurn", redController.getCallLog().get(0));
    assertEquals("onCardSelected: 0", redController.getCallLog().get(1));
    assertEquals("onCellSelected: 2, 0", redController.getCallLog().get(2));
    assertEquals("onConfirm", redController.getCallLog().get(3));

    assertFalse(model.isRedTurn());

    blueView.simulateCardSelected(0);
    blueView.simulateCellSelected(0, 4);
    blueView.simulateConfirm();

    assertEquals("addPlayerListener", blueView.getLogs().get(0));
    assertEquals("userSelectCard: 0", blueView.getLogs().get(1));
    assertEquals("userSelectCell: 0, 4", blueView.getLogs().get(2));
    assertEquals("userConfirm", blueView.getLogs().get(3));

    assertEquals("onMyTurn", blueController.getCallLog().get(0));
    assertEquals("onCardSelected: 0", blueController.getCallLog().get(1));
    assertEquals("onCellSelected: 0, 4", blueController.getCallLog().get(2));
    assertEquals("onConfirm", blueController.getCallLog().get(3));


    assertTrue(model.isRedTurn());
    assertEquals("onMyTurn", redController.getCallLog().get(4));
  }


  @Test
  public void testPassingTurns() {
    model.startGame();
    redController.onMyTurn();

    redView.simulatePass();
    assertFalse(model.isRedTurn());

    blueView.simulatePass();

    assertEquals("onMyTurn", redController.getCallLog().get(0));
    assertEquals("onPass", redController.getCallLog().get(1));

    assertEquals("onMyTurn", blueController.getCallLog().get(0));
    assertEquals("onPass", blueController.getCallLog().get(1));
    assertEquals("onGameOver: Tie!", blueController.getCallLog().get(2));
  }


  @Test
  public void testInvalidMoves() {
    model.startGame();
    redController.onMyTurn();

    redView.simulateConfirm();

    assertEquals("Attempted to confirm move without selected card or cell",
            redController.getErrorLog().get(0));
    assertTrue("Should still be red's turn", model.isRedTurn());

    redView.simulateCardSelected(0);
    redView.simulateCellSelected(-1, -1);
    redView.simulateConfirm();

    assertEquals("Error confirming move: Index -1 out of bounds for length 3",
            redController.getErrorLog().get(1));
    assertTrue(model.isRedTurn());

    redView.simulateCardSelected(0);
    redView.simulateCellSelected(0, 3);
    redView.simulateConfirm();

    assertEquals("Error confirming move: The cost is more than the number of pawns",
            redController.getErrorLog().get(2));
    assertTrue(model.isRedTurn());
  }

  /**
   * Tests a complete game with machine players using a simple strategy.
   */
  @Test
  public void testMachineVsMachine() {
    IPawnsBoardModel machineModel = new PawnsBoardModelImpl(new Board(3, 5),
            redMachine, blueMachine, 5);
    MockView redMachineView = new MockView();
    MockView blueMachineView = new MockView();
    MockController redAiController = new MockController(redMachine, machineModel);
    MockController blueAiController = new MockController(blueMachine, machineModel);
    redMachineView.addPlayerListener(redAiController);
    blueMachineView.addPlayerListener(blueAiController);
    machineModel.startGame();
    redAiController.onMyTurn();

    boolean gameOverRed = false;
    boolean gameOverBlue = false;
    for (String log : redAiController.getCallLog()) {
      if (log.startsWith("onGameOver")) {
        gameOverRed = true;
        break;
      }
    }
    for (String log : blueAiController.getCallLog()) {
      if (log.startsWith("onGameOver")) {
        gameOverBlue = true;
        break;
      }
    }
    assertTrue(gameOverRed);
    assertTrue(gameOverBlue);
  }
}