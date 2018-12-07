public class Util {
  public static char XtoCol(int x) {
    return (char)('a' + x);
  }
  public static int YtoRow(int y) {
    return y + 1;
  }
  public static int ColtoX(char col) {
    return col - 'a';
  }
  public static int RowtoY(int row) {
    return row - 1;
  }

  public static Square[] ShrinkSquaresArray(Square[] arr, int length) {
    Square[] arr2 = new Square[length];
    for (int i = 0; i < length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  public static Move[] ShrinkMovesArray(Move[] arr, int length) {
    Move[] arr2 = new Move[length];
    for (int i = 0; i < length; i++) {
      arr2[i] = arr[i];
    }
    return arr2;
  }

  public static boolean MovesContains(Move[] moves, Move move) {
    for (Move m : moves) {
      if (m == move) {
        return true;
      }
    }
    return false;
  }

}