/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  protected final int COMPUTER = 0;
  protected final int OPPONENT = 1;
  
  private Grid board; //this machine's internal board representation
  private Grid 
  int searchDepth; // this machine's search depth


  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
      if(color == 0)
        myName = "black";
      if(color == 1)
        myName = "white";
      this.searchDepth = 1; // needs to search as high as possible within 5 seconds
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
      if(color == 0)
        myName = "black";
      if(color == 1)
        myName = "white";
      this.searchDepth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    
    // generate all possible moves
    Move[] moves = board.generateAllPossibleMoves();

    // algorithm to find best possible move
    Move chosenMove = board.miniMax(moves);

    // place piece on board
    if(chosenMove.moveKind ==)
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
    board.putPiece(OPPONENT);
    return false;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    //if(invalid)
    return false;
    //else
    //return true;
  }

}
