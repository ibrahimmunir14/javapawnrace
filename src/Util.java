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
}