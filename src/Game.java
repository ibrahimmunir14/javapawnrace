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
    if (san.length() == 2) { // standard move
      char sanCol = san.charAt(0);
      char sanRow = san.charAt(1);
      int toX = Util.ColtoX(sanCol);
      int toY = Util.RowtoY(Character.getNumericValue(sanRow));
      Square sTo = board.getSquare(toX, toY);
      if (sTo.occupiedBy() != Colour.NONE) {
        return null; // invalid move: square occupied
      }
      int fromX = toX;
      Square oneSquareBack = board.getSquare(fromX, (currentPlayer == Colour.WHITE ? toY-1 : toY+1));
      Square twoSquareBack = board.getSquare(fromX, (currentPlayer == Colour.WHITE ? toY-2 : toY+2));
      boolean couldBeStartingMove = (currentPlayer == Colour.WHITE) ? (toY == 3) : (toY == 4);
      if (oneSquareBack.occupiedBy() == currentPlayer) {
        // player has pawn one square back, move that pawn forward one space
        return new Move(oneSquareBack, sTo, false, false);
      }
      else if (couldBeStartingMove && twoSquareBack.occupiedBy() == currentPlayer && oneSquareBack.occupiedBy() == Colour.NONE) {
        // player has pawn two space back, on the starting line, and the middle square is empty, move pawn two spaces forward
        return new Move(twoSquareBack, sTo, false, false);
      }
      else {
        return null;
      }
    }
    else if (san.length() == 4) { // capture move
      char sanColFrom = san.charAt(0);
      char sanCol = san.charAt(2);
      char sanRow = san.charAt(3);
      int toX = Util.ColtoX(sanCol);
      int toY = Util.RowtoY(Character.getNumericValue(sanRow));
      Square sTo = board.getSquare(toX, toY);
      int fromX = Util.ColtoX(sanColFrom);
      int fromY = currentPlayer == Colour.WHITE ? toY-1 : toY+1;
      Square sFrom = board.getSquare(fromX, fromY);
      Square enPassantSquare = board.getSquare(toX, (currentPlayer == Colour.WHITE ? toY-1 : toY+1));
      Square enPassantSquareHome = board.getSquare(toX, (currentPlayer == Colour.WHITE ? toY+1 : toY-1));
      // TODO: Check if Square is on FIRST/LAST COLUMN
      if (sFrom.occupiedBy() != currentPlayer) {
        // invalid move: from square not occupied by player
        return null;
      }
      if (sTo.occupiedBy() == currentPlayer) {
        // invalid move: to square occupied by player
        return null;
      }
      else if (sTo.occupiedBy() == currentPlayer.opponentColour()) {
        // standard capture: to square occupied by opponent
        return new Move(sFrom, sTo, true, false);
      }
      else if (enPassantSquare.occupiedBy() == currentPlayer.opponentColour() && getLastMove().getFrom() == enPassantSquareHome && getLastMove().getTo() == enPassantSquare) {
        // en passant capture: toSquare is empty, enPassantSquare occupied by opponent, last move was right enpassant move
        return new Move(sFrom, sTo, true, true);
      }
      return null;
    }
    return null;
  }
}