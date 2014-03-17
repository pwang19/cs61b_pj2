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
  protected void putPiece(int side, int x, int y) throws InvalidMoveException{
  	// if adding opponent piece, don't even bother checking valid
    // the ref would catch them cheating anyways.
    if(side == MachinePlayer.OPPONENT) {
      board[x][y].addPiece(side + 1);
    
    //when adding our piece, we have to check validity.
    } else if(board[x][y].isValid()) {	
      board[x][y].addPiece(side + 1);

      // if adjacent to another piece of same color, 
      // then make invalid some boxes

      // add pieces count
      computer_pieces++;
    } else {
      throw new InvalidMoveException(x, y);
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

  /**
  * toString() prints out a visual representation of the
  * board onto the console.
  **/
  public String toString() {
    String board = "----------------------------------------- \n";
    for(int row = 0; row < 8; row++) {
      board += "|";
      for(int col = 0; col < 8; col++) {
        board += " " + this.board[row][col] + " |";
      }
      board += " _" + row;
      board += "\n-----------------------------------------\n";
    }
    board += "  0_   1_   2_   3_   4_   5_   6_   7_";
    return board;
  }

  // For testing purposes
  private int getNumPieces() {
    return computer_pieces;
  }

  /**
  * main() is the test class for Grid.
  **/
  public static void main(String[] args) {
    Grid g1 = new Grid(0);
    System.out.println("Grid representation of player 0:");
    System.out.println(g1);

    Grid g2 = new Grid(1);
    System.out.println("Grid representation of player 1:");
    System.out.println(g2);

    try {
      g1.putPiece(0, 1, 1);
      System.out.println(g1.getNumPieces());
      // g1.putPiece(1, 1, 1); //gives error. good.
      // System.out.println(g1.getNumPieces()); 
      g1.putPiece(1, 2, 1);
      System.out.println(g1.getNumPieces());

      System.out.println(g1);

    } catch (InvalidMoveException e) {
      System.out.println(e);
    }
  }

}