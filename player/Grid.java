package player;

import list.*;

public class Grid {

  private final int BOARD_DIMENSIONS = 8;
  protected int[][] board;
  protected static final int EMPTY = 0;
  protected int PLAYER;
  protected int OPP;
  protected int COMP;
  protected int HUMAN;
  
  protected int computer_pieces;
  protected int total;
  /**
  * Instantiate a board
  **/

  public Grid(int side) {
		board = new int[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		for (int x = 0; x < BOARD_DIMENSIONS; x++) {
		   for (int y = 0; y < BOARD_DIMENSIONS; y++) {
			   board[x][y] = EMPTY;
		   }
		}
	if (side == 0) {
		PLAYER = 1;
		OPP = 2;
		COMP = 0;
		HUMAN = 1;
	}
	if (side == 1) {
		PLAYER = 2;
		OPP = 1;
		COMP = 1;
		HUMAN = 0;
	}
		computer_pieces = 0;
		total = 0;
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
	 * @throws InvalidNodeException 
   **/
  protected boolean hasValidNetwork(int side) throws InvalidNodeException {
	  	int[][] lists = allPieces(side);
		DList start = Start(side, lists);
		int i = 0;
		while (i < start.length()) {
	  	    boolean total = CountNetwork(side, ((int[])start.front().item())[0], ((int[])start.front().item())[1], lists, 0, 0);
	  	    if (total == true) {
	  	    return true;
	  	    }
	  	}
	  	return false;
	  }

  
  protected boolean CountNetwork(int side, int startx, int starty, int[][] list, int con, int len) throws InvalidNodeException {
	  DList back = new DList();
	  int[][] conn;
	  boolean discovered = false;
	  
	  if(len == list.length) {								// base case: if no more elements in list, return null
		  return false;
	  }
	  if (inSecondGoal(side, startx, starty) && con >= 6) {		// base case: if in second goal, then return connection
		  return true;
	  }
	conn = listConnections(startx, starty);  		// create list of connections from first point in the list
	DList newcon = new DList();					// create new 2d array
	int leng = 0;
	ListNode pointer = back.front();
	for (int i = 0; i < conn.length; i++) {
	  while (leng != back.length()) {
		  if (conn[i][0] == ((int[]) pointer.item())[0] && conn[i][1] == ((int[])pointer.item())[1]) {			// if any point in conn == any point in back
			  discovered = true;
		  }
		  leng++;
		  pointer = pointer.next();
	  }
	  if (discovered == false) {
		  int[] b = {conn[i][0], conn[i][1]};
		  newcon.insertBack(b);
		  back.insertBack(b);
	  }
	}
	leng = 0;
	ListNode pointer2 = newcon.front();
	if (newcon.length() != 0) {
	  con++;
	  len++;
	  while (leng != newcon.length()) {
		  int[][] g = listConnections(((int[])pointer2.item())[0], ((int[])pointer2.item())[1]);
		  pointer2 = pointer2.next();
		  leng++;
		  return CountNetwork(side, g[0][0], g[0][1], g, con, len);
	  }
	}
	return false;
  }
  
  protected DList Start(int side, int[][] list) {
	  DList start = new DList();
	  for (int i = 0; i < list.length; i++) {
		  if (inFirstGoal(side, list[i][0], list[i][1])) {
			  int[] b = {list[i][0], list[i][1]};
			  start.insertBack(b);
		  }
	  }
	  return start;
  }
  /**
  * putPiece() puts a piece on the board a piece on the board.
  * @param side either COMPUTER or OPPONENT
  * @param x x-coordinate
  * @param y y-coordinate
 * @throws InvalidMoveException 
  */
  protected void putPiece(int side, int x, int y) {
	System.out.println(this.isValidMove(side, x, y));
	if(side != COMP  && this.isValidMove(side, x, y)) {
      board[x][y] = OPP;
      total++;
    //when adding our piece, we have to check validity.
    } else if(side == COMP && this.isValidMove(side, x, y)) {
      board[x][y] = PLAYER;
      System.out.println("herp");
      computer_pieces++;
      total++;
    }
  }	

  /**
  * removePiece() removes a piece on the board at the
  * specified coordinates.
  * @param x x-coordinate
  * @param y y-coordinate
  */
  protected void removePiece(int side, int x, int y) {
	  board[x][y] = EMPTY;
	  total--;
	  if (side == COMP) {
		  computer_pieces--;
	  }
  }
  protected void doMove(int side, Move move) {
	  if (move.moveKind == Move.ADD) {
		  this.putPiece(side, move.x1, move.y1);
	  }
	  if (move.moveKind == Move.STEP) {
		  this.putPiece(side, move.x1, move.y1);
		  this.removePiece(side, move.x2, move.y2);
	  }
  }
  
  
  protected void undoMove(int side, Move move) {
	  if (move.moveKind == Move.ADD) {
		  this.removePiece(side, move.x1, move.y1);
	  }
	  if (move.moveKind == Move.STEP) {
		  this.putPiece(side, move.x2, move.y2);
		  this.removePiece(side, move.x1, move.y1);
	  }
  }
  
  
  protected int[][] allPieces(int side) {
	int i = 0; int d = 0;
	if (side == COMP) {
		d = computer_pieces;
	}
	else {
		d = total - computer_pieces;
	}
	int[][] pieces = new int[d][2];
	for (int x = 0; x < BOARD_DIMENSIONS; x++) {
		for (int y = 0; y < BOARD_DIMENSIONS; y++) {
			if (side == COMP) {
				if (board[x][y] == PLAYER) {
					pieces[i][0] = x;
					pieces[i][1] = y;
					i++;
			    }
			} else {
				if (board[x][y] == OPP) {
					pieces[i][0] = x;
					pieces[i][1] = y;
					i++;
				}
			}
		}
	} return pieces;
  }
  /**
  * generateAllPossibleMoves() generates an array of all the possible
  * moves that our bot can make.
  **/

  protected boolean isValidMove(int side, int x, int y) {
	  int stotal = 0;
	  int g = 0;
	  int h = 0;
	  int j = 0;
	  int k = 0;

	  if (x > 7 || y > 7 || x < 0 || y < 0) {
		  return false;
	  }
	  if(side == 1) {						// if side is black - edges
	      for(int i = 0; i < BOARD_DIMENSIONS; i++) {
	    	if (x == i && y == 0) {
	    		return false;
	    	}
	    	if (x == i && y == BOARD_DIMENSIONS-1) {
	    		return false;
	    	}
	      }

	    } else if(side == 0) {				//if side is white - edges
	      for(int i = 0; i < BOARD_DIMENSIONS; i++) {
	    	if (x == 0 && y == i) {
	    		return false;
	    	}
	    	if (x == BOARD_DIMENSIONS - 1 && y == i) {
	    		return false;
	    	}
	      }
	    }


	  if (board[x][y] != EMPTY) { 				//if piece already there
		   return false;
	   }

	  if (x == 0) {
		  h = x + 1;
	  } else if (x == 7) {
		  j = x - 1;
	  } else {
		  h = x;
		  j = x;
	  }
	  if (y == 0) {
		  g = y + 1;
	  } else if (y == 7){
		  k = y - 1;
	  } else {
		  g = y;
		  k = y;
	  }
	  if (board[h-1][g-1] == PLAYER)  {
    		stotal++;
    		x = h-1;
    		y = g-1;
    	}	  
	  if (board[h-1][y] == PLAYER) {
		  stotal++;
		  x = h-1;
	  }
	  if (board[h-1][k+1] == PLAYER) {
		  stotal++;
		  x = h-1;
		  y = k+1;
	  }
      if (board[x][g-1] == PLAYER) {
    		stotal++;
    		y = g-1;
    	}
      if (board[x][k+1] == PLAYER) {
    	  stotal++;
    	  y = k + 1;

      }
      if (board[j+1][g-1] == PLAYER) {
    	  	stotal++;
    	  	x = j+1;
    	  	y = g-1;
      }
      if (board[j+1][y] == PLAYER) {
    	  stotal++;
    	  x = j+1;
      }
      if (board[j+1][k+1] == PLAYER) {
    	  stotal++;
    	  x = j+1;
    	  y = k+1;
      }
      if (stotal == 0) {
    	  return true;
      }

      if (stotal == 1) {
    	  if (x == 0) {
    		  h = x + 1;
    	  } else if (x == 7) {
    		  j = x - 1;
    	  } else {
    		  h = x;
    		  j = x;
    	  }
    	  if (y == 0) {
    		  g = y + 1;
    	  } else if (y == 7){
    		  k = y - 1;
    	  } else {
    		  g = y;
    		  k = y;
    	  }
    	  if (board[h-1][g-1] == PLAYER)  {
      		  stotal++;
	      }
	  	  if (board[h-1][y] == PLAYER) {
	  		  stotal++;
	  	  }
	  	  if (board[h-1][k+1] == PLAYER) {
	  		  stotal++;
	  	  }
	      if (board[x][g-1] == PLAYER)  {
	          stotal++;
	      }
	      if (board[x][k+1] == PLAYER) {
	      	  stotal++;
	      }
	      if (board[j+1][g-1] == PLAYER) {
	      	  stotal++;
	      }
	      if (board[j+1][y] == PLAYER) {
	      	  stotal++;
	      }
	      if (board[j+1][k+1] == PLAYER) {
	      	  stotal++;
	      }
      }

      if (stotal >= 2) {
    		return false;
      }	

	  return true;
  }
  
  
  protected DList generateAllPossibleMoves(int side) {
  	  DList moves = new DList(); // what kind of data type do we want for this? dlist? yeah.
      //all generated moves are step moves
      for (int x=0; x < BOARD_DIMENSIONS; x++) {
    	  for (int y=0; y < BOARD_DIMENSIONS; y++) {
    		  if (this.isValidMove(side, x, y)) {
    			  Move validmove = new Move(x, y);
    			  moves.insertFront(validmove);
    		  }
    	  }
      }
      if (this.allPiecesUsed()) {
	      for (int[] piece : this.allPieces(side)) {
	    	  for (int x=0; x < BOARD_DIMENSIONS; x++) {
	        	  for (int y=0; y < BOARD_DIMENSIONS; y++) {
	        		  if (this.isValidMove(side, x, y)) {
	        	    	  Move validmove = new Move(piece[0], piece[1], x, y);
	        			  moves.insertFront(validmove);
	        		  }
	        	  }
	          }
	      }
      }
    return moves;
  }

  protected boolean inGoal(int side, int x, int y) {
	  if(side == 1) {						// if side is black - edges
	      if (x == 0 || x == 7) {
	    	  return true;
	      }
	   }
	  else if(side == 0) {				//if side is white - edges
	      if (y == 0 || y == 7) {
	    	  return true;
	      }
	    }
	  return false;
  }
  protected boolean inFirstGoal(int side, int x, int y) {
	  if(side == 1) {						// if side is black - edges
	      if (x == 0) {
	    	  return true;
	      }
	   }
	  else if(side == 0) {				//if side is white - edges
	      if (y == 0) {
	    	  return true;
	      }
	    }
	  return false;
  }
  protected boolean inSecondGoal(int side, int x, int y) {
	  if(side == 1) {						// if side is black - edges
	      if (x == 7) {
	    	  return true;
	      }
	   }
	  else if(side == 0) {				//if side is white - edges
	      if (y == 7) {
	    	  return true;
	      }
	    }
	  return false;
  }
  
  protected int chipsInGoal(int side) {
	  total = 0;
	  int[][] chips = allPieces(side);
	  for(int i = 0; i < chips.length; i++) {
		  if (inGoal(side, chips[i][0], chips[i][1])) {
			  total++;
		  }
	  }
	  return total;
  }
  
  protected boolean isFull() {
	if (total == 60) {
		return true;
	}
	return false;
  }
  private boolean allPiecesUsed() {
    return (computer_pieces >= 10);
  }

  protected int[][] getBoard() {
    return board;
  }
  
//List the coordinates of connections of piece at coordinate (x,y)

private int[][] listConnections(int x, int y){
	// instantiation of arrays with coordinates
	int[][] sideCoordinates;
	int[][] opposingCoordinates;
	if (board[x][y] == PLAYER){
		sideCoordinates = allPieces(COMP);
		opposingCoordinates = allPieces(HUMAN);
	}else{
		sideCoordinates = allPieces(HUMAN);
		opposingCoordinates = allPieces(COMP);
	}
	
	// if arrays are different lengths
	if (sideCoordinates.length < opposingCoordinates.length){
		int[][] array = new int[sideCoordinates.length + 1][];
		for (int i = 0; i < sideCoordinates.length; i++){
			array[i] = sideCoordinates[i];
		}
		array[array.length][0] = 10; 
		array[array.length][1] = 10; 
		sideCoordinates = array;
		}
	else if (sideCoordinates.length > opposingCoordinates.length){
		int[][] array = new int[opposingCoordinates.length + 1][];
		for (int i = 0; i < opposingCoordinates.length; i++){
			array[i] = opposingCoordinates[i];
		}
		array[array.length][0] = 10; 
		array[array.length][1] = 10; 
		opposingCoordinates = array;
		}
	
	// instantiating reference variables
	int a = -1; int b = -1; int c = 8; int d = 8;
	int e = -1; int f = -1; int g = 8; int h = 8;
	int a1 = x; int b1 = y; int c1 = x; int d1 = y;
	int k = -1; int l = -1; int m = 8; int n = 8;
	int o = -1; int p = -1; int q = 8; int r = 8;
	int s = -1; int t = -1; int u = 8; int v = 8;
	int w = -1; int xx = -1; int yy = 8; int z = 8;
	
	//checking for connections and blocks
	
		for (int j = 0; j < sideCoordinates.length; j++){
			//left to piece
			if (sideCoordinates[j][1] == y && sideCoordinates[j][0] < x && sideCoordinates[j][0] > a){
				a = sideCoordinates[j][0];
			}
			if (opposingCoordinates[j][1] == y && opposingCoordinates[j][0] < x && sideCoordinates[j][0] > b){
				b = opposingCoordinates[j][0];
			}
			
			//right to piece
			if (sideCoordinates[j][1] == y && sideCoordinates[j][0] > x && sideCoordinates[j][0] < c){
				c = sideCoordinates[j][0];
			}
			if (opposingCoordinates[j][1] == y && opposingCoordinates[j][0] > x && sideCoordinates[j][0] < d){
				d = opposingCoordinates[j][0];
			}

			//up to piece
			if (sideCoordinates[j][0] == x && sideCoordinates[j][1] < y && sideCoordinates[j][1] > e){
				e = sideCoordinates[j][0];
			}
			if (opposingCoordinates[j][0] == x && opposingCoordinates[j][1] > y && sideCoordinates[j][1] > f){
				f = opposingCoordinates[j][0];
			}
			
			//down to piece
			if (sideCoordinates[j][0] == x && sideCoordinates[j][1] > y && sideCoordinates[j][1] < g){
				g = sideCoordinates[j][1];
			}
			if (opposingCoordinates[j][0] == x && opposingCoordinates[j][1] > x && sideCoordinates[j][1] < h){
				h = opposingCoordinates[j][0];
			}
	
		while(a >= 0 && c < 8 && b >=0 && d < 8){
			a--; b--; c++; d++;

			//diagonal-left-up to piece
			if (sideCoordinates[j][0] == a1 && sideCoordinates[j][1] == b1 && a1 > k){
				k = a1; l = b1;
			}
			if (opposingCoordinates[j][0] == a1 && sideCoordinates[j][1] == b1 && a1 > o){
				o = a1; p = b1;
			}

			//diagonal-left-down to piece
			if (sideCoordinates[j][0] == a1 && sideCoordinates[j][1] == d1 && a1 > s){
				s = a1;	m = d1;
			}
			if (opposingCoordinates[j][0] == a1 && opposingCoordinates[j][1] == d1 && a1 > t){
				t = a1; n = d1;
			}

			//diagonal-right-up to piece
			if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == b1 && b1 > w){
				q = c1; w = b1;
			}
			if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == b1 && b1 > xx){
				r = c1;	xx = b1;
			}

			//diagonal-right-down to piece
			if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == d1 && d1 < v){
				u = c1;	v = d1;
			}
			
			if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == d1 && d1 < z){
				yy = c1; z = d1;
			}
			
			}
		}
		// determine size of array
		int arraySize = 0;
		if (a > b){
			arraySize++;
		}
		if (c < d){
			arraySize++;
		}
		if (e > f){
			arraySize++;
		}
		if (g < h){
			arraySize++;
		}
		if (k > o){
			arraySize++;
		}
		if (s > t){
			arraySize++;
		}
		if (q > r){
			arraySize++;
		}
		if (u > yy){
			arraySize++;
		}
		// instantiate array
		int[][] list = new int[arraySize][2];
		int index = 0;
		// if not blocked, add coordinate to list
		if (a > b){
			list[index][0] = a;
			list[index][1] = y;
			index++;
		}
		if (c < d){
			list[index][0] = c;
			list[index][1] = y;
			index++;
		}
		if (e > f){
			list[index][0] = x;
			list[index][1] = e;
			index++;
		}
		if (g < h){
			list[index][0] = x;
			list[index][1] = g;
			index++;
		}
		if (g < h){
			list[index][0] = x;
			list[index][1] = g;
			index++;
		}
		if (k > o){
			list[index][0] = k;
			list[index][1] = l;
			index++;
		}
		if (s > t){
			list[index][0] = s;
			list[index][1] = m;
			index++;
		}
		if (q > r){
			list[index][0] = q;
			list[index][1] = w;
			index++;
		}
		if (u > yy){
			list[index][0] = u;
			list[index][1] = v;
			index++;
		}
		return list;
}

protected int maxNumOfConnections(int side){
	int[][] list = allPieces(side);
	int[] array = new int[list.length];
	int[][] list2 = new int[1][];
	for (int i = 0; i < list.length; i++){
		int counter = 0;
		while (list2.length != 0){
			if (list2.length == 0){
				array[i] = counter;
			}else{
				list2 = listConnections(list[i][0], list[i][1]);
				if (list2.length > 0){
					counter++;	
					}
				}
			}
	}
	int max = 0;
	for (int j = 0; j < array.length; j++){
		if (array[j] > max){
		max = array[j];
		}
	}
	return max;
}


  /**
  * toString() prints out a visual representation of the
  * board onto the console.
  **/
  public String toString() {
    String board = "----------------------------------------- \n";
    for(int row = 0; row < 8; row++) {
      board += row + "|";
      for (int x = 0; x < 8; x++) {
    	  board += " " + this.board[x][row] + "_  " ;
      }
      board += "\n-----------------------------------------\n";
    }
    board += "  0_   1_   2_   3_   4_   5_   6_   7_";
    return board;
  }

  // For testing purposes
  protected int getNumPieces() {
    return computer_pieces;
  }

  /**
  * main() is the test class for Grid.
 * @throws InvalidNodeException 
 * @throws InvalidMoveException 
  **/
  public static void main(String[] args) throws InvalidNodeException  {
    Grid g1 = new Grid(0);
    System.out.println("Grid representation of player 0:");
    System.out.println(g1);

    Grid g2 = new Grid(1);
    System.out.println("Grid representation of player 1:");
    System.out.println(g2);
    
    int[][] array = new int[8][8];
    System.out.println("hi" + g1.board[2][2]);
    g1.putPiece(1, 2, 2);
      array[2][2] = 2;
      System.out.println(g1.board[2][2] + "" + array[2][2]);
    	
    	
      Move move = new Move(1, 1); 				//doMove
      g1.doMove(0, move);
      System.out.println(g1.getNumPieces());
      array[1][1] = 1;
      System.out.println(array[1][1]+ "" + g1.board[1][1]);
      
      Move newmove = new Move(0, 1);
      g1.doMove(0, newmove);
      array[0][1] = 0;
      System.out.println(g1.board[0][1] + "" + array[0][1]);
      System.out.println(g1);
      Move move2 = new Move(1, 3);

      g1.doMove(1, move2);


      System.out.println("hi");

      int[][] d = g1.allPieces(1);
      System.out.println(d[0][0]);
      System.out.println(d[0][1]);
      System.out.println(d[1][0]);
      System.out.println(d[1][1]);

      g1.removePiece(1,  2,  2);
      int[][] d1 = g1.allPieces(1);
      System.out.println(d1[0][0]);
      System.out.println(d1[0][1]);
      g1.putPiece(1, 0, 1);
      System.out.println(g1);
      
      System.out.println(g1.hasValidNetwork(1));
      // g1.putPiece(1, 1, 1); //gives error. good.
      // System.out.println(g1.getNumPieces()); 
      /*g1.putPiece(1, 2, 1);
      System.out.println(g1.getNumPieces());
      System.out.println("hi" + g1.total);

      System.out.println(g1);
      int[][] list = g1.listConnections(1,2);
      System.out.println(list);
      g1.putPiece(1, 4, 1);

      System.out.println(g1);
      int[][] newlist = g1.listConnections(1,2);
      System.out.println(newlist[0][0]);
      System.out.println(newlist[0][1]);
      System.out.println(newlist[1][0]);
      System.out.println(newlist[1][1]);
      System.out.println(g1.maxNumOfConnections(0));*/
  }

}