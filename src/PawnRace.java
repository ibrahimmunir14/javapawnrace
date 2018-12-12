import java.util.Scanner;

public class PawnRace {
  public static void main2(String[] args) {
    Board board = new Board(args[2].toLowerCase().charAt(0), args[3].toLowerCase().charAt(0));
    Game game = new Game(board);
    Player p1 = new Player(game, board, Colour.WHITE, (args[0].toLowerCase().charAt(0) == 'p' ? false : true));
    Player p2 = new Player(game, board, Colour.BLACK, (args[1].toLowerCase().charAt(0) == 'p' ? false : true));
    p1.setOpponent(p2);
    p2.setOpponent(p1);
    game.setAIPlayer(p1.isComputerPlayer() ? p1 : p2);
    System.out.println();
    System.out.println(" =============================== ");
    System.out.println(" ====  JAVA CHESS PAWN RACE ==== ");
    System.out.println(" ====  Ibrahim Munir-Zubair ==== ");
    System.out.println(" =============================== ");
    System.out.println(); System.out.println();
    while (!game.isFinished()) {
      board.display();
      System.out.println();
      Player currentPlayer = (game.getCurrentPlayer() == Colour.WHITE ? p1 : p2);
      Move thisMove = null;
      // Computer's turn
      if (currentPlayer.isComputerPlayer()) {
        currentPlayer.makeAIMove(2);
        thisMove = game.getLastMove();
      }
      // Player's turn
      else {
        String inputSAN;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println(currentPlayer.getColour().colourString() + "'s turn. Please enter a move: ");
            inputSAN = scan.next().toLowerCase();
            thisMove = game.parseMove(inputSAN);
        } while (thisMove == null);
        game.applyMove(thisMove);
      }
      System.out.println(currentPlayer.getColour().colourString() + " has played " + thisMove.getSAN() + ".");
      System.out.println(" ------------------------------- ");
      System.out.println();
    }
    System.out.println();
    System.out.println(" =========  GAME OVER  ========= ");
    System.out.println(); System.out.println();
    board.display();
    System.out.println();
    switch (game.getGameResult()) {
      case NONE:
      System.out.println(" -  The result is a stalemate.  -");
      break;
      case WHITE:
      System.out.println(" -  White (Player 1) has won the game.  -");
      break;
      case BLACK:
      System.out.println(" -  Black (Player 2) has won the game.  -");
      break;
    }
    System.out.println();
  }

  

  public static void main(String[] args) {
    int whiteWins = 0;
    int blackWins = 0;
    for (int i = 0; i < 1000; i++) {
      Board board = new Board('c', 'e');
      Game game = new Game(board);
      Player p1 = new Player(game, board, Colour.WHITE, true);
      Player p2 = new Player(game, board, Colour.BLACK, true);
      p1.setOpponent(p2);
      p2.setOpponent(p1);
      
      //game.setAIPlayer(p1.isComputerPlayer() ? Colour.WHITE : Colour.BLACK);
      p1.makeMove();
      p2.makeMove();
      p1.makeMove();
      p2.makeMove();
      while (!game.isFinished()) {
        // board.display();
        // System.out.println();
        Player currentPlayer = (game.getCurrentPlayer() == Colour.WHITE ? p1 : p2);
        game.setAIPlayer(currentPlayer);
        Move thisMove = null;
        // Computer's turn
        if (currentPlayer == p1) {
          currentPlayer.makeAIMove(8);
          thisMove = game.getLastMove();
        }
        // Player's turn
        else {
          currentPlayer.makeAIMove(1);
          thisMove = game.getLastMove();
        }
        // System.out.println(currentPlayer.getColour().colourString() + " has played " + thisMove.getSAN() + ".");
        // System.out.println(" ------------------------------- ");
        // System.out.println();
        // System.out.print(thisMove.getSAN() + "  ");
      }
      // System.out.println();
      // System.out.println(" =========  GAME OVER  ========= ");
      // System.out.println(); System.out.println();
      // board.display();
      // System.out.println();
      switch (game.getGameResult()) {
        case NONE:
        // System.out.println(" -  The result is a stalemate.  -");
        break;
        case WHITE:
        // System.out.println(" -  White (Player 1) has won the game.  -");
        whiteWins++;
        break;
        case BLACK:
        // System.out.println(" -  Black (Player 2) has won the game.  -");
        blackWins++;
        break;
      }
      System.out.println(((100*whiteWins) / (i+1)) + "%");
    }
    System.out.println();System.out.println();System.out.println();
    System.out.println("White Win Percentage: " + ((100*whiteWins) / 100));
    System.out.println("Black Win Percentage: " + ((100*blackWins) / 100));
  }
}