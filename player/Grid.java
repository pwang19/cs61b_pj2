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

  /*
  @return true if it is a valid move
  @return false if it is not
  */
  protected void isValid() throws InvalidMoveException {
  	if(hasPiece() || inCorner() || inOppGoal() || isClustered()) {
  		throw new InvalidMoveException();
  	} else {
  		//do nothing?
  	}
  }

  private boolean inCorner() {
  	return false;
  }

  private boolean inOppGoal() {
  	return false;
  }

  private boolean isClustered() {
  	return false;
  }

  private boolean hasPiece() {
  	return false;
  }

  protected Move[] generateAllPossibleMoves() {
  	return new Move[];
  }

  public Move miniMax(Move[] moves) {
  	return new Move();
  }
}