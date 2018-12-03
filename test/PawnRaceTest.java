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
  public void BoardApplyStandardMoveTest() {
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
        4  5  .  .  w' b' .  b'  .  .  5
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
  }


}