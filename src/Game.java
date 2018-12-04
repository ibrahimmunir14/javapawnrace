public class Game {
  private Board board;
  private Move[] moves;
  private int index;
  private Colour currentPlayer;

  public Game(Board board) {
    this.board = board;
    moves = new Move[96];
    index = 0;
    currentPlayer = Colour.WHITE;
  }

  public Colour getCurrentPlayer() {
    return currentPlayer;
  }

  public Move getLastMove() {
    return (index == 0) ? null : moves[index-1];
  }

  public void applyMove(Move move) {
    board.applyMove(move);
    moves[index] = move;
    index++;
    currentPlayer = currentPlayer.opponentColour();
  }

  public void unapplyMove() {
    if (getLastMove() != null) {
      board.unapplyMove(getLastMove());
      moves[index-1] = null;
      index--;
      currentPlayer = currentPlayer.opponentColour();
    }
  }

  public boolean isFinished() {
    Player pl = new Player(this, board, Colour.WHITE, false);
    if (pl.getAllValidMoves().length == 0) {
      return true;
    }
    else if (getLastMove().getTo().getY() == 0 && currentPlayer == Colour.WHITE) {
      // last move sent a black pawn to the white home line
      return true;
    }
    else if (getLastMove().getTo().getY() == 7 && currentPlayer == Colour.BLACK) {
      // last move sent a white pawn to the black home line
      return true;
    }
    else return false;
  }

  public Colour getGameResult() {
    Player pl = new Player(this, board, Colour.WHITE, false);
    if (pl.getAllValidMoves().length == 0) {
      return Colour.NONE;
    }
    else if (getLastMove().getTo().getY() == 0 && currentPlayer == Colour.WHITE) {
      // last move sent a black pawn to the white home line
      return Colour.BLACK;
    }
    else if (getLastMove().getTo().getY() == 7 && currentPlayer == Colour.BLACK) {
      // last move sent a white pawn to the black home line
      return Colour.WHITE;
    }
    else return null;
  }

  public Move parseMove(String san) {
    if (san.length() == 2) { // standard move, one or two spaces
      char sanCol = san.charAt(0);
      char sanRow = san.charAt(1);
      int toX = sanCol - 'a';
      int toY = Character.getNumericValue(sanRow)-1;
      Square sTo = board.getSquare(toX, toY);
      if (sTo.occupiedBy() != Colour.NONE) {
        return null; // invalid move: square occupied
      }
      int fromX = toX;
      Square possibleFrom = board.getSquare(fromX, (currentPlayer == Colour.WHITE ? toY-1 : toY+1));
      Square possibleFromStarting = board.getSquare(fromX, (currentPlayer == Colour.WHITE ? toY-2 : toY+2));
      boolean couldBeStartingMove = (currentPlayer == Colour.WHITE) ? (toY-2 == 1) : (toY+2 == 6);
      if (possibleFrom.occupiedBy() == Colour.WHITE) {
        return new Move(possibleFrom, sTo, false, false); // piece moves forward one space
      }
      else if (couldBeStartingMove && possibleFromStarting.occupiedBy() == Colour.WHITE) {
        if (possibleFrom.occupiedBy() != Colour.NONE) {
            return null; // invalid move: square jumping over is occupied
        }
        return new Move(possibleFromStarting, sTo, false, false); // piece moves forward two spaces opening move
      }
      else {
        return null; // invalid move: no pawn to move straight forwards to that space
      }
    }
    else if (san.length() == 4) { // capture move, diagonally one space
      // possible cases: normal capture
      //                 enpassant capture

    }
    return null;
  }
}