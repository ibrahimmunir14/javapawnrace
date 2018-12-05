
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
    return Util.ShrinkSquaresArray(allPawns, index);
  }

  public Move[] getAllValidMoves() {
    Move[] allMoves = new Move[14];
    int index = 0;
    for (Square pawn : getAllPawns()) {
      if (colour == Colour.WHITE ? pawn.getY() == 7 : pawn.getY() == 0) { // pawn on last line
        continue;
      }
      if (colour == Colour.WHITE ? pawn.getY() == 6 : pawn.getY() == 1) { // pawn on penultimate line
        allMoves[index] = new Move(pawn, board.getSquare(pawn.getX(), (colour == Colour.WHITE ? 7 : 0)), false, false);
        index++;
        continue;
      }
      Square sqInFront = board.getSquare(pawn.getX(), (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1));
      if (sqInFront.occupiedBy() == Colour.NONE) {
        // there is an empty square in front
        allMoves[index] = new Move(pawn, sqInFront, false, false);
        index++;
        if (colour == Colour.WHITE ? pawn.getY() == 1 : pawn.getY() == 6) {
          Square sq2InFront = board.getSquare(pawn.getX(), (colour == Colour.WHITE ? pawn.getY()+2 : pawn.getY()-2));
          if (sq2InFront.occupiedBy() == Colour.NONE) {
            // this pawn can play a starting move two squares forward
            allMoves[index] = new Move(pawn, sq2InFront, false, false);
            index++;
          }
        }
      }
      if (colour == Colour.WHITE ? pawn.getX() != 7 : pawn.getX() != 0) {
        // pawn is not on the right edge
        Square sqForwardRight = board.getSquare((colour == Colour.WHITE ? pawn.getX()+1 : pawn.getX()-1), (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1));
        Square sqRight = board.getSquare((colour == Colour.WHITE ? pawn.getX()+1 : pawn.getX()-1), pawn.getY());
        if (sqForwardRight.occupiedBy() == colour.opponentColour()) {
          // forward right square can be captured
          allMoves[index] = new Move(pawn, sqForwardRight, true, false);
          index++;
        }
        else if (sqRight.occupiedBy() == colour.opponentColour() && game.getLastMove().getTo() == sqRight && game.getLastMove().getFrom().getY() == (colour == Colour.WHITE ? sqRight.getY() + 2 : sqRight.getY() - 2)) {
          // forward right square can be taken by enpassant
          allMoves[index] = new Move(pawn, sqForwardRight, true, true);
          index++;
        }
      }
      if (colour == Colour.WHITE ? pawn.getX() != 0 : pawn.getX() != 7) {
        Square sqForwardLeft = board.getSquare((colour == Colour.WHITE ? pawn.getX()-1 : pawn.getX()+1), (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1));
        Square sqLeft = board.getSquare((colour == Colour.WHITE ? pawn.getX()-1 : pawn.getX()+1), pawn.getY());
        if (sqForwardLeft.occupiedBy() == colour.opponentColour()) {
          // forward left square can be captured
          allMoves[index] = new Move(pawn, sqForwardLeft, true, false);
          index++;
        }
        else if (sqLeft.occupiedBy() == colour.opponentColour() && game.getLastMove().getTo() == sqLeft && game.getLastMove().getFrom().getY() == (colour == Colour.WHITE ? sqLeft.getY() + 2 : sqLeft.getY() - 2)) {
          // forward left square can be taken by enpassant
          allMoves[index] = new Move(pawn, sqForwardLeft, true, true);
          index++;
        }
      }
    }
    return Util.ShrinkMovesArray(allMoves, index);
  }

  public boolean isPassedPawn(Square square) {
    return false;
  }

  public void makeMove() {

  }
}