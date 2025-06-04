package cs3500.pawnsboard.strategy;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.Deck;
import cs3500.pawnsboard.model.HumanPlayer;
import cs3500.pawnsboard.model.ICard;
import cs3500.pawnsboard.model.IPlayer;
import cs3500.pawnsboard.model.InfluenceGrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Tests that FillFirstStrategy inspects moves in order and stops at the first legal one.
 */
public class StrategyTest {
  InfluenceGrid grid;
  ICard cardA;
  ICard cardB;
  ICard cardC;
  ICard cardD;
  IPlayer red;
  IPlayer blue;
  MockModel model;
  IStrategy fillFirstStrategy;
  IStrategy maxScoreStrategy;

  @Before
  public void setUp() throws Exception {
    grid = new InfluenceGrid(new String[] {
        "XXXXX",
        "XXIXX",
        "XICIX",
        "XXIXX",
        "XXXXX"
    });

    cardA = new Card("A", 1, 100, grid);
    cardB = new Card("B", 1, 2, grid);
    cardC = new Card("C", 1, 5, grid);
    cardD = new Card("D", 1, 1, grid);

    red = new HumanPlayer("Red",
            new Deck(List.of(cardA, cardB, cardC, cardD)), 1);
    red.drawCard();
    red.drawCard();

    blue = new HumanPlayer("Blue",
            new Deck(List.of(cardA, cardB, cardC, cardD)), 1);
    blue.drawCard();
    blue.drawCard();

    model = new MockModel(3, 5, red, blue);

    fillFirstStrategy = new FillFirstStrategy();
    maxScoreStrategy = new MaximizeRowScoreStrategy();
  }

  @Test
  public void testFirstPossibleMove() {
    model.allowMove(cardA, 0, 4, false);
    Move move = fillFirstStrategy.chooseMove(model, blue);

    assertEquals("A", move.getCard().getName());
    assertEquals(0, move.getRow());
    assertEquals(4, move.getCol());

    List<String> log = model.getLog();
    assertTrue(log.contains("getPlayerHand for Blue"));
    assertTrue(log.contains("checked isMoveLegal at 0,0 for card A for player Blue"));
    assertTrue(log.contains("checked isMoveLegal at 0,1 for card A for player Blue"));
    assertTrue(log.contains("checked isMoveLegal at 0,2 for card A for player Blue"));
    assertTrue(log.contains("checked isMoveLegal at 0,3 for card A for player Blue"));
    assertTrue(log.contains("checked isMoveLegal at 0,4 for card A for player Blue"));
    assertFalse(log.contains("checked isMoveLegal at 1,0 for card A for player Blue"));
    assertEquals("Place card: A on (0, 4)", move.toString());
  }

  @Test
  public void testPass() {
    Move move = fillFirstStrategy.chooseMove(model, red);

    assertEquals(null, move.getCard());

    List<String> log = model.getLog();
    assertTrue(log.contains("getPlayerHand for Red"));
    assertTrue(log.contains("checked isMoveLegal at 0,0 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 0,1 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 0,2 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 0,3 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 0,4 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 1,0 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 1,1 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 1,2 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 1,3 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 1,4 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 2,0 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 2,1 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 2,2 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 2,3 for card A for player Red"));
    assertTrue(log.contains("checked isMoveLegal at 2,4 for card A for player Red"));
    assertEquals("Pass", move.toString());
  }

  @Test
  public void testLogsRowCheckAndScoreFlipping() {
    model.setRowScore(red, 0, 5);
    model.setRowScore(blue, 0, 2);
    model.setRowScore(red, 1, 3);
    model.setRowScore(blue, 1, 8);
    model.setRowScore(red, 1, 4);
    model.setRowScore(blue, 1, 7);

    // red is winning row 0, so strategy should skip row 0 and choose row 1
    model.allowMove(cardA, 0, 0, true);
    model.allowMove(cardA,1, 0, true);

    Move move = maxScoreStrategy.chooseMove(model, red);

    assertEquals("A", move.getCard().getName());
    assertEquals(1, move.getRow());
    assertEquals(0, move.getCol());

    List<String> log = model.getLog();

    assertTrue(log.contains("getPlayerHand for Red"));
    assertTrue(log.contains("checked scoreAtRow for Red at row 0"));
    assertTrue(log.contains("checked scoreAtRow for Blue at row 0"));
    assertTrue(log.contains("checked scoreAtRow for Red at row 1"));
    assertTrue(log.contains("checked scoreAtRow for Blue at row 1"));
    assertTrue(log.contains("checked isMoveLegal at 1,0 for card A for player Red"));
    assertFalse(log.contains("checked scoreAtRow for Red at row 2"));
    assertFalse(log.contains("checked scoreAtRow for Blue at row 2"));
    assertEquals("Place card: A on (1, 0)", move.toString());
  }

  @Test
  public void testPassForMax() {
    model.setRowScore(red, 0, 5);
    model.setRowScore(blue, 0, 2);
    model.setRowScore(red, 1, 10);
    model.setRowScore(blue, 1, 8);
    model.setRowScore(red, 2, 10);
    model.setRowScore(blue, 2, 7);

    // red is winning all three rows
    model.allowMove(cardA, 0, 0, true);
    model.allowMove(cardA,1, 0, true);
    model.allowMove(cardA,2, 0, true);

    Move move = maxScoreStrategy.chooseMove(model, red);

    assertEquals(null, move.getCard());

    List<String> log = model.getLog();

    assertTrue(log.contains("getPlayerHand for Red"));
    assertTrue(log.contains("checked scoreAtRow for Red at row 0"));
    assertTrue(log.contains("checked scoreAtRow for Blue at row 0"));
    assertTrue(log.contains("checked scoreAtRow for Red at row 1"));
    assertTrue(log.contains("checked scoreAtRow for Blue at row 1"));
    assertTrue(log.contains("checked scoreAtRow for Red at row 2"));
    assertTrue(log.contains("checked scoreAtRow for Blue at row 2"));
    assertFalse(log.contains("checked isMoveLegal at 0,0 for card A for player Red"));
    assertFalse(log.contains("checked isMoveLegal at 1,0 for card A for player Red"));
    assertFalse(log.contains("checked isMoveLegal at 2,0 for card A for player Red"));
    assertEquals("Pass", move.toString());
  }

}
