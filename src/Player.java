
public class Player {
  
  private Game game;
  private Board board;
  private Colour colour;
  private boolean isComputerPlayer;
  private Player opponent;

  public Player(Game game, Board board, Colour colour, boolean isComputerPlayer) {
    this.game = game;
    this.board = board;
    this.colour = colour;
    this.isComputerPlayer = isComputerPlayer;
  }

  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  public Colour getColour() {
    return colour;
  }

  public boolean isComputerPlayer() {
    return isComputerPlayer;
  }

  public Square[] getAllPawns() {
    Square[] allPawns = new Square[7];
    int index = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board.getSquare(i, j).occupiedBy() == colour) {
          allPawns[index] = board.getSquare(i, j);
          index += 1;
        }
      }
    }
    Square[] allPawns2 = new Square[index];
    for (int i = 0; i < index; i++) {
      allPawns2[i] = allPawns[i];
    }
    return allPawns2;
  }

  public Move[] getAllValidMoves() {
    return null;
  }

  public boolean isPassedPawn(Square square) {
    return false;
  }

  public void makeMove() {

  }
}