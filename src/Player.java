import java.util.random;

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
      return null;
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