package player;

public class Grid {

  private final int BOARD_DIMENSIONS = 8;
	private Coordinate[][] board;
  
  private int computer_pieces;

  /**
  * Instantiate a board
  **/

  public Grid(int playerColor) {
		board = new Coordinate[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		
    //instantiate all coordinates
    for(int i = 0; i < BOARD_DIMENSIONS; i++) {
      for(int j = 0; j < BOARD_DIMENSIONS; j++) {
        board[i][j] = new Coordinate();
      }
    }

    //makes the corners and opponent goals invalid
    if(playerColor == 0) {
      for(int i = 0; i < BOARD_DIMENSIONS; i++) {
        board[i][0].setValid(false);
       board[i][BOARD_DIMENSIONS - 1].setValid(false);
      }
    } else if(playerColor == 1) {
      for(int i = 0; i < BOARD_DIMENSIONS; i++) {
        board[0][i].setValid(false);
       board[BOARD_DIMENSIONS - 1][i].setValid(false);
      }
    }

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

  /**
  * putPiece() puts a piece on the board a piece on the board.
  * @param side either COMPUTER or OPPONENT
  * @param x x-coordinate
  * @param y y-coordinate
  */
  protected void putPiece(int side, int x, int y) {
  	if(board[x][y].isValid()) {
  		//do it
      if(side == 0) {
        computer_pieces++;
      }
  	}

  }

  /**
  * removePiece() removes a piece on the board at the
  * specified coordinates.
  * @param x x-coordinate
  * @param y y-coordinate
  */
  protected void removePiece(int x, int y) {
  	//removes piece at specified coordinates
  }

  /**
  * isValid() checks if this move is a valid move.
  * @param m a move
  * @return true if it is a valid move
  * @return false if it is not
  */
  protected boolean isValid(Move m, int side) throws InvalidMoveException {
  	// if(hasPiece(m) || inCorner(m) || inOppGoal(m, side) || isClustered(m, side)) {
  	if(board[m.x1][m.y1].isValid()) {
    	throw new InvalidMoveException();
  	}
    return true;
  }

  /**
  * generateAllPossibleMoves() generates an array of all the possible
  * moves that our bot can make.
  **/

  protected Move[] generateAllPossibleMoves() {
  	Move[] moves = new Move[10];
    if(allPiecesUsed()) {
      //all generated moves are step moves
    } else {
      //all generated moves are add moves
    }
    return moves;
  }

  private boolean allPiecesUsed() {
    return (computer_pieces >= 10);
  }

  protected Coordinate[][] getBoard() {
    return board;
  }

  // /**
  // * inCorner() checks if the move is in a corner.
  // * @param move the move
  // **/
  // private boolean inCorner(Move m) {
  //  return false;
  // }

  // /**
  // * inOppGoal() checks if the move is in the opposite goal.
  // * @param move the move
  // * @param side either COMPUTER or OPPONENT
  // **/
  // private boolean inOppGoal(Move m, int side) {
  //  return false;
  // }

  // /**
  // * inClustered() checks if the move makes 3 or more pieces adjacent
  // * to one another.
  // * @param move the move
  // * @param side either COMPUTER or OPPONENT
  // **/
  // private boolean isClustered(Move m, int side) {
  //  return false;
  // }

  // /**
  // * inCorner() checks if there is already a piece in the final location.
  // * @param move the move
  // **/
  // private boolean hasPiece(Move m) {
  //  return false;
  // }

  /**
  * toString() prints out a visual representation of the
  * board onto the console.
  **/
  public String toString() {
    return "";
  }
  
  public static void main(String[] args) {
	  
  }
}