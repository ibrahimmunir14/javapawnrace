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
    switch (getGameResult()) {
      case BLACK:
      case WHITE:
      case NONE:
      return true;
      default:
      return false;
    }
  }

  public Colour getGameResult() {
    Player pl = new Player(this, board, Colour.WHITE, false);
    if (getLastMove().getTo().getY() == 0 && currentPlayer == Colour.WHITE) {
      return Colour.BLACK; // last move sent a black pawn to the white home line
    }
    else if (getLastMove().getTo().getY() == 7 && currentPlayer == Colour.BLACK) {
      return Colour.WHITE; // last move sent a white pawn to the black home line
    }
    else if (pl.getAllValidMoves().length == 0) {
      return Colour.NONE; // no valid moves for current player: stalemate
    }
    else return null;
  }

  public Move parseMove(String san) {
    // Pre: input is of the form [a-h]x[a-h][1-8] e.g. bxc4
    // Case: standard move, one/two spaces forward
    if (san.length() == 2) {
      char sanCol = san.charAt(0);
      char sanRow = san.charAt(1);
      int toX = Util.ColtoX(sanCol);
      int toY = Util.RowtoY(Character.getNumericValue(sanRow));
      Square sTo = board.getSquare(toX, toY);
      if (sTo.occupiedBy() != Colour.NONE) {
        return null; // invalid move: to square already occupied
      }
      if (currentPlayer == Colour.WHITE ? toY == 0 || toY == 1 : toY == 7 || toY == 6) {
        // pawn is trying to move to first/starting line
        return null; // invalid move: can only move forwards
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
        return null; // invalid move: there is no pawn that can move to the to square
      }
    }
    // Case: capture move, one space forward-right or forward-left
    else if (san.length() == 4) {
      char sanColFrom = san.charAt(0);
      char sanCol = san.charAt(2);
      char sanRow = san.charAt(3);
      int toX = Util.ColtoX(sanCol);
      int toY = Util.RowtoY(Character.getNumericValue(sanRow));
      Square sTo = board.getSquare(toX, toY);
      if (sTo.occupiedBy() == currentPlayer) {
        // invalid move: to square already occupied by player
        return null;
      }
      if (currentPlayer == Colour.WHITE ? toY == 0 || toY == 1 : toY == 7 || toY == 6) {
        // pawn is trying to move to first/starting line
        return null; // invalid move: can only move forwards
      }
      if (currentPlayer == Colour.WHITE ? toY == 7 : toY == 0) {
        // pawn is trying to capture a pawn on the last line
        return null; // invalid move: no pawns can be captured on the last line
      }
      int fromX = Util.ColtoX(sanColFrom);
      int fromY = currentPlayer == Colour.WHITE ? toY-1 : toY+1;
      if (fromX != toX - 1 && fromX != toY - 1) {
        return null; // invalid move: capture must be from an adjacent column
      }
      Square sFrom = board.getSquare(fromX, fromY);
      Square enPassantSquare = board.getSquare(toX, (currentPlayer == Colour.WHITE ? toY-1 : toY+1));
      Square enPassantSquareHome = board.getSquare(toX, (currentPlayer == Colour.WHITE ? toY+1 : toY-1));
      if (sFrom.occupiedBy() != currentPlayer) {
        // invalid move: from square not occupied by player
        return null;
      }
      if (sTo.occupiedBy() == currentPlayer.opponentColour()) {
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