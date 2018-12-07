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

  @Test
  public void PlayerSetUpTest() {
    Board b = new Board('e', 'c');
    Game g = new Game(b);
    Player p = new Player(g, b, Colour.WHITE, true);
    assertEquals("Player: SetUp colour not working correctly.", Colour.WHITE, p.getColour());
    assertEquals("Player: SetUp isPcPlayer not working correctly.", true, p.isComputerPlayer());
  }

  @Test
  public void PlayerGetAllPawnsTest() {
    Board b = new Board('e', 'c');
    Game g = new Game(b);
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
    Player p = new Player(g, b, Colour.WHITE, false);
    assertEquals("Player: getAllPawns at White Starting Position not working."
                , new Square[] { b.getSquare(0, 1), b.getSquare(1, 1), b.getSquare(2, 1)
                               , b.getSquare(3, 1), b.getSquare(5, 1), b.getSquare(6, 1), b.getSquare(7, 1)}
                , p.getAllPawns());
    Player q = new Player(g, b, Colour.BLACK, false);
    assertEquals("Player: getAllPawns at Black Starting Position not working."
                , new Square[] { b.getSquare(0, 6), b.getSquare(1, 6), b.getSquare(3, 6)
                               , b.getSquare(4, 6), b.getSquare(5, 6), b.getSquare(6, 6), b.getSquare(7, 6)}
                , q.getAllPawns());
    g.applyMove(g.parseMove("c4"));
    g.applyMove(g.parseMove("f5"));
    g.applyMove(g.parseMove("c5"));
    g.applyMove(g.parseMove("d5"));
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
    assertEquals("Player: getAllPawns not working."
                , new Square[] { b.getSquare(0, 1), b.getSquare(1, 1), b.getSquare(2, 4)
                               , b.getSquare(3, 1), b.getSquare(5, 1), b.getSquare(6, 1), b.getSquare(7, 1)}
                , p.getAllPawns());
    assertEquals("Player: getAllPawns not working."
                , new Square[] { b.getSquare(0, 6), b.getSquare(1, 6), b.getSquare(3, 4)
                               , b.getSquare(4, 6), b.getSquare(5, 4), b.getSquare(6, 6), b.getSquare(7, 6)}
                , q.getAllPawns());
    g.applyMove(g.parseMove("cxd6"));
    g.applyMove(g.parseMove("exd6"));
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
    assertEquals("Player: getAllPawns after capture not working."
                , new Square[] { b.getSquare(0, 1), b.getSquare(1, 1), b.getSquare(3, 1)
                               , b.getSquare(5, 1), b.getSquare(6, 1), b.getSquare(7, 1)}
                , p.getAllPawns());
    assertEquals("Player: getAllPawns after capture not working."
                , new Square[] { b.getSquare(0, 6), b.getSquare(1, 6), b.getSquare(3, 5)
                               , b.getSquare(5, 4), b.getSquare(6, 6), b.getSquare(7, 6)}
                , q.getAllPawns());
    
  }

  @Test
  public void PlayerGetAllValidMovesTest() {
    Board b = new Board('e', 'c');
    Game g = new Game(b);
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
    Player p = new Player(g, b, Colour.WHITE, false);
    Player q = new Player(g, b, Colour.BLACK, false);
    Move[] validMovesW1 = new Move[] {
      createMove(0, 1, 0, 2, false, false),
      createMove(0, 1, 0, 3, false, false),
      createMove(1, 1, 1, 2, false, false),
      createMove(1, 1, 1, 3, false, false),
      createMove(2, 1, 2, 2, false, false),
      createMove(2, 1, 2, 3, false, false),
      createMove(3, 1, 3, 2, false, false),
      createMove(3, 1, 3, 3, false, false),
      createMove(5, 1, 5, 2, false, false),
      createMove(5, 1, 5, 3, false, false),
      createMove(6, 1, 6, 2, false, false),
      createMove(6, 1, 6, 3, false, false),
      createMove(7, 1, 7, 2, false, false),
      createMove(7, 1, 7, 3, false, false)
    };
    Move[] validMovesW1p = p.getAllValidMoves();
    assertEquals("Player: getAllValidMoves has wrong length.", validMovesW1.length, validMovesW1p.length);
    for (int i = 0; i < validMovesW1.length; i++) {
      compareMovesEqual("Player: getAllValidMoves White Opening Move #" + (i+1), validMovesW1[i], validMovesW1p[i]);
    }
    Move[] validMovesB1 = new Move[] {
      createMove(0, 6, 0, 5, false, false),
      createMove(0, 6, 0, 4, false, false),
      createMove(1, 6, 1, 5, false, false),
      createMove(1, 6, 1, 4, false, false),
      createMove(3, 6, 3, 5, false, false),
      createMove(3, 6, 3, 4, false, false),
      createMove(4, 6, 4, 5, false, false),
      createMove(4, 6, 4, 4, false, false),
      createMove(5, 6, 5, 5, false, false),
      createMove(5, 6, 5, 4, false, false),
      createMove(6, 6, 6, 5, false, false),
      createMove(6, 6, 6, 4, false, false),
      createMove(7, 6, 7, 5, false, false),
      createMove(7, 6, 7, 4, false, false)
    };
    Move[] validMovesB1p = q.getAllValidMoves();
    assertEquals("Player: getAllValidMoves has wrong length.", validMovesB1.length, validMovesB1p.length);
    for (int i = 0; i < validMovesB1.length; i++) {
      compareMovesEqual("Player: getAllValidMoves Black Opening Move #" + (i+1), validMovesB1[i], validMovesB1p[i]);
    }
    g.applyMove(g.parseMove("c4"));
    g.applyMove(g.parseMove("f5"));
    g.applyMove(g.parseMove("c5"));
    g.applyMove(g.parseMove("d5"));
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
    Move[] validMovesW2 = new Move[] {
      createMove(0, 1, 0, 2, false, false),
      createMove(0, 1, 0, 3, false, false),
      createMove(1, 1, 1, 2, false, false),
      createMove(1, 1, 1, 3, false, false),
      createMove(2, 4, 2, 5, false, false),
      createMove(2, 4, 3, 5,  true,  true),
      createMove(3, 1, 3, 2, false, false),
      createMove(3, 1, 3, 3, false, false),
      createMove(5, 1, 5, 2, false, false),
      createMove(5, 1, 5, 3, false, false),
      createMove(6, 1, 6, 2, false, false),
      createMove(6, 1, 6, 3, false, false),
      createMove(7, 1, 7, 2, false, false),
      createMove(7, 1, 7, 3, false, false)
    };
    Move[] validMovesW2p = p.getAllValidMoves();
    assertEquals("Player: getAllValidMoves has wrong length.", validMovesW2.length, validMovesW2p.length);
    for (int i = 0; i < validMovesW2.length; i++) {
      compareMovesEqual("Player: getAllValidMoves White w/Capture #" + (i+1), validMovesW2[i], validMovesW2p[i]);
    }
    g.applyMove(g.parseMove("cxd6"));
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
    Move[] validMovesB2 = new Move[] {
      createMove(0, 6, 0, 5, false, false),
      createMove(0, 6, 0, 4, false, false),
      createMove(1, 6, 1, 5, false, false),
      createMove(1, 6, 1, 4, false, false),
      createMove(4, 6, 4, 5, false, false),
      createMove(4, 6, 4, 4, false, false),
      createMove(4, 6, 3, 5,  true, false),
      createMove(5, 4, 5, 3, false, false),
      createMove(6, 6, 6, 5, false, false),
      createMove(6, 6, 6, 4, false, false),
      createMove(7, 6, 7, 5, false, false),
      createMove(7, 6, 7, 4, false, false)
    };
    Move[] validMovesB2p = q.getAllValidMoves();
    assertEquals("Player: getAllValidMoves has wrong length.", validMovesB2.length, validMovesB2p.length);
    for (int i = 0; i < validMovesB2.length; i++) {
      compareMovesEqual("Player: getAllValidMoves Black w/Capture #" + (i+1), validMovesB2[i], validMovesB2p[i]);
    }
    g.applyMove(g.parseMove("e6"));
    g.applyMove(g.parseMove("d7"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b b . w . . b b  7
        5 6  . . . . b . . .  6
        4 5  . . . . . b . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w w  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    Move[] validMovesW3 = new Move[] {
      createMove(0, 1, 0, 2, false, false),
      createMove(0, 1, 0, 3, false, false),
      createMove(1, 1, 1, 2, false, false),
      createMove(1, 1, 1, 3, false, false),
      createMove(3, 1, 3, 2, false, false),
      createMove(3, 1, 3, 3, false, false),
      createMove(3, 6, 3, 7, false, false),
      createMove(5, 1, 5, 2, false, false),
      createMove(5, 1, 5, 3, false, false),
      createMove(6, 1, 6, 2, false, false),
      createMove(6, 1, 6, 3, false, false),
      createMove(7, 1, 7, 2, false, false),
      createMove(7, 1, 7, 3, false, false)
    };
    Move[] validMovesW3p = p.getAllValidMoves();
    assertEquals("Player: getAllValidMoves has wrong length.", validMovesW3.length, validMovesW3p.length);
    for (int i = 0; i < validMovesW3.length; i++) {
      compareMovesEqual("Player: getAllValidMoves White w/2ndLastLine #" + (i+1), validMovesW3[i], validMovesW3p[i]);
    }
  }

  @Test
  public void PlayerisPassedPawnTest() {
    Board b = new Board('e', 'c');
    Game g = new Game(b);
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
    Player p = new Player(g, b, Colour.WHITE, false);
    Player q = new Player(g, b, Colour.BLACK, false);
    assertFalse("Player: isPassedPawn gives True for non-passed white pawn.", p.isPassedPawn(b.getSquare(2, 1)));
    assertFalse("Player: isPassedPawn gives True for non-passed white pawn.", p.isPassedPawn(b.getSquare(7, 1)));
    assertFalse("Player: isPassedPawn gives True for non-passed black pawn.", q.isPassedPawn(b.getSquare(6, 6)));
    assertFalse("Player: isPassedPawn gives True for non-passed black pawn.", q.isPassedPawn(b.getSquare(0, 6)));
    assertFalse("Player: isPassedPawn gives True for empty square.", p.isPassedPawn(b.getSquare(4, 1)));
    assertFalse("Player: isPassedPawn gives True for empty square.", q.isPassedPawn(b.getSquare(2, 6)));
    assertFalse("Player: isPassedPawn gives True for empty square.", p.isPassedPawn(b.getSquare(3, 3)));
    assertFalse("Player: isPassedPawn gives True for empty square.", q.isPassedPawn(b.getSquare(3, 3)));
    assertFalse("Player: isPassedPawn gives True for empty square.", p.isPassedPawn(b.getSquare(7, 7)));
    assertFalse("Player: isPassedPawn gives True for empty square.", q.isPassedPawn(b.getSquare(0, 0)));
    g.applyMove(g.parseMove("c4"));
    g.applyMove(g.parseMove("b5"));
    g.applyMove(g.parseMove("c5"));
    g.applyMove(g.parseMove("e5"));
    g.applyMove(g.parseMove("h3"));
    g.applyMove(g.parseMove("d6"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b . . . . b b b  7
        5 6  . . . b . . . .  6
        4 5  . b w . b . . .  5
        3 4  . . . . . . . .  4
        2 3  . . . . . . . w  3
        1 2  w w . w . w w .  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    assertFalse("Player: isPassedPawn gives True for half-passed white pawn.", p.isPassedPawn(b.getSquare(2, 4)));
    g.applyMove(g.parseMove("h4"));
    g.applyMove(g.parseMove("d5"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b . . . . b b b  7
        5 6  . . . . . . . .  6
        4 5  . b W b b . . .  5
        3 4  . . . . . . . w  4
        2 3  . . . . . . . .  3
        1 2  w w . w . w w .  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    assertTrue("Player: isPassedPawn gives False for inline passed white pawn.", p.isPassedPawn(b.getSquare(2, 4)));
    g.applyMove(g.parseMove("d4"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b . . . . b b b  7
        5 6  . . . . . . . .  6
        4 5  . b W b b . . .  5
        3 4  . . . w . . . w  4
        2 3  . . . . . . . .  3
        1 2  w w . . . w w .  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    assertFalse("Player: isPassedPawn gives True for half-passed black pawn.", q.isPassedPawn(b.getSquare(3, 4)));
    g.applyMove(g.parseMove("exd4"));
    /*       A B C D E F G H
        7 8  . . . . . . . .  8
        6 7  b . . . . b b b  7
        5 6  . . . . . . . .  6
        4 5  . b W b . . . .  5
        3 4  . . . B . . . w  4
        2 3  . . . . . . . .  3
        1 2  w w . . . w w .  2
        0 1  . . . . . . . .  1
             A B C D E F G H
             0 1 2 3 4 5 6 7
    */
    assertTrue("Player: isPassedPawn gives False for passed black pawn.", q.isPassedPawn(b.getSquare(3, 3)));
  }

  @Test
  public void PlayerMakeMoveTest() {
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
    Player p = new Player(game, board, Colour.WHITE, true);
    Player q = new Player(game, board, Colour.BLACK, true);
    Move[] validMovesP;
    Move[] validMovesQ;
    for (int turn = 1; turn < 11; turn++) {
      validMovesP = p.getAllValidMoves();
      p.makeMove();
      assertTrue("Player: applyMove p" + turn + " did not apply a valid move.", Util.MovesContains(validMovesP, game.getLastMove()));
      validMovesQ = q.getAllValidMoves();
      q.makeMove();
      assertTrue("Player: applyMove q" + turn + " did not apply a valid move.", Util.MovesContains(validMovesQ, game.getLastMove()));
    }
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

  private Move createMove(int a, int b, int c, int d, boolean capture, boolean enPassant) {
    return new Move(new Square(a, b), new Square(c, d), capture, enPassant);
  }
}