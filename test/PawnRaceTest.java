import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnRaceTest {

  @Test
  public void SquareTest() {
    Square sq = new Square(4, 5);
    assertEquals("Square: x-coordinate set or get incorrectly.", 4, sq.getX());
    assertEquals("Square: y-coordinate set or get incorrectly.", 5, sq.getY());
    assertEquals("Square: get san form of column unsuccessful.",  'e', sq.getColSAN());
    assertEquals("Square: get san form of row unsuccessful.",  6, sq.getRowSAN());
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
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  b  b  b  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  .  .  .  .  .  .  5
        3  4  .  .  .  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  w  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(0, 0).occupiedBy());
    assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    assertEquals("Board: empty squares not set properly.", Colour.NONE, board.getSquare(7, 7).occupiedBy());
    assertEquals("Board: black starting square not set properly.", Colour.BLACK, board.getSquare(0, 6).occupiedBy());
    assertEquals("Board: black starting square not set properly.", Colour.BLACK, board.getSquare(4, 6).occupiedBy());
    assertEquals("Board: black starting square not set properly.", Colour.BLACK, board.getSquare(7, 6).occupiedBy());
    assertEquals("Board: black empty square not set properly.", Colour.NONE, board.getSquare(2, 6).occupiedBy());
    assertEquals("Board: white starting square not set properly.", Colour.WHITE, board.getSquare(0, 1).occupiedBy());
    assertEquals("Board: white starting square not set properly.", Colour.WHITE, board.getSquare(2, 1).occupiedBy());
    assertEquals("Board: white starting square not set properly.", Colour.WHITE, board.getSquare(7, 1).occupiedBy());
    assertEquals("Board: white empty square not set properly.", Colour.NONE, board.getSquare(4, 1).occupiedBy());
  }

  @Test
  public void BoardApplyMoveTest() {
    Board board = new Board('e', 'c');
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  b  b  b  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  .  .  .  .  .  .  5
        3  4  .  .  .  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  w  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    Square sf = board.getSquare(2, 1);
    Square st = board.getSquare(2, 3);
    Move m = new Move(sf, st, false, false);
    board.applyMove(m);
    assertEquals("Board: apply white opening move not successful.", Colour.WHITE, board.getSquare(2, 3).occupiedBy());
    assertEquals("Board: apply white opening move not successful.", Colour.NONE, board.getSquare(2, 1).occupiedBy());
    sf = board.getSquare(5, 6);
    st = board.getSquare(5, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    assertEquals("Board: apply black opening move not successful.", Colour.BLACK, board.getSquare(5, 4).occupiedBy());
    assertEquals("Board: apply black opening move not successful.", Colour.NONE, board.getSquare(5, 6).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  b  b  -  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  .  .  .  b'  .  .  5
        3  4  .  .  w' .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    sf = board.getSquare(2, 3);
    st = board.getSquare(2, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    assertEquals("Board: apply white move not successful.", Colour.WHITE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: apply white move not successful.", Colour.NONE, board.getSquare(2, 3).occupiedBy());
    sf = board.getSquare(3, 6);
    st = board.getSquare(3, 4);
    m = new Move(sf, st, false, false);
    board.applyMove(m);
    assertEquals("Board: apply black move not successful.", Colour.BLACK, board.getSquare(3, 4).occupiedBy());
    assertEquals("Board: apply black move not successful.", Colour.NONE, board.getSquare(3, 6).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  -  b  -  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  w' b' .  b' .  .  5
        3  4  .  .  -  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    sf = board.getSquare(2, 4);
    st = board.getSquare(3, 5);
    m = new Move(sf, st, true, true);
    board.applyMove(m);
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.WHITE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.NONE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  -  b  -  b  b  7
        5  6  .  .  .  w' .  .  .  .  6
        4  5  .  .  -  -  .  b'  .  .  5
        3  4  .  .  -  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    sf = board.getSquare(4, 6);
    st = board.getSquare(3, 5);
    m = new Move(sf, st, true, false);
    board.applyMove(m);
    assertEquals("Board: apply capture by black move not successful.", Colour.BLACK, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: apply capture by black move not successful.", Colour.NONE, board.getSquare(4, 6).occupiedBy());
  }

  @Test
  public void BoardUnApplyMoveTest() {
    Board board = new Board('e', 'c');
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  b  b  b  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  .  .  .  .  .  .  5
        3  4  .  .  .  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  w  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    Square sf1 = board.getSquare(2, 1);
    Square st1 = board.getSquare(2, 3);
    Move m1 = new Move(sf1, st1, false, false);
    board.applyMove(m1);
    assertEquals("Board: apply white opening move not successful.", Colour.WHITE, board.getSquare(2, 3).occupiedBy());
    assertEquals("Board: apply white opening move not successful.", Colour.NONE, board.getSquare(2, 1).occupiedBy());
    Square sf2 = board.getSquare(5, 6);
    Square st2 = board.getSquare(5, 4);
    Move m2 = new Move(sf2, st2, false, false);
    board.applyMove(m2);
    assertEquals("Board: apply black opening move not successful.", Colour.BLACK, board.getSquare(5, 4).occupiedBy());
    assertEquals("Board: apply black opening move not successful.", Colour.NONE, board.getSquare(5, 6).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  b  b  -  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  .  .  .  b'  .  .  5
        3  4  .  .  w' .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    Square sf3 = board.getSquare(2, 3);
    Square st3 = board.getSquare(2, 4);
    Move m3 = new Move(sf3, st3, false, false);
    board.applyMove(m3);
    assertEquals("Board: apply white move not successful.", Colour.WHITE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: apply white move not successful.", Colour.NONE, board.getSquare(2, 3).occupiedBy());
    Square sf4 = board.getSquare(3, 6);
    Square st4 = board.getSquare(3, 4);
    Move m4 = new Move(sf4, st4, false, false);
    board.applyMove(m4);
    assertEquals("Board: apply black move not successful.", Colour.BLACK, board.getSquare(3, 4).occupiedBy());
    assertEquals("Board: apply black move not successful.", Colour.NONE, board.getSquare(3, 6).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  -  b  -  b  b  7
        5  6  .  .  .  .  .  .  .  .  6
        4  5  .  .  w' b' .  b'  .  .  5
        3  4  .  .  -  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    Square sf5 = board.getSquare(2, 4);
    Square st5 = board.getSquare(3, 5);
    Move m5 = new Move(sf5, st5, true, true);
    board.applyMove(m5);
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.WHITE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.NONE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: apply enpassant move unsuccessful.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    /*        A  B  C  D  E  F  G  H
        7  8  .  .  .  .  .  .  .  .  8
        6  7  b  b  .  -  b  -  b  b  7
        5  6  .  .  .  w' .  .  .  .  6
        4  5  .  .  -  -  .  b'  .  .  5
        3  4  .  .  -  .  .  .  .  .  4
        2  3  .  .  .  .  .  .  .  .  3
        1  2  w  w  -  w  .  w  w  w  2
        0  1  .  .  .  .  .  .  .  .  1
              A  B  C  D  E  F  G  H
              0  1  2  3  4  5  6  7
    */
    Square sf6 = board.getSquare(4, 6);
    Square st6 = board.getSquare(3, 5);
    Move m6 = new Move(sf6, st6, true, false);
    board.applyMove(m6);
    assertEquals("Board: apply capture by black move not successful.", Colour.BLACK, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: apply capture by black move not successful.", Colour.NONE, board.getSquare(4, 6).occupiedBy());
    
    board.unapplyMove(m6);
    assertEquals("Board: unapply capture by black move not successful.", Colour.WHITE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: unapply capture by black move not successful.", Colour.BLACK, board.getSquare(4, 6).occupiedBy());
    board.unapplyMove(m5);
    assertEquals("Board: unapply enpassant move unsuccessful.", Colour.NONE, board.getSquare(3, 5).occupiedBy());
    assertEquals("Board: unapply enpassant move unsuccessful.", Colour.WHITE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: unapply enpassant move unsuccessful.", Colour.BLACK, board.getSquare(3, 4).occupiedBy());
    board.unapplyMove(m4);
    assertEquals("Board: unapply black move not successful.", Colour.NONE, board.getSquare(3, 4).occupiedBy());
    assertEquals("Board: unapply black move not successful.", Colour.BLACK, board.getSquare(3, 6).occupiedBy());
    board.unapplyMove(m3);
    assertEquals("Board: unapply white move not successful.", Colour.NONE, board.getSquare(2, 4).occupiedBy());
    assertEquals("Board: unapply white move not successful.", Colour.WHITE, board.getSquare(2, 3).occupiedBy());
    board.unapplyMove(m2);
    assertEquals("Board: unapply black opening move not successful.", Colour.NONE, board.getSquare(5, 4).occupiedBy());
    assertEquals("Board: unapply black opening move not successful.", Colour.BLACK, board.getSquare(5, 6).occupiedBy());
    board.unapplyMove(m1);
    assertEquals("Board: unapply white opening move not successful.", Colour.NONE, board.getSquare(2, 3).occupiedBy());
    assertEquals("Board: unapply white opening move not successful.", Colour.WHITE, board.getSquare(2, 1).occupiedBy());
    
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
  public void GameTest() {
    Board board = new Board('e', 'c');
    Game game = new Game(board);
    assertEquals("Game: set up not successful.", Colour.WHITE, game.getCurrentPlayer());
    assertEquals("Game: set up not successful.", null, game.getLastMove());
    Square sf1 = board.getSquare(2, 1);
    Square st1 = board.getSquare(2, 3);
    Move m1 = new Move(sf1, st1, false, false);
    game.applyMove(m1);
    assertEquals("Game: get last move after white opens unsuccessful.", m1, game.getLastMove());
    assertEquals("Game: apply white opening move unsuccessful.", Colour.BLACK, game.getCurrentPlayer());
    Square sf2 = board.getSquare(5, 6);
    Square st2 = board.getSquare(5, 4);
    Move m2 = new Move(sf2, st2, false, false);
    game.applyMove(m2);
    assertEquals("Game: get last move unsuccessful.", m2, game.getLastMove());
    assertEquals("Game: apply move unsuccessful.", Colour.WHITE, game.getCurrentPlayer());
    Square sf3 = board.getSquare(2, 3);
    Square st3 = board.getSquare(2, 4);
    Move m3 = new Move(sf3, st3, false, false);
    game.applyMove(m3);
    assertEquals("Game: get last move unsuccessful.", m3, game.getLastMove());
    assertEquals("Game: apply move unsuccessful.", Colour.BLACK, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapply move unsuccessful.", m2, game.getLastMove());
    assertEquals("Game: unapply move unsuccessful.", Colour.WHITE, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapply move unsuccessful.", m1, game.getLastMove());
    assertEquals("Game: unapply move unsuccessful.", Colour.BLACK, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapply move unsuccessful.", null, game.getLastMove());
    assertEquals("Game: unapply move unsuccessful.", Colour.WHITE, game.getCurrentPlayer());
    game.unapplyMove();
    assertEquals("Game: unapply move at starting position unsuccessful.", null, game.getLastMove());
    assertEquals("Game: unapply move at starting position unsuccessful.", Colour.WHITE, game.getCurrentPlayer());

    assertEquals("Game: Parse Move 1 unsuccessful.", 1, game.parseMove("b3").getFrom().getX());
    assertEquals("Game: Parse Move 1 unsuccessful.", 1, game.parseMove("b3").getFrom().getY());
    assertEquals("Game: Parse Move 1 unsuccessful.", 1, game.parseMove("b3").getTo().getX());
    assertEquals("Game: Parse Move 1 unsuccessful.", 2, game.parseMove("b3").getTo().getY());
    assertEquals("Game: Parse Move 2 unsuccessful.", 7, game.parseMove("h4").getFrom().getX());
    assertEquals("Game: Parse Move 2 unsuccessful.", 1, game.parseMove("h4").getFrom().getY());
    assertEquals("Game: Parse Move 2 unsuccessful.", 7, game.parseMove("h4").getTo().getX());
    assertEquals("Game: Parse Move 2 unsuccessful.", 3, game.parseMove("h4").getTo().getY());
  }
}