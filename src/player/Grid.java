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

	private int computer_pieces;
	protected int total;
	private boolean[][] record;

	/**
	 * Instantiate a board
	 **/

	public Grid(int side) {
		record = new boolean[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		board = new int[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		for (int x = 0; x < BOARD_DIMENSIONS; x++) {
			for (int y = 0; y < BOARD_DIMENSIONS; y++) {
				board[x][y] = EMPTY;
				record[x][y] = false;
				//System.out.println("bye" + board[x][y]);
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
	 * hasValidNetwork() determines whether "this" GameBoard has a valid network
	 * for player "side". (Does not check whether the opponent has a network.) A
	 * full description of what constitutes a valid network appears in the
	 * project "readme" file.
	 * 
	 * Unusual conditions: If side is neither MachinePlayer.COMPUTER nor
	 * MachinePlayer.OPPONENT, returns false. If GameBoard squares contain
	 * illegal values, the behavior of this method is undefined (i.e., don't
	 * expect any reasonable behavior).
	 * 
	 * @param side
	 *            is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
	 * @return true if player "side" has a winning network in "this" GameBoard;
	 *         false otherwise.
	 **/
	protected boolean hasValidNetwork(int side) throws InvalidNodeException {
		if (side == 0){
			for (int x = 1; x < 8; x++){
				if (board[x][0] == 1){ //if black
					if (countNetwork(0, x, 0, 1, -1, -1)) {
						return true;
					}
				}
			}
		} else {
			for (int y = 1; y < 8; y++){
				if (board[0][y] == 2){ //if white
					if (countNetwork(1, 0, y, 1, -1, -1)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	protected boolean countNetwork(int side, int startx, int starty, int con, int prevx, int prevy) throws InvalidNodeException {
	  if (record[startx][starty]==true){
		  return false; // there was no network
	  }
	  int check;
	  
	  if(side == 0) {
		  check = starty;
	  } else {
		  check = startx;
	  }
	  if (check == 0) {		// base case: if in first goal, then fail
		  if(con != 1) {
			  return false;
		  }
	  } else if (check == 7) {
		  if (con >= 6) {   // if in 2nd goal and more than 6 connections, we have a network! yay
			  return true;
		  } else {
			  return false;
		  }
	  }
	int[][] conn = listConnections(startx, starty);  		// create list of connections from first point in the list
	for (int i = 0; i < conn.length; i++){
		int x = conn[i][0]; int y = conn[i][1];
		if(prevx != -1) {
			if (ifLine(prevx,prevy,startx,starty,x,y)){
				continue;
			}
		}
		
		record[startx][starty] = true;
		if (countNetwork(side, x, y, con + 1, startx, starty)) {
			record[startx][starty] = false;
			return true;
		}
		record[startx][starty] = false;
	}
	return false;
	
}
	
	private boolean ifLine(int prevx, int prevy, int startx, int starty,int x,int y){
		if (prevx == startx){
			return prevx == x;
		}
		else if (prevy == starty){
			return prevy == y;
		}
		else if (prevx < startx){
			if (prevy > starty){
				return (starty<y && startx>x) || (starty>y && startx<x);
			}
			else if (prevy < starty){
				return (starty<y && startx<x) || (starty<y && startx<x);
			}
		}
		else if (prevx > startx){
			if (prevy > starty){
				return (starty<y && startx<x) || (starty>y && startx>x);
			}
			else if (prevy < starty){
				return (starty<y && startx>x) || (starty<y && startx>x);
			}
		}
		return false;
	}

	/**
	 * putPiece() puts a piece on the board a piece on the board.
	 * 
	 * @param side
	 *            either COMPUTER or OPPONENT
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @throws InvalidMoveException
	 */
	protected void putPiece(int side, int x, int y) {
		if (side != COMP && this.isValidMove(side, x, y)) {
			board[x][y] = OPP;
			//System.out.println("derp");
			total++;
			// when adding our piece, we have to check validity.
			return;
		} else if (side == COMP && this.isValidMove(side, x, y)) {
			board[x][y] = PLAYER;
			//System.out.println("herp");
			computer_pieces++;
			total++;
			return;
		}
		
		//System.out.println("\n\n\n OMG JENNIFER");
	}

	/**
	 * removePiece() removes a piece on the board at the specified coordinates.
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
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
			this.removePiece(side, move.x2, move.y2);
			this.putPiece(side, move.x1, move.y1);
		}
	}

	protected void undoMove(int side, Move move) {
		if (move.moveKind == Move.ADD) {
			this.removePiece(side, move.x1, move.y1);
		}
		if (move.moveKind == Move.STEP) {
			this.removePiece(side, move.x1, move.y1);
			this.putPiece(side, move.x2, move.y2);
		}
	}

	protected int[][] allPieces(int side) {
		int i = 0;
		int d = 0;
		if (side == COMP) {
			d = computer_pieces;
		} else {
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
		}
		return pieces;
	}
	
	protected boolean isValidMove(int side, Move m) {
		if(m.moveKind == Move.STEP) {
			removePiece(side, m.x2, m.y2);
			boolean ret = isValidMove(side, m.x1, m.y1);
			putPiece(side, m.x2, m.y2);
			return ret;
		}
		return isValidMove(side, m.x1, m.y1);
	}

	/**
	 * generateAllPossibleMoves() generates an array of all the possible moves
	 * that our bot can make.
	 **/

	protected boolean isValidMove(int side, int x, int y) {
		int stotal = 0;
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int e = 0;
		int f = 0;

		boolean[][] visited = new boolean[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		if (x > 7 || y > 7 || x < 0 || y < 0) {
			return false;
		}
		if (side == 1) { 						// if side is white - edges
			if (y == 0) {
//				System.out.println("1");
				return false;
			}
			if (y == BOARD_DIMENSIONS - 1) {
//				System.out.println("2");
				return false;
			}
		} else { 				// if side is black - edges
			if (x == 0) {
//				System.out.println("3");
				return false;
			}
			if (x == BOARD_DIMENSIONS - 1) {
//				System.out.println("4");
				return false;
			}
		}


		if (board[x][y] != EMPTY) { 			// if is piece already there
//			System.out.println("5");
			return false;
		}
		if (side == COMP) {
			side = PLAYER;
		} else if (side == HUMAN) {
			side = OPP;
		}
		
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((x+i) < 0 || (y+j) < 0 || (x+i) > 7 || (y+j) > 7) {
					continue;
				}
				if (isValid(side, x+i, y+j, visited)) {
					stotal++;
					visited[x+i][y+j] = true;
					for (int k = -1; k < 2; k++) {
						for (int l = -1; l < 2; l++) {
							if ((x+i+k) < 0 || (y+j+l) < 0 || (x+i+k) > 7 || (y+j+l) > 7) {
								continue;
							}
							if(isValid(side, x+i+k, y+j+l, visited)) {
//								System.out.println("Double");
								return false;
							}
						}
					}
				}
			}
		}
		if (stotal > 1) {
//			System.out.println("stotal > 1");
			return false;
		}

//		if (x == 0) {
//			d = x + 1;
//			e = x;
//		} else if (x == 7) {
//			e = x - 1;
//			d = x;
//		} else {
//			d = x;
//			e = x;
//		}
//		if (y == 0) {
//			c = y + 1;
//			f = y;
//		} else if (y == 7) {
//			f = y - 1;
//			c = y;
//		} else {
//			c = y;
//			f = y;
//		}
//
//		int[] m = {d-1, d-1, d-1, d, d, e+1, e+1, e+1};
//		int[] n = {c-1, c, f+1, c-1, f+1, c-1, c, f+1};
//		
//		for (int i = 0; i < m.length; i++) {
//			if (isValid(side, m[i], n[i], visited)) {
//				if (m[i] != x || n[i] != y) {
//					stotal++;
//					a = m[i];
//					b = n[i];
//					visited[m[i]][n[i]] = true;
//				}
//			}
//			
//		}

//
//		if (stotal == 1) {
//			if (a == 0) {
//				d = a + 1;
//				e = a;
//			} else if (a == 7) {
//				e = a - 1;
//				d = a;
//			} else {
//				d = a;
//				e = a;
//			}
//			if (b == 0) {
//				c = b + 1;
//				f = b;
//			} else if (b == 7) {
//				f = b - 1;
//				c = b;
//			} else {
//				c = b;
//				f = b;
//			}
//			
//			int[] p = {d-1, d-1, d-1, d, d, e+1, e+1, e+1};
//			int[] q = {c-1, c, f+1, c-1, f+1, c-1, c, f+1};
//			
//			for (int o = 0; o < p.length; o++) {
//				if (isValid(side, p[o], q[o], visited)){
//					if ((p[o] != a || q[o] != b) && (p[o] != x || q[o] != y)) {
//						stotal++;
//						visited[p[o]][q[o]] = true;
//					}
//				}
//			}
//		}
//		if (stotal >= 2) {
//			System.out.println("stotal"+x+" "+y);
//			return false;
//		}
		return true;
	}
	
	private boolean isValid(int side, int x, int y, boolean[][] visit) {
		if (board[x][y] == side && visit[x][y] == false) {
			return true;
		} else {
			return false;
		}
	}


	protected list.DList generateAllPossibleMoves(int side) {
		list.DList moves = new list.DList(); // what kind of data type do we
												// want for this? dlist? yeah.
		// all generated moves are step moves
		if (this.allPiecesUsed()) {
			for (int[] piece : this.allPieces(side)) {
				removePiece(side, piece[0], piece[1]);
				for (int x = 0; x < BOARD_DIMENSIONS; x++) {
					for (int y = 0; y < BOARD_DIMENSIONS; y++) {
						if(piece[0] == x && piece[1] == y) {
							continue;
						}
						if (this.isValidMove(side, x, y)) {
							Move validmove = new Move(x, y, piece[0], piece[1]);
							moves.insertFront(validmove);
						}
					}
				}
				putPiece(side, piece[0], piece[1]);
			}
		} else {
			for (int x = 0; x < BOARD_DIMENSIONS; x++) {
				for (int y = 0; y < BOARD_DIMENSIONS; y++) {
					if (this.isValidMove(side, x, y)) {
						Move validmove = new Move(x, y);
						moves.insertFront(validmove);
					}
				}
			}
		}
		return moves;
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

	// List the coordinates of connections of piece at coordinate (x,y)
	private int[][] listConnections(int x, int y) {
		// instantiation of arrays with coordinates
		int[][] sideCoordinates;
		int[][] opposingCoordinates;
		if (board[x][y] == PLAYER) {
			sideCoordinates = allPieces(COMP);
			opposingCoordinates = allPieces(HUMAN);
		} else {
			sideCoordinates = allPieces(HUMAN);
			opposingCoordinates = allPieces(COMP);
		}

		// if arrays are different lengths
		if (sideCoordinates.length < opposingCoordinates.length) {
			int[][] array = new int[sideCoordinates.length + 1][2];
			for (int i = 0; i < sideCoordinates.length; i++) {
				array[i] = sideCoordinates[i];
			}
			array[array.length - 1][0] = 10;
			array[array.length - 1][1] = 10;
			sideCoordinates = array;
		} else if (sideCoordinates.length > opposingCoordinates.length) {
			int[][] array = new int[opposingCoordinates.length + 1][2];
			for (int i = 0; i < opposingCoordinates.length; i++) {
				array[i] = opposingCoordinates[i];
			}
			array[array.length - 1][0] = 10;
			array[array.length - 1][1] = 10;
			opposingCoordinates = array;
		}

		// instantiating reference variables
		int a = -1;
		int b = -1;
		int c = 8;
		int d = 8;
		int e = -1;
		int f = -1;
		int g = 8;
		int h = 8;
		int a1 = x;
		int b1 = y;
		int c1 = x;
		int d1 = y;
		int k = -1;
		int l = -1;
		int m = 8;
		int n = 8;
		int o = -1;
		int p = -1;
		int q = 8;
		int r = 8;
		int s = -1;
		int t = -1;
		int u = 8;
		int v = 8;
		int w = -1;
		int xx = -1;
		int yy = 8;
		int z = 8;

		// checking for connections and blocks

		for (int j = 0; j < sideCoordinates.length; j++) {
			// left to piece
			if (sideCoordinates[j][1] == y && sideCoordinates[j][0] < x
					&& sideCoordinates[j][0] > a) {
				a = sideCoordinates[j][0];
			}
			if (opposingCoordinates[j][1] == y && opposingCoordinates[j][0] < x
					&& sideCoordinates[j][0] > b) {
				b = opposingCoordinates[j][0];
			}

			// right to piece
			if (sideCoordinates[j][1] == y && sideCoordinates[j][0] > x
					&& sideCoordinates[j][0] < c) {
				c = sideCoordinates[j][0];
			}
			if (opposingCoordinates[j][1] == y && opposingCoordinates[j][0] > x
					&& sideCoordinates[j][0] < d) {
				d = opposingCoordinates[j][0];
			}

			// up to piece
			if (sideCoordinates[j][0] == x && sideCoordinates[j][1] < y
					&& sideCoordinates[j][1] > e) {
				e = sideCoordinates[j][1];
			}
			if (opposingCoordinates[j][0] == x && opposingCoordinates[j][1] < y
					&& sideCoordinates[j][1] > f) {
				f = opposingCoordinates[j][1];
			}

			// down to piece
			if (sideCoordinates[j][0] == x && sideCoordinates[j][1] > y
					&& sideCoordinates[j][1] < g) {
				g = sideCoordinates[j][1];
			}
			if (opposingCoordinates[j][0] == x && opposingCoordinates[j][1] > y
					&& sideCoordinates[j][1] < h) {
				h = opposingCoordinates[j][1];
			}

			while (a1 >= 0 || c1 < 8 || b1 >= 0 || d1 < 8) {
				a1--;
				b1--;
				c1++;
				d1++;
				// diagonal-left-up to piece
				if (sideCoordinates[j][0] == a1 && sideCoordinates[j][1] == b1
						&& a1 > k) {
					k = a1;
					l = b1;
				}
				if (opposingCoordinates[j][0] == a1
						&& opposingCoordinates[j][1] == b1 && a1 > o) {
					o = a1;
					p = b1;
				}

				// diagonal-left-down to piece
				if (sideCoordinates[j][0] == a1 && sideCoordinates[j][1] == d1
						&& a1 > s) {
					s = a1;
					m = d1;
				}
				if (opposingCoordinates[j][0] == a1
						&& opposingCoordinates[j][1] == d1 && a1 > t) {
					t = a1;
					n = d1;
				}

				// diagonal-right-up to piece
				if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == b1
						&& b1 > w) {
					q = c1;
					w = b1;
				}
				if (opposingCoordinates[j][0] == c1
						&& opposingCoordinates[j][1] == b1 && b1 > xx) {
					r = c1;
					xx = b1;
				}

				// diagonal-right-down to piece
				if (sideCoordinates[j][0] == c1 && sideCoordinates[j][1] == d1
						&& d1 < v) {
					u = c1;
					v = d1;
				}

				if (opposingCoordinates[j][0] == c1
						&& opposingCoordinates[j][1] == d1 && d1 < z) {
					yy = c1;
					z = d1;
				}
			}
			a1 = x;
			b1 = y;
			c1 = x;
			d1 = y;
		}
		// determine size of array
		int arraySize = 0;
		if (a > b) {
			arraySize++;
		}
		if (c < d) {
			arraySize++;
		}
		if (e > f) {
			arraySize++;
		}
		if (g < h) {
			arraySize++;
		}
		if (k > o) {
			arraySize++;
		}
		if (s > t) {
			arraySize++;
		}
		if (q < r) {
			arraySize++;
		}
		if (u < yy) {
			arraySize++;
		}
		// instantiate array
		int[][] list = new int[arraySize][2];
		int index = 0;
		// if not blocked, add coordinate to list
		if (a > b) {
			list[index][0] = a;
			list[index][1] = y;
			index++;
		}
		if (c < d) {
			list[index][0] = c;
			list[index][1] = y;
			index++;
		}
		if (e > f) {
			list[index][0] = x;
			list[index][1] = e;
			index++;
		}
		if (g < h) {
			list[index][0] = x;
			list[index][1] = g;
			index++;
		}
		if (k > o) {
			list[index][0] = k;
			list[index][1] = l;
			index++;
		}
		if (s > t) {
			list[index][0] = s;
			list[index][1] = m;
			index++;
		}
		if (q < r) {
			list[index][0] = q;
			list[index][1] = w;
			index++;
		}
		if (u < yy) {
			list[index][0] = u;
			list[index][1] = v;
			index++;
		}
		return list;
	}

	protected int totalNumberConnections(int side) {
		int total = 0;
		int[][] listPieces = allPieces(side);
		for (int i = 0; i < listPieces.length; i++) {
			int[][] listConnections = listConnections(listPieces[i][0],
					listPieces[i][1]);
			total += listConnections.length;
		}
		return total;
	}

	// returns max number of connected connections of a side

	protected int numberPiecesInGoal(int side, boolean start){
		// true = start goal; false = end goal
		int counter = 0;
			for (int x = 1; x < 7; x++){
				if (side == 0 && start && board[x][0] == 1){
					counter+=1;
				}
				else if (side == 0 && !start && board[x][7] == 1){
					counter+=1;
				}
				else if (side == 1 && start && board[0][x] == 2){
					counter+=1;
				}
				else if (side == 1 && !start && board[7][x] == 2){
					counter+=1;
				}
			}
			return counter;
		}
	
	/**
	 * private int maxNumOfConnections(int side){ int[][] list =
	 * allPieces(side); int[] array = new int[list.length]; int[][] list2 = new
	 * int[1][]; for (int i = 0; i < list.length; i++){ int counter = 0; while
	 * (list2.length != 0){ if (list2.length == 0){ array[i] = counter; }else{
	 * list2 = listConnections(list[i][0], list[i][1]); if (list2.length > 0){
	 * counter++; } } } } int max = 0; for (int j = 0; j < array.length; j++){
	 * if (array[j] > max){ max = array[j]; } } return max; }
	 **/

	/**
	 * toString() prints out a visual representation of the board onto the
	 * console.
	 **/
	public String toString() {
		String board = "----------------------------------------- \n";
		for (int row = 0; row < 8; row++) {
			board += row + "|";
			for (int x = 0; x < 8; x++) {
				board += " " + this.board[x][row] + "_  ";
			}
			board += "\n-----------------------------------------\n";
		}
		board += "  0_   1_   2_   3_   4_   5_   6_   7_";
		return board;
	}

	// For testing purposes
	private int getNumPieces() {
		return computer_pieces;
	}

	public String toString2(int[][] x) {

		String list = "[ ";
		for (int i = 0; i < x.length; i++) {
			list = list + "( " + x[i][0] + ", " + x[i][1] + " ) ";
		}
		list = list + "]";
		return list;
	}

	/**
	 * main() is the test class for Grid.
	 * 
	 * @throws InvalidMoveException
	 **/
	public static void main(String[] args) {
		Grid g1 = new Grid(0);
		System.out.println("Grid representation of player 0:");
		System.out.println(g1);

		Grid g2 = new Grid(1);
		System.out.println("Grid representation of player 1:");
		System.out.println(g2);

		/**
		 * int[][] array = new int[8][8]; System.out.println("hi" +
		 * g1.board[2][2]); //g1.putPiece(1, 2, 2); array[2][2] = 2;
		 * System.out.println(g1.board[2][2] + "" + array[2][2]);
		 * 
		 * 
		 * Move move = new Move(1, 1); //doMove g1.doMove(0, move);
		 * System.out.println(g1.getNumPieces()); array[1][1] = 1;
		 * System.out.println(array[1][1]+ "" + g1.board[1][1]);
		 * 
		 * Move newmove = new Move(0, 1); g1.doMove(0, newmove); array[0][1] =
		 * 0; System.out.println(g1.board[0][1] + "" + array[0][1]); //
		 * g1.putPiece(1, 1, 1); //gives error. good. //
		 * System.out.println(g1.getNumPieces()); // g1.putPiece(1, 3, 1);
		 * System.out.println(g1.getNumPieces()); System.out.println("hi" +
		 * g1.total);
		 **/
		
		g1.putPiece(0, 4, 0);
		g1.putPiece(1, 0, 4);
		g1.putPiece(0, 4, 4);
		g1.putPiece(1, 5, 6);
		g1.putPiece(0, 2, 4);
		g1.putPiece(1, 5, 5); // change this
		g1.putPiece(0, 6, 4);
		g1.putPiece(1, 6, 1);
		g1.putPiece(0, 2, 2);
		g1.putPiece(1, 1, 1);
		g1.putPiece(0, 6, 2);
		g1.putPiece(1, 3, 6);
		g1.putPiece(0, 6, 6);
		g1.putPiece(1, 7, 3);
		g1.putPiece(0, 2, 6);
		System.out.println(g1);
		int[][] list = g1.listConnections(4, 4);
		System.out.println(g1.toString2(g1.allPieces(0)));
		System.out.println(g1.toString2(g1.allPieces(1)));
		System.out.println(g1.toString2(list));
		
		System.out.println(g1.numberPiecesInGoal(1, false));
		
		  g2.putPiece(1, 0, 1);
	      g2.putPiece(1, 2, 2);
	      g2.putPiece(1, 6, 1);
	      g2.putPiece(1, 2, 3);
	      g2.putPiece(1, 4, 4);
	      g2.putPiece(1, 7, 4);
	      g2.putPiece(0, 1, 0);
	      g2.putPiece(0, 3, 4);
	      g2.putPiece(0, 6, 4);
	      g2.putPiece(0, 1, 6);
	      g2.putPiece(0, 6, 7);
	      g2.putPiece(0, 3, 1);
		  System.out.println(g2);
	    //  try {
	    	System.out.println(g2.toString2(g2.listConnections(3, 1)));
			//System.out.println(g2.hasValidNetwork(0));
		//} catch (InvalidNodeException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
	//	}
	}

}
