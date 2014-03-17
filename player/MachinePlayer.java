/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  protected static final int COMPUTER = 0;
  protected static final int OPPONENT = 1;
  
  private Grid board; //this machine's internal board representation
  int searchDepth; // this machine's search depth


  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
      if(color == 0)
        myName = "black";
      if(color == 1)
        myName = "white";
      this.searchDepth = 1; // needs to search as high as possible within 5 seconds
      board = new Grid(color);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
      if(color == 0)
        myName = "black";
      if(color == 1)
        myName = "white";
      this.searchDepth = searchDepth;
      board = new Grid(color);
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    
    // generate all possible moves
    Move[] moves = board.generateAllPossibleMoves();

    // algorithm to find best possible move
    Move chosenMove = miniMax(moves, searchDepth);

    // place piece on board
    if(chosenMove.moveKind == Move.STEP)
      board.removePiece(chosenMove.x2, chosenMove.y2);
    board.putPiece(COMPUTER, chosenMove.x1, chosenMove.y1);

    // return the move
    return chosenMove;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    // place opponent piece on the grid
    if(!board.getBoard()[m.x1][m.y1].isValid())
      return false;
    if(m.moveKind == Move.STEP)
      board.removePiece(m.x2, m.y2);
    board.putPiece(OPPONENT, m.x1, m.y1);
    return true;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if(!board.getBoard()[m.x1][m.y1].isValid()) {
      return false;
    }

    if(m.moveKind == 2) {
      board.removePiece(m.x2, m.y2);
    }

    board.putPiece(COMPUTER, m.x1, m.y1);
    return true;
  }


  /**
  * miniMax() calculates the best move within a given set of moves
  * for a given search depth.
  * @param moves the array of possible moves
  * @param searchDepth the search depth complexity
  * @return the best calculated possible move
  **/
  private Move miniMax(Move[] moves, int searchDepth) {
    return new Move();
  }

}
