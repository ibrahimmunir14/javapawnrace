import java.util.Random;

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
    Move[] allMoves = new Move[21];
    int index = 0;
    for (Square pawn : getAllPawns()) {
      // Case: pawn is on the last line (-> game has been won) -> no moves for this pawn.
      if (colour == Colour.WHITE ? pawn.getY() == 7 : pawn.getY() == 0) {
        continue;
      }
      // Case: pawn is on the penultimate line -> only possible move is one forward, if its empty.
      Square sqInFront = board.getSquare(pawn.getX(), (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1));
      if ( colour == Colour.WHITE
         ? (pawn.getY() == 6 && sqInFront.occupiedBy() == Colour.NONE)
         : (pawn.getY() == 1 && sqInFront.occupiedBy() == Colour.NONE) ) {
        allMoves[index] = new Move(pawn, board.getSquare(pawn.getX(), (colour == Colour.WHITE ? 7 : 0)), false, false);
        index++;
        continue;
      }
      // Cases: pawn is on any other line
      //  Case: standard move: pawn moving forwards
      if (sqInFront.occupiedBy() == Colour.NONE) { // square in front is empty
        allMoves[index] = new Move(pawn, sqInFront, false, false);
        index++;
        if (colour == Colour.WHITE ? pawn.getY() == 1 : pawn.getY() == 6) { // pawn is on starting line
          Square sq2InFront = board.getSquare(pawn.getX(), (colour == Colour.WHITE ? pawn.getY()+2 : pawn.getY()-2));
          if (sq2InFront.occupiedBy() == Colour.NONE) { // two squares in front are empty
            // this pawn can play a starting move two squares forward
            allMoves[index] = new Move(pawn, sq2InFront, false, false);
            index++;
          }
        }
      }
      //  Case: capture move: pawn taking forward_right square
      if (colour == Colour.WHITE ? pawn.getX() != 7 : pawn.getX() != 0) { // pawn is not on the right edge
        Square sqForwardRight = board.getSquare( (colour == Colour.WHITE ? pawn.getX()+1 : pawn.getX()-1)
                                               , (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1) );
        Square sqRight = board.getSquare((colour == Colour.WHITE ? pawn.getX()+1 : pawn.getX()-1), pawn.getY());
        if (sqForwardRight.occupiedBy() == colour.opponentColour()) { // forward right square can be captured standard
          allMoves[index] = new Move(pawn, sqForwardRight, true, false);
          index++;
        }
        else if ( sqRight.occupiedBy() == colour.opponentColour() // right square occupied by opponent
               && game.getLastMove().getTo() == sqRight // opponents last move was to the right square
               && game.getLastMove().getFrom().getY() == (colour == Colour.WHITE ? 6 : 1)) { 
          // forward right square can be taken by enpassant
          allMoves[index] = new Move(pawn, sqForwardRight, true, true);
          index++;
        }
      }
      //  Case: capture move: pawn taking forward_left square
      if (colour == Colour.WHITE ? pawn.getX() != 0 : pawn.getX() != 7) { // pawn is not on the left edge
        Square sqForwardLeft = board.getSquare( (colour == Colour.WHITE ? pawn.getX()-1 : pawn.getX()+1)
                                              , (colour == Colour.WHITE ? pawn.getY()+1 : pawn.getY()-1) );
        Square sqLeft = board.getSquare((colour == Colour.WHITE ? pawn.getX()-1 : pawn.getX()+1), pawn.getY());
        if (sqForwardLeft.occupiedBy() == colour.opponentColour()) { // forward left square can be captured standard
          allMoves[index] = new Move(pawn, sqForwardLeft, true, false);
          index++;
        }
        else if ( sqLeft.occupiedBy() == colour.opponentColour() // left square occupied by opponent
               && game.getLastMove().getTo() == sqLeft // opponents last move was to left square
               && game.getLastMove().getFrom().getY() == (colour == Colour.WHITE ? 6 : 1)) {
          // forward left square can be taken by enpassant
          allMoves[index] = new Move(pawn, sqForwardLeft, true, true);
          index++;
        }
      }
    }
    return Util.ShrinkMovesArray(allMoves, index);
  }

  public boolean isPassedPawn(Square square) {
    int pawnX = square.getX();
    int pawnY = square.getY();
    if (square.occupiedBy() != colour) {
      return false;
    }
    int side1X = pawnX - 1;
    int side2X = pawnX + 1;
    int startline = (colour == Colour.WHITE ? pawnY + 1 : 1);
    int lastline = (colour == Colour.WHITE ? 7 : pawnY);
    for (int i = startline; i < lastline; i++) {
      if (board.getSquare(pawnX, i).occupiedBy() == colour.opponentColour()) {
        return false;
      }
      if (side1X >= 0 && board.getSquare(side1X, i).occupiedBy() == colour.opponentColour()) {
        return false;
      }
      if (side2X <= 7 && board.getSquare(side2X, i).occupiedBy() == colour.opponentColour()) {
        return false;
      }
    }
    return true;
  }

  public void makeMove() {
    // apply a random valid move to the game
    // does nothing if player is non-computer, or there are no valid moves
    if (isComputerPlayer) {
      Move[] validMoves = getAllValidMoves();
      if (validMoves.length > 0) {
        Move move = validMoves[new Random().nextInt(validMoves.length)];
        game.applyMove(move);
      }
    }
  }

  public void makeAIMove(int depth) {
    int maxEval = Integer.MIN_VALUE;
    Move moveToMake = null;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    for (Move child : getAllValidMoves()) {
      game.applyMove(child);
      int eval = opponent.minimax(depth-1, alpha, beta, false);
      game.unapplyMove();
      if (eval >= maxEval) {
        maxEval = eval;
        moveToMake = child;
      }
      alpha = Math.max(alpha, eval);
      if (beta <= alpha) {
        break;
      }
    }
    game.applyMove(moveToMake);
  }

  private int minimax(int depth, int alpha, int beta, boolean maximisingPlayer) {
    if (depth == 0 || game.isFinished()) {
      return evalPosition();
    }
    if (maximisingPlayer) {
      int maxEval = Integer.MIN_VALUE;
      for (Move child : getAllValidMoves()) {
        game.applyMove(child);
        int eval = opponent.minimax(depth-1, alpha, beta, false);
        game.unapplyMove();
        maxEval = Math.max(maxEval, eval);
        alpha = Math.max(alpha, eval);
        if (beta <= alpha) {
          break;
        }
      }
      return maxEval;
    }
    else {
      int minEval = Integer.MAX_VALUE;
      for (Move child : getAllValidMoves()) {
        game.applyMove(child);
        int eval = opponent.minimax(depth-1, alpha, beta, true);
        game.unapplyMove();
        minEval = Math.min(minEval, eval);
        beta = Math.min(beta, eval);
        if (beta <= alpha) {
          break;
        }
      }
      return minEval;
    }
  }

  public int evalPosition() {
    int score = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board.getSquare(i, j).occupiedBy() == game.getAIPlayer()) {
          score++;
        }
        else if (board.getSquare(i, j).occupiedBy() == game.getAIPlayer().opponentColour()){
          score--;
        }
      }
    }
    return score;
  }
  // minimax(currentPosition, 3, -infinity, infinity, true)
  // position here can be take from last move, so dont need argument?

  
}