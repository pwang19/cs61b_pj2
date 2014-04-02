package player;

import list.*;

public class Grid {

	private final int BOARD_DIMENSIONS = 8;
	private final int EMPTY = 0;
	protected final int BLACK = 0;
	protected final int WHITE = 1;

	protected int[][] board; // our internal grid representation

	protected int myPiece; // this bot's piece representation
	protected int oppPiece; // opponent's piece representation
	protected int machine;
	protected int opponent;

	private int machinePieces;
	protected int totalPieces; // total number of pieces on the board
	private boolean[][] record; // for countNetwork()

	/**
	 * Instantiates a board, knows which color the MachinePlayer is.
	 * 
	 * @param side
	 *            the side that MachinePlayer is playing
	 **/
	public Grid(int side) {

		// instantiate record with all boolean values of false
		// instantiate board with all values of 0
		record = new boolean[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		board = new int[BOARD_DIMENSIONS][BOARD_DIMENSIONS];

		if (side == BLACK) {
			myPiece = 1;
			oppPiece = 2;
			machine = BLACK;
			opponent = WHITE;
		}
		if (side == WHITE) {
			myPiece = 2;
			oppPiece = 1;
			machine = WHITE;
			opponent = BLACK;
		}
		machinePieces = 0;
		totalPieces = 0;
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
	 *            is BLACK or WHITE
	 * @return true if player "side" has a winning network in "this" GameBoard;
	 *         false otherwise.
	 **/
	protected boolean hasValidNetwork(int side) throws InvalidNodeException {
		if (side == BLACK) {
			for (int x = 1; x < 8; x++) {
				if (board[x][0] == 1) { // if piece is on the board
					if (countNetwork(0, x, 0, 1, -1, -1)) {
						return true;
					}
				}
			}
		} else { // side is white
			for (int y = 1; y < 8; y++) {
				if (board[0][y] == 2) { // if piece is on the board
					if (countNetwork(1, 0, y, 1, -1, -1)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * countNetwork() checks for a valid network that is at least 6 connections
	 * long and spans across the start and end zones of the respective sides.
	 * 
	 * @param side
	 *            either BLACK or WHITE
	 * @param startx
	 *            network's starting piece's x-coordinate
	 * @param starty
	 *            network's starting piece's y-coordinate
	 * @param numConnections
	 *            number of connections in network
	 * @param prevx
	 * @param prevy
	 * @return if true, then there is a valid network
	 * @throws InvalidNodeException
	 */
	private boolean countNetwork(int side, int startx, int starty,
			int numConnections, int prevx, int prevy)
			throws InvalidNodeException {
		if (record[startx][starty] == true) {
			return false; // there was no network
		}

		int check;

		if (side == 0) {
			check = starty;
		} else {
			check = startx;
		}

		// base case: if in first goal, then fail
		if (check == 0) {
			if (numConnections != 1) {
				return false;
			}

			// if in 2nd goal and more than 6 connections, we have a network
		} else if (check == 7) {

			if (numConnections >= 6) {
				return true;
			} else {
				return false;
			}
		}

		// create list of connections from first point in the list
		int[][] conn = listConnections(startx, starty);
		for (int i = 0; i < conn.length; i++) {
			int x = conn[i][0];
			int y = conn[i][1];
			if (prevx != -1) {
				if (ifLine(prevx, prevy, startx, starty, x, y)) {
					continue;
				}
			}

			record[startx][starty] = true;
			if (countNetwork(side, x, y, numConnections + 1, startx, starty)) {
				record[startx][starty] = false;
				return true;
			}
			record[startx][starty] = false;
		}
		return false;

	}

	/**
	 * 
	 * @param prevx
	 * @param prevy
	 * @param startx
	 * @param starty
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean ifLine(int prevx, int prevy, int startx, int starty, int x,
			int y) {
		if (prevx == startx) {
			return prevx == x;
		} else if (prevy == starty) {
			return prevy == y;
		} else if (prevx < startx) {
			if (prevy > starty) {
				return (starty < y && startx > x) || (starty > y && startx < x);
			} else if (prevy < starty) {
				return (starty < y && startx < x) || (starty < y && startx < x);
			}
		} else if (prevx > startx) {
			if (prevy > starty) {
				return (starty < y && startx < x) || (starty > y && startx > x);
			} else if (prevy < starty) {
				return (starty < y && startx > x) || (starty < y && startx > x);
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
		if (side != machine && this.isValidMove(side, x, y)) {
			board[x][y] = oppPiece;
			totalPieces++;
			// when adding our piece, we have to check validity.
			return;
		} else if (side == machine && this.isValidMove(side, x, y)) {
			board[x][y] = myPiece;
			machinePieces++;
			totalPieces++;
			return;
		}
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
		totalPieces--;
		if (side == machine) {
			machinePieces--;
		}
	}

	/**
	 * doMove() will update the internal board representation with the move that
	 * is specified.
	 * 
	 * @param side
	 *            either WHITE or BLACK
	 * @param move
	 *            the move that will be performed
	 */
	protected void doMove(int side, Move move) {
		if (move.moveKind == Move.ADD) {
			this.putPiece(side, move.x1, move.y1);
		}
		if (move.moveKind == Move.STEP) {
			this.removePiece(side, move.x2, move.y2);
			this.putPiece(side, move.x1, move.y1);
		}
	}

	/**
	 * undoMove() will revert the board back to its original state one turn ago.
	 * It accomplishes this task by reversing the move specified.
	 * 
	 * @param side
	 *            either BLACK or WHITE
	 * @param move
	 *            the move that will be reversed
	 */
	protected void undoMove(int side, Move move) {
		if (move.moveKind == Move.ADD) {
			this.removePiece(side, move.x1, move.y1);
		}
		if (move.moveKind == Move.STEP) {
			this.removePiece(side, move.x1, move.y1);
			this.putPiece(side, move.x2, move.y2);
		}
	}

	/**
	 * allPieces() collects all the pieces coordinates of one color on the
	 * board.
	 * 
	 * @param side
	 *            either machine or opponent
	 * @return a 2d array representation of all the coordinates taken by the
	 *         mentioned side
	 */
	protected int[][] allPieces(int side) {
		int index = 0;
		int numPieces = 0;

		if (side == machine) {
			numPieces = machinePieces;
		} else {
			numPieces = totalPieces - machinePieces;
		}

		int[][] pieces = new int[numPieces][2];
		for (int x = 0; x < BOARD_DIMENSIONS; x++) {
			for (int y = 0; y < BOARD_DIMENSIONS; y++) {
				if (side == machine) {
					if (board[x][y] == myPiece) {
						pieces[index][0] = x;
						pieces[index][1] = y;
						index++;
					}
				} else {
					if (board[x][y] == oppPiece) {
						pieces[index][0] = x;
						pieces[index][1] = y;
						index++;
					}
				}
			}
		}
		return pieces;
	}

	/**
	 * isValidMove() checks for if doing a certain move is valid by the given
	 * side.
	 * 
	 * @param side
	 *            machine or opponent
	 * @param m
	 *            the move being checked
	 * @return boolean of whether the move is valid or not
	 */
	protected boolean isValidMove(int side, Move m) {
		if (m.moveKind == Move.STEP) {
			removePiece(side, m.x2, m.y2);
			boolean ret = isValidMove(side, m.x1, m.y1);
			putPiece(side, m.x2, m.y2);
			return ret;
		}
		return isValidMove(side, m.x1, m.y1);
	}

	/**
	 * isValidMove() checks if placing a piece at coordinate(x, y) would be a
	 * valid move for the given side.
	 * 
	 * @param side
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return boolean of whether placing a piece at coordinate(x, y) would be
	 *         valid for a particular side or not
	 */
	protected boolean isValidMove(int side, int x, int y) {
		int stotal = 0;

		boolean[][] visited = new boolean[BOARD_DIMENSIONS][BOARD_DIMENSIONS];

		// make sure index is not out of bounds
		if (x > 7 || y > 7 || x < 0 || y < 0) {
			return false;
		}

		if (side == 1) { // if side is white - edges
			if (y == 0) {
				return false;
			}
			if (y == BOARD_DIMENSIONS - 1) {
				return false;
			}
		} else { // if side is black - edges
			if (x == 0) {
				return false;
			}
			if (x == BOARD_DIMENSIONS - 1) {
				return false;
			}
		}

		if (board[x][y] != EMPTY) { // if is piece already there
			return false;
		}

		side += 1; // change from player to piece on board

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((x + i) < 0 || (y + j) < 0 || (x + i) > 7 || (y + j) > 7) {
					continue;
				}
				if (isValid(side, x + i, y + j, visited)) {
					stotal++;
					visited[x + i][y + j] = true;
					for (int k = -1; k < 2; k++) {
						for (int l = -1; l < 2; l++) {
							if ((x + i + k) < 0 || (y + j + l) < 0
									|| (x + i + k) > 7 || (y + j + l) > 7) {
								continue;
							}
							if (isValid(side, x + i + k, y + j + l, visited)) {
								return false;
							}
						}
					}
				}
			}
		}
		if (stotal > 1) {
			return false;
		}
		return true;
	}

	/**
	 * isValid() helps isValidMove(). Checks if there is a piece at coordinate
	 * (x, y) that is a piece of the given side, and only returns true if the
	 * piece has never been visited before. This helps count the number of
	 * pieces in a piece's immediate vicinity to check for clusters.
	 * 
	 * @param side
	 *            machine or opponent
	 * @param x
	 *            x coordinate being checked
	 * @param y
	 *            y coordinate being checked
	 * @param visit
	 *            a doubly linked list containing booleans of which coordinates
	 *            have already been visited and which have not
	 * @return true if the coordinate contains a piece of specified side that
	 *         has not been visited yet, false if not
	 */
	private boolean isValid(int side, int x, int y, boolean[][] visit) {
		if (board[x][y] == side && visit[x][y] == false) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * generateAllPossibleMoves() generates a list of all the possible moves
	 * that the MachinePlayer can make at the moment. If it has placed less than
	 * 10 pieces on the board, then it will generate an array of valid placement
	 * moves. If there is already 10 pieces on the board, however, it will try
	 * to find ways to move a piece to complete a network.
	 * 
	 * @param side
	 *            either WHITE or BLACK
	 * @return a doubly linked list representing all the different possible
	 *         moves that this player is able to make at the time.
	 */
	protected DList generateAllPossibleMoves(int side) {
		DList moves = new DList();

		// all generated moves are step moves
		if (this.allPiecesUsed()) {
			for (int[] piece : this.allPieces(side)) {
				removePiece(side, piece[0], piece[1]);
				for (int x = 0; x < BOARD_DIMENSIONS; x++) {
					for (int y = 0; y < BOARD_DIMENSIONS; y++) {
						if (piece[0] == x && piece[1] == y) {
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

			// all generated moves are placement moves
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

	/**
	 * Determines whether or not the machine player has used all its pieces.
	 * Helper function to generateAllPossibleMoves().
	 * 
	 * @return true if 10 machine pieces are used
	 */
	private boolean allPiecesUsed() {
		return machinePieces >= 10;
	}

	/**
	 * Lists the coordinates of connections of piece at coordinate (x,y)
	 * 
	 * @param x
	 *            x-coordinate of piece
	 * @param y
	 *            y-coordinate of piece
	 * @return an array of connecting coordinates
	 */
	private int[][] listConnections(int x, int y) {
		int[][] sideCoordinates;
		int[][] opposingCoordinates;

		if (board[x][y] == myPiece) {
			sideCoordinates = allPieces(machine);
			opposingCoordinates = allPieces(opponent);
		} else {
			sideCoordinates = allPieces(opponent);
			opposingCoordinates = allPieces(machine);
		}

		if (sideCoordinates.length < opposingCoordinates.length) {
			sideCoordinates = increaseArrayLength(sideCoordinates);

		} else if (sideCoordinates.length > opposingCoordinates.length) {
			opposingCoordinates = increaseArrayLength(opposingCoordinates);
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

			// looking for diagonal connections
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
		// instantiate output array
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

	/**
	 * increaseArrayLength() will take an array and add one length to it. This
	 * is a helper method to listConnections().
	 * 
	 * @param side
	 * @return
	 */
	private int[][] increaseArrayLength(int[][] side) {
		int[][] array = new int[side.length + 1][2];
		for (int i = 0; i < side.length; i++) {
			array[i] = side[i];
		}
		array[array.length - 1][0] = 10;
		array[array.length - 1][1] = 10;
		return array;
	}

	/**
	 * totalNumberConnections() returns a count of the longest network for a
	 * particular side.
	 * 
	 * @param side
	 *            either machine or opponent
	 * @return the longest network represented by this side
	 */
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

	/**
	 * numberPiecesInGoal() figures out how many pieces reside at a specific
	 * goal line.
	 * 
	 * @param side
	 *            either BLACK or WHITE
	 * @param start
	 *            true = start goal false = end goal
	 * @return number of pieces in the goal line
	 */
	protected int numberPiecesInGoal(int side, boolean start) {
		int counter = 0;
		for (int goalZoneIndex = 1; goalZoneIndex < 7; goalZoneIndex++) {

			// check black start zone for black pieces
			if (side == BLACK && start && board[goalZoneIndex][0] == 1) {
				counter += 1;

				// check black end zone for black pieces
			} else if (side == BLACK && !start
					&& board[goalZoneIndex][BOARD_DIMENSIONS - 1] == 1) {
				counter += 1;

				// check white start zone for white pieces
			} else if (side == WHITE && start && board[0][goalZoneIndex] == 2) {
				counter += 1;

				// check white end zone for white pieces
			} else if (side == WHITE && !start
					&& board[BOARD_DIMENSIONS - 1][goalZoneIndex] == 2) {
				counter += 1;
			}
		}
		return counter;
	}

	/**
	 * toString() prints out a visual representation of the board onto the
	 * console.
	 * 
	 * @return a String representation of the board
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

	/**
	 * pieceCoordinatesToString() will output a String representation of the
	 * coordinates of all the pieces of a side. This method is used for testing
	 * purposes only.
	 * 
	 * @param coordinates
	 *            a group of coordinates expressed in 2d array form
	 * @return a String representation of the coordinates
	 */
	public String pieceCoordinatesToString(int[][] coordinates) {
		String list = "[ ";
		for (int i = 0; i < coordinates.length; i++) {
			list = list + "( " + coordinates[i][0] + ", " + coordinates[i][1]
					+ " ) ";
		}
		list = list + "]";
		return list;
	}

	/**
	 * main() is the test class for Grid. This is not meant to be graded.
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
		System.out.println(g1.pieceCoordinatesToString(g1.allPieces(0)));
		System.out.println(g1.pieceCoordinatesToString(g1.allPieces(1)));
		System.out.println(g1.pieceCoordinatesToString(list));

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
		// try {
		System.out
				.println(g2.pieceCoordinatesToString(g2.listConnections(3, 1)));
		// System.out.println(g2.hasValidNetwork(0));
		// } catch (InvalidNodeException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
