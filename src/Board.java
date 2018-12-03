public class Board {
  private final char whiteGap;
  private final char blackGap;
  private Square[][] gameBoard = new Square[8][8];
  // board indexes match coordinates
  /*        A  B  C  D  E  F  G  H
      7  8  .  .  .  .  .  .  .  .  8
      6  7  .  .  .  .  .  .  .  .  7
      5  6  .  .  .  .  .  .  .  .  6
      4  5  .  .  .  .  .  .  .  .  5
      3  4  .  .  .  .  .  .  .  .  4
      2  3  .  .  .  .  .  .  .  .  3
      1  2  .  .  .  .  .  .  .  .  2
      0  1  .  .  .  .  .  .  .  .  1
            A  B  C  D  E  F  G  H
            0  1  2  3  4  5  6  7
  */

  public Board(char whiteGap, char blackGap) {
    this.whiteGap = whiteGap;
    this.blackGap = blackGap;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        gameBoard[i][j] = new Square(i, j);
      }
    }
    for (int k = 0; k < 8; k++) {
      if (gameBoard[k][1].getColSAN() != whiteGap) {
        gameBoard[k][1].setOccupier(Colour.WHITE);
      }
      if (gameBoard[k][6].getColSAN() != blackGap) {
        gameBoard[k][6].setOccupier(Colour.BLACK);
      }
    }
  }

  public Square getSquare(int x, int y) {
      return gameBoard[x][y];
  }
  public void applyMove(Move move) {
      Square fSquare = move.getFrom();
      Square tSquare = move.getTo();
      if (! move.isCapture()) {
        gameBoard[tSquare.getX()][tSquare.getY()].setOccupier(fSquare.occupiedBy());
        gameBoard[fSquare.getX()][fSquare.getY()].setOccupier(Colour.NONE);
      }
  }
}