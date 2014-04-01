/* MachinePlayer.java */

package player;

import list.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  protected static int COMPUTER;
  protected static int OPPONENT;
  
  private Grid board; //this machine's internal board representation
  int searchDepth; // this machine's search depth


  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
      if(color == 0)
        COMPUTER = 0;
      if(color == 1)
        COMPUTER = 1;
      OPPONENT = 1-COMPUTER;
      this.searchDepth = 1; // needs to search as high as possible within 5 seconds
      board = new Grid(color);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
      if(color == 0)
        COMPUTER = 0;
      if(color == 1)
        COMPUTER = 1;
      OPPONENT = 1 - COMPUTER;
      this.searchDepth = searchDepth;
      board = new Grid(color);
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    
    // generate all possible moves
    list.DList moves = board.generateAllPossibleMoves(COMPUTER);

    try {
    // algorithm to find best possible move
    Best bestMove = miniMax(COMPUTER, 1234567, -1234567, searchDepth);
  } catch (Invalid) {

  }
    // place piece on board
    Move chosenMove = bestMove.move;
    board.doMove(COMPUTER, chosenMove);
    // return the move
    return chosenMove;
  	} 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    // place opponent piece on the grid
    if(!board.isValidMove(OPPONENT, m.x1, m.y1))
      return false;
    board.doMove(OPPONENT, m);
    return true;
    }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if(!board.isValidMove(COMPUTER, m.x1, m.y1)) {
      return false;
    }
    board.doMove(COMPUTER, m);
    return true;
    }

  /**
  * miniMax() calculates the best move within a given set of moves
  * for a given search depth.
  * @param moves the array of possible moves
  * @param searchDepth the search depth complexity
  * @return the best calculated possible move
  **/
  private Best miniMax(int side, int alpha, int beta, int searchDepth) throws InvalidNodeException {
	  Best myBest = new Best();     // Machine's best move
	  Best reply;                   // Opponent's best reply
	  int side2;
	  if (side == 0) {
		  side2 = 1;
	  } else {
		  side2 = 0;
	  }
	  
	  if (board.isFull() || board.hasValidNetwork(side) || searchDepth >= this.searchDepth){
		  myBest.score = eval();
		  return myBest;
	  }
	  if (side == OPPONENT){
		  myBest.score = alpha;
	  }else{
		  myBest.score = beta;
		  }
	  DList moves = board.generateAllPossibleMoves(side);
	  DListNode pointer;
	try {
		pointer = (DListNode) moves.front().next();
	  myBest.move = (Move) pointer.item();
	  while(pointer != moves.front()){
		  board.doMove(side, myBest.move);
		  reply = miniMax(side2, alpha, beta, searchDepth++);
		  board.undoMove(side, myBest.move);
		  if (side == OPPONENT && reply.score > myBest.score){
			  myBest.move = (Move) pointer.item();
			  myBest.score = reply.score;
			  alpha = reply.score;
		  } else if (side == COMPUTER && reply.score < myBest.score){
			  myBest.move = (Move) pointer.item();
			  myBest.score = reply.score;
			  beta = reply.score;
		  }
		  if (alpha >= beta){ return myBest;}
		  pointer = (DListNode) pointer.next();
	  }
	}
	  catch (InvalidNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  return myBest;
 }
  
  private int eval() throws InvalidNodeException {
	  if (board.hasValidNetwork(COMPUTER)){
		  return 1000;
	  }
	  else if (board.hasValidNetwork(OPPONENT)){
		  return -1000;
		  }
	  return (board.maxNumOfConnections(COMPUTER) - board.maxNumOfConnections(OPPONENT) + Math.max((board.chipsInGoal(COMPUTER)), 3) + board.getNumPieces() + 10*(board.chipsInGoal(COMPUTER) + board.chipsInGoal(OPPONENT))) ;
	  }
}