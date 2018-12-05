import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnRaceTest {

  @Test
  public void UtilTest() {
    assertEquals("Util: XtoCol not working correctly.", 'a', Util.XtoCol(0));
    assertEquals("Util: XtoCol not working correctly.", 'd', Util.XtoCol(3));
    assertEquals("Util: XtoCol not working correctly.", 'h', Util.XtoCol(7));
    assertEquals("Util: ColtoX not working correctly.", 0, Util.ColtoX('a'));
    assertEquals("Util: ColtoX not working correctly.", 3, Util.ColtoX('d'));
    assertEquals("Util: ColtoX not working correctly.", 7, Util.ColtoX('h'));
    assertEquals("Util: YtoRow not working correctly.", 1, Util.YtoRow(0));
    assertEquals("Util: YtoRow not working correctly.", 4, Util.YtoRow(3));
    assertEquals("Util: YtoRow not working correctly.", 8, Util.YtoRow(7));
    assertEquals("Util: RowtoY not working correctly.", 0, Util.RowtoY(1));
    assertEquals("Util: RowtoY not working correctly.", 3, Util.RowtoY(4));
    assertEquals("Util: RowtoY not working correctly.", 7, Util.RowtoY(8));
  }
  @Test
  public void ColourTest() {
    Colour col = Colour.BLACK;
    assertEquals("Colour: Colour Rep not set or get correctly.", 'B', col.colourRep());
    assertEquals("Colour: Opponent Colour not set or get correctly.", Colour.WHITE, col.opponentColour());
    Colour dot = Colour.NONE;
    assertEquals("Colour: Colour Rep not set or get correctly.", '.', dot.colourRep());
    assertEquals("Colour: Opponent Colour not set or get correctly.", Colour.NONE, dot.opponentColour());
  }
  
  @Test
  public void SquareTest() {
    Square sq = new Square(4, 5);
    assertEquals("Square: x-coordinate set or get incorrectly.", 4, sq.getX());
    assertEquals("Square: y-coordinate set or get incorrectly.", 5, sq.getY());
    assertEquals("Square: getCol unsuccessful.",  'e', sq.getCol());
    assertEquals("Square: getRow unsuccessful.",  6, sq.getRow());
    assertEquals("Square: colour not defaulted to None.", Colour.NONE, sq.occupiedBy());
    sq.setOccupier(Colour.WHITE);
    assertEquals("Square: colour not set correctly.", Colour.WHITE, sq.occupiedBy());
    
  }
  
  @Test
  public void MoveTest() {
    Square s1 = new Square(4, 4);
    Square s2 = new Square(4, 5);
    Move mv = new Move(s1, s2, false, false);
    assertEquals("Move: from square set or get incorrectly.", s1, mv.getFrom());
    assertEquals("Move: to square set or get incorrectly.", s2, mv.getTo());
    assertEquals("Move: isCapture set or get incorrectly.", false, mv.isCapture());
    assertEquals("Move: isEnPassantCapture set or get incorrectly.", false, mv.isEnPassantCapture());
    assertEquals("Move: standard move SAN not retrieved correctly.", "e6", mv.getSAN());
    Square s3 = new Square(3, 5);
    Move mv2 = new Move(s1, s3, true, false);
    assertEquals("Move: isCapture set or get incorrectly.", true, mv2.isCapture());
    assertEquals("Move: isEnPassantCapture set or get incorrectly.", false, mv2.isEnPassantCapture());
    assertEquals("Move: capture move SAN not retrieved correctly.", "exd6", mv2.getSAN());
    Square s4 = new Square(5, 5);
    Move mv3 = new Move(s1, s4, true, true);
    assertEquals("Move: isCapture set or get incorrectly.", true, mv3.isCapture());
    assertEquals("Move: isEnPassantCapture set or get incorrectly.", true, mv3.isEnPassantCapture());
    assertEquals("Move: capture move SAN not retrieved correctly.", "exf6", mv3.getSAN());
  }

  @Test
  public void BoardSetUpTest() {
    Board board = new Board('e', 'c');
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b b b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . . . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w w w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    assertEquals("Board: white empty square not set properly.", Colour.NONE, board.getSquare(4, 1).occupiedBy());
    assertEquals("Board: white empty square not set properly.", Colour.NONE, board.getSquare(2, 6).occupiedBy());
    for (int i = 0; i < 8; i++) {
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 0).occupiedBy());
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 2).occupiedBy());
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 3).occupiedBy());
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 4).occupiedBy());
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 5).occupiedBy());
      assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(i, 7).occupiedBy());
      if (i != 4) {
        assertEquals("Board: white starting square not set properly.", Colour.WHITE, board.getSquare(i, 1).occupiedBy());
      }
      if (i != 2) {
        assertEquals("Board: black starting square not set properly.", Colour.BLACK, board.getSquare(i, 6).occupiedBy());
      }
    }    
  }

  @Test
  public void BoardApplyMoveTest() {
    // Test two standard moves, an EnPassant capture, and a standard capture
    Board board = new Board('e', 'c');
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b b b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . . . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w w w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw1 = board.getSquare(2, 1);
    Square stw1 = board.getSquare(2, 3);
    Move mw1 = new Move(sfw1, stw1, false, false);
    board.applyMove(mw1);
    Square sfb1 = board.getSquare(5, 6);
    Square stb1 = board.getSquare(5, 4);
    Move mb1 = new Move(sfb1, stb1, false, false);
    board.applyMove(mb1);
    assertEquals("Board: applyMove white1 not successful.", Colour.WHITE, board.getSquare(2, 3).occupiedBy());
    assertEquals("Board: applyMove white1 not successful.", Colour.NONE, board.getSquare(2, 1).occupiedBy());
    assertEquals("Board: applyMove black1 not successful.", Colour.BLACK, board.getSquare(5, 4).occupiedBy());
    assertEquals("Board: applyMove black1 not successful.", Colour.NONE, board.getSquare(5, 6).occupiedBy());
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b . b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . w . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw2 = board.getSquare(2, 3);
    Square stw2 = board.getSquare(2, 4);
    Move mw2 = new Move(sfw2, stw2, false, false);
    board.applyMove(mw2);
    Square sfb2 = board.getSquare(3, 6);
    Square stb2 = board.getSquare(3, 4);
    Move mb2 = new Move(sfb2, stb2, false, false);
    board.applyMove(mb2);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . . . . . .  6
        4 5  . . w b . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw3 = board.getSquare(2, 4);
    Square stw3 = board.getSquare(3, 5);
    Move mw3 = new Move(sfw3, stw3, true, true);
    board.applyMove(mw3);
    assertEquals("Board: applyMove white3 EnPassant unsuccessful.", Colour.WHITE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: applyMove white3 EnPassant unsuccessful.", Colour.NONE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: applyMove white3 EnPassant unsuccessful.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . w . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfb3 = board.getSquare(4, 6);
    Square stb3 = board.getSquare(3, 5);
    Move mb3 = new Move(sfb3, stb3, true, false);
    board.applyMove(mb3);
    assertEquals("Board: applyMove black3 Capture not successful.", Colour.BLACK, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: applyMove black3 Capture not successful.", Colour.NONE, board.getSquare(4, 6).occupiedBy());
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . . . b b  7
        5 6  . . . b . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
  }

  @Test
  public void BoardUnApplyMoveTest() {
    // Test two standard moves, an EnPassant capture, and a standard capture
    Board board = new Board('e', 'c');
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b b b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . . . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w w w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw1 = board.getSquare(2, 1);
    Square stw1 = board.getSquare(2, 3);
    Move mw1 = new Move(sfw1, stw1, false, false);
    board.applyMove(mw1);
    Square sfb1 = board.getSquare(5, 6);
    Square stb1 = board.getSquare(5, 4);
    Move mb1 = new Move(sfb1, stb1, false, false);
    board.applyMove(mb1);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b . b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . w . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw2 = board.getSquare(2, 3);
    Square stw2 = board.getSquare(2, 4);
    Move mw2 = new Move(sfw2, stw2, false, false);
    board.applyMove(mw2);
    Square sfb2 = board.getSquare(3, 6);
    Square stb2 = board.getSquare(3, 4);
    Move mb2 = new Move(sfb2, stb2, false, false);
    board.applyMove(mb2);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . . . . . .  6
        4 5  . . w b . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfw3 = board.getSquare(2, 4);
    Square stw3 = board.getSquare(3, 5);
    Move mw3 = new Move(sfw3, stw3, true, true);
    board.applyMove(mw3);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . w . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Square sfb3 = board.getSquare(4, 6);
    Square stb3 = board.getSquare(3, 5);
    Move mb3 = new Move(sfb3, stb3, true, false);
    board.applyMove(mb3);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . w . b b  7
        5 6  . . . b . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    
    board.unapplyMove(mb3);
    assertEquals("Board: unapplyMove black3 Capture not successful.", Colour.WHITE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: unapplyMove black3 Capture not successful.", Colour.BLACK, board.getSquare(4, 6).occupiedBy());
    board.unapplyMove(mw3);
    assertEquals("Board: unapplyMove white3 EnPassant unsuccessful.", Colour.NONE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: unapplyMove white3 EnPassant unsuccessful.", Colour.WHITE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: unapplyMove white3 EnPassant unsuccessful.", Colour.BLACK, board.getSquare(3, 4).occupiedBy());
    board.unapplyMove(mb2);
    assertEquals("Board: unapplyMove black2 not successful.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    assertEquals("Board: unapplyMove black2 not successful.", Colour.BLACK, board.getSquare(3, 6).occupiedBy());
    board.unapplyMove(mw2);
    assertEquals("Board: unapplyMove white2 not successful.", Colour.NONE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: unapplyMove white2 not successful.", Colour.WHITE, board.getSquare(2, 3).occupiedBy());
    board.unapplyMove(mb1);
    assertEquals("Board: unapplyMove black1 not successful.", Colour.NONE, board.getSquare(5, 4).occupiedBy());
    assertEquals("Board: unapplyMove black1 not successful.", Colour.BLACK, board.getSquare(5, 6).occupiedBy());
    board.unapplyMove(mw1);
    assertEquals("Board: unapplyMove white1 not successful.", Colour.NONE, board.getSquare(2, 3).occupiedBy());
    assertEquals("Board: unapplyMove white1 not successful.", Colour.WHITE, board.getSquare(2, 1).occupiedBy());
  }

  @Test
  public void BoardDisplayTest() {
    /* 
          A B C D E F G H
      7 8 . . . . . . . . 8
      6 7 B B . B B B B B 7
      5 6 . . . . . . . . 6
      4 5 . . . . . . . . 5
      3 4 . . . . . . . . 4
      2 3 . . . . . . . . 3
      1 2 W W W W . W W W 2
      0 1 . . . . . . . . 1
          A B C D E F G H
          0 1 2 3 4 5 6 7
    */
    Board board = new Board('e', 'c');
    assertEquals( "Board: Display method not working as expected."
                , "  A B C D E F G H\n"
                + "8 . . . . . . . . 8\n"
                + "7 B B . B B B B B 7\n"
                + "6 . . . . . . . . 6\n"
                + "5 . . . . . . . . 5\n"
                + "4 . . . . . . . . 4\n"
                + "3 . . . . . . . . 3\n"
                + "2 W W W W . W W W 2\n"
                + "1 . . . . . . . . 1\n"
                + "  A B C D E F G H"
                , board.genDisplayString());
    Square sf = board.getSquare(2, 1);
    Square st = board.getSquare(2, 3);
    Move m = new Move(sf, st, false, false);
    board.applyMove(m);
    sf = board.getSquare(5, 6);
    st = board.getSquare(5, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    sf = board.getSquare(2, 3);
    st = board.getSquare(2, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    sf = board.getSquare(3, 6);
    st = board.getSquare(3, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    assertEquals( "Board: Display method not working as expected."
                , "  A B C D E F G H\n"
                + "8 . . . . . . . . 8\n"
                + "7 B B . . B . B B 7\n"
                + "6 . . . . . . . . 6\n"
                + "5 . . W B . B . . 5\n"
                + "4 . . . . . . . . 4\n"
                + "3 . . . . . . . . 3\n"
                + "2 W W . W . W W W 2\n"
                + "1 . . . . . . . . 1\n"
                + "  A B C D E F G H"
                , board.genDisplayString());
  }

  @Test
  public void GameApplyUnapplyTest() {
    Board board = new Board('e', 'c');
    Game game = new Game(board);
    assertEquals("Game: set up not successful.", Colour.WHITE, game.getCurrentPlayer());
    assertEquals("Game: set up not successful.", null, game.getLastMove());

    Square sfw1 = board.getSquare(2, 1);
    Square stw1 = board.getSquare(2, 3);
    Move mw1 = new Move(sfw1, stw1, false, false);
    game.applyMove(mw1);
    assertEquals("Game: player turn switching not working.", Colour.BLACK, game.getCurrentPlayer());
    Square sfb1 = board.getSquare(5, 6);
    Square stb1 = board.getSquare(5, 4);
    Move mb1 = new Move(sfb1, stb1, false, false);
    game.applyMove(mb1);
    compareMovesEqual("Game: getLastMove mb1", mb1, game.getLastMove());
    assertEquals("Game: player turn switching not working.", Colour.WHITE, game.getCurrentPlayer());
    Square sfw2 = board.getSquare(2, 3);
    Square stw2 = board.getSquare(2, 4);
    Move mw2 = new Move(sfw2, stw2, false, false);
    game.applyMove(mw2);
    compareMovesEqual("Game: getLastMove mw2", mw2, game.getLastMove());
    assertEquals("Game: player turn switching not working.", Colour.BLACK, game.getCurrentPlayer());

    game.unapplyMove();
    compareMovesEqual("Game: unapplyMove mw2", mb1, game.getLastMove());
    assertEquals("Game: player turn switching after unapply not working.", Colour.WHITE, game.getCurrentPlayer());
    game.unapplyMove();
    compareMovesEqual("Game: unapplyMove mb1", mw1, game.getLastMove());
    assertEquals("Game: player turn switching after unapply not working.", Colour.BLACK, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapplyMove mw1 not working.", null, game.getLastMove());
    assertEquals("Game: player turn switching after unapply not working.", Colour.WHITE, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapplyMove at starting position not working.", null, game.getLastMove());
    assertEquals("Game: unapplyMove at starting position not working.", Colour.WHITE, game.getCurrentPlayer());
  }

  @Test
  public void GameParseMoveTest() {
    Board board = new Board('e', 'c');
    Game game = new Game(board);
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . b b b b b  7
        5 6  . . . . . . . .  6
        4 5  . . . . . . . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w w w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    compareMovesEqual("Game: parseMove standard-white-one", new Move(new Square(1, 1), new Square(1, 2), false, false), game.parseMove("b3"));
    compareMovesEqual("Game: parseMove standard-white-two", new Move(new Square(7, 1), new Square(7, 3), false, false), game.parseMove("h4"));
    game.applyMove(game.parseMove("c4"));
    compareMovesEqual("Game: parseMove standard-black-one", new Move(new Square(7, 6), new Square(7, 5), false, false), game.parseMove("h6"));
    compareMovesEqual("Game: parseMove standard-black-two", new Move(new Square(3, 6), new Square(3, 4), false, false), game.parseMove("d5"));
    game.applyMove(game.parseMove("f5"));
    game.applyMove(game.parseMove("c5"));
    game.applyMove(game.parseMove("d5"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . . . . . .  6
        4 5  . . w b . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    // We are now ready for an EnPassant Capture
    compareMovesEqual("Game: parseMove enpassant-white-x-black", new Move(new Square(2, 4), new Square(3, 5), true, true), game.parseMove("cxd6"));
    game.applyMove(game.parseMove("cxd6"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . b . b b  7
        5 6  . . . w . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    // We are now ready for a regular capture
    compareMovesEqual("Game: parseMove capture-black-x-white", new Move(new Square(4, 6), new Square(3, 5), true, false), game.parseMove("exd6"));
    game.applyMove(game.parseMove("exd6"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . . . . b b  7
        5 6  . . . b . . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
  }

  private void compareMovesEqual(String msg, Move m1, Move m2) {
    if (m1 == null || m2 == null) {
      assertTrue(msg + " has a null move.", false);
    }
    else {
      assertEquals(msg + " fromX not matching.", m1.getFrom().getX(), m2.getFrom().getX());
      assertEquals(msg + " fromY not matching.", m1.getFrom().getY(), m2.getFrom().getY());
      assertEquals(msg + " toX not matching.", m1.getTo().getX(), m2.getTo().getX());
      assertEquals(msg + " toY not matching.", m1.getTo().getY(), m2.getTo().getY());
      assertEquals(msg + " isCapture not matching", m1.isCapture(), m2.isCapture());
      assertEquals(msg + " isEnPassant not matching", m1.isEnPassantCapture(), m2.isEnPassantCapture());
    }
  }
}