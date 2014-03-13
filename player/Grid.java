public class Grid {

	Coordinate[][] board;

	public Grid() {
		board = new Coordinate[8][8];
		//instantiate coordinates
	}

	  /**
   *  hasValidNetwork() determines whether "this" GameBoard has a valid network
   *  for player "side".  (Does not check whether the opponent has a network.)
   *  A full description of what constitutes a valid network appears in the
   *  project "readme" file.
   *
   *  Unusual conditions:
   *    If side is neither MachinePlayer.COMPUTER nor MachinePlayer.OPPONENT,
   *          returns false.
   *    If GameBoard squares contain illegal values, the behavior of this
   *          method is undefined (i.e., don't expect any reasonable behavior).
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @return true if player "side" has a winning network in "this" GameBoard;
   *          false otherwise.
   **/
  protected boolean hasValidNetwork(int side) {
  	return false;
  }


  protected void putPiece(int side, int x, int y) {
  	if(isValid()) {
  		//do it
  	}

  }

  protected void removePiece(int x, int y) {
  	//removes piece at specified coordinates
  }

  /**
  * isValid() checks if this move is a valid move.
  * @param m a move
  * @return true if it is a valid move
  * @return false if it is not
  */
  protected void isValid(Move m) throws InvalidMoveException {
  	if(hasPiece() || inCorner() || inOppGoal() || isClustered()) {
  		throw new InvalidMoveException();
  	} else {
  		//do nothing?
  	}
  }

  /**
  * inCorner() checks if the move is in a corner.
  * @param move the move
  **/
  private boolean inCorner(Move m) {
  	return false;
  }

  /**
  * inOppGoal() checks if the move is in the opposite goal.
  * @param move the move
  **/
  private boolean inOppGoal(Move m) {
  	return false;
  }

  /**
  * inClustered() checks if the move makes 3 or more pieces adjacent
  * to one another.
  * @param move the move
  **/
  private boolean isClustered(Move m) {
  	return false;
  }

  /**
  * inCorner() checks if there is already a piece in the final location.
  * @param move the move
  **/
  private boolean hasPiece(Move m) {
  	return false;
  }

  /**
  * generateAllPossibleMoves() generates an array of all the possible
  * moves that our bot can make.
  **/
  protected Move[] generateAllPossibleMoves() {
  	return new Move[];
  }

  /**
  * miniMax() calculates the best move within a given set of moves
  * for a given search depth.
  * @param moves the array of possible moves
  * @param searchDepth the search depth complexity
  * @return the best calculated possible move
  **/
  public Move miniMax(Move[] moves, int searchDepth) {
  	return new Move();
  }
}