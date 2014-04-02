/* MachinePlayer.java */

package player;

import list.*;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {
	private int machine;
	private int opponent;

	private Grid board; // this machine's internal board representation
	int searchDepth; // this machine's search depth

	/**
	 * Creates a machine player with the given color. Color is either 0 (black)
	 * or 1 (white). (White has the first move.)
	 * 
	 * @param color
	 *            this machine player's color
	 */
	public MachinePlayer(int color) {
		// instantiate a board that understands that this machine
		// is a certain color
		board = new Grid(color);

		// assign machine color
		if (color == board.BLACK)
			machine = board.BLACK;
		if (color == board.WHITE)
			machine = board.WHITE;

		// assign opponent color
		opponent = 1 - machine;

		// assign search depth to maximum search depth
		// that we can afford when constrained to 5 seconds
		this.searchDepth = 3;

	}

	/**
	 * Creates a machine player with the given color and search depth. Color is
	 * either 0 (black) or 1 (white). (White has the first move.)
	 * 
	 * @param color
	 *            this machine player's color
	 * @param searchDepth
	 *            this machine player's search depth
	 */
	public MachinePlayer(int color, int searchDepth) {
		// instantiate a board that understands that this machine
		// is a certain color
		board = new Grid(color);

		// assign machine color
		if (color == board.BLACK)
			machine = board.BLACK;
		if (color == board.WHITE)
			machine = board.WHITE;

		// assign opponent color
		opponent = 1 - machine;

		// assign search depth
		this.searchDepth = searchDepth;
	}

	/**
	 * Returns a new move by "this" player. Internally records the move (updates
	 * the internal game board) as a move by "this" player.
	 * 
	 * @return the machine player's chosen move
	 */
	public Move chooseMove() {

		// algorithm to find best possible move
		Best bestMove = miniMax(machine, -1234567, 1234567, 0);

		// place piece on board
		Move chosenMove = bestMove.move;
		board.doMove(machine, chosenMove);

		// return the move
		return chosenMove;
	}

	/**
	 * If the Move m is legal, records the move as a move by the opponent
	 * (updates the internal game board) and returns true. If the move is
	 * illegal, returns false without modifying the internal state of "this"
	 * player.
	 * 
	 * @param m
	 *            the proposed move
	 * @return if true, then move is successful
	 **/
	public boolean opponentMove(Move m) {

		// if it isn't a valid move, then do nothing and return false
		if (!board.isValidMove(opponent, m)) {
			return false;
		}

		// do the move
		board.doMove(opponent, m);
		return true;
	}

	/**
	 * If the Move m is legal, records the move as a move by "this" player
	 * (updates the internal game board) and returns true. If the move is
	 * illegal, returns false without modifying the internal state of "this"
	 * player.
	 * 
	 * @param m
	 *            the proposed move
	 * @return if true, then move is successful
	 **/
	public boolean forceMove(Move m) {

		// if it isn't a valid move, then do nothing and return false
		if (!board.isValidMove(machine, m)) {
			return false;
		}

		// do the move
		board.doMove(machine, m);
		return true;
	}

	/**
	 * miniMax() calculates the best move within a given set of moves for a
	 * given search depth. Utilizes alpha-beta pruning technique.
	 * 
	 * 
	 * @param side
	 *            the color
	 * @param alpha
	 *            machine score
	 * @param beta
	 *            opponent score
	 * @param searchDepth
	 *            the search depth complexity
	 * @return the best calculated possible move
	 **/
	private Best miniMax(int side, int alpha, int beta, int searchDepth) {
		Best myBest = new Best(); // Machine's best move
		Best reply; // Opponent's best reply
		int otherSide;

		// assign otherSide
		if (side == board.BLACK) {
			otherSide = board.WHITE;
		} else {
			otherSide = board.BLACK;
		}

		// if there are already valid networks, then there's no need to look any
		// further
		try {
			if (board.hasValidNetwork(machine)) {
				myBest.score = eval() - searchDepth;
				return myBest;
			}
			if (board.hasValidNetwork(side) || searchDepth >= this.searchDepth
					|| board.hasValidNetwork(otherSide)) {
				myBest.score = eval();
				return myBest;
			}
		} catch (InvalidNodeException e1) {
			// do nothing
		}

		// assign machine score as alpha
		// assign opponent score as beta
		if (side == machine) {
			myBest.score = alpha;
		} else {
			myBest.score = beta;
		}

		// generate a list of all the possible moves
		DList moves = board.generateAllPossibleMoves(side);
		DListNode pointer;

		try {
			// the minimax algorithm with alpha beta pruning
			pointer = (DListNode) moves.front();
			myBest.move = (Move) pointer.item();

			while (pointer.isValidNode()) {

				// make a move and a response
				Move m = (Move) pointer.item();
				board.doMove(side, m);
				reply = miniMax(otherSide, alpha, beta, searchDepth + 1);

				// reset the board
				board.undoMove(side, m);

				// analyze the outcome, and assign the score to alpha/beta
				if (side == machine && reply.score > myBest.score) {
					myBest.move = m;
					myBest.score = reply.score;
					alpha = reply.score;
				} else if (side == opponent && reply.score < myBest.score) {
					myBest.move = m;
					myBest.score = reply.score;
					beta = reply.score;
				}

				// if machine's score is better than opponent, then return the
				// move. if this is not the case, then do not return the move,
				// and prune this branch
				if (alpha >= beta) {
					return myBest;
				}
				pointer = (DListNode) pointer.next();
			}
		} catch (InvalidNodeException e) {
			// e.printStackTrace();
		}
		return myBest;
	}

	/**
	 * This is the evaluation function, which assigns a value to each potential
	 * move so that the AI can give the best possible move.
	 * 
	 * @return the assigned value of this move
	 */
	private int eval() {

		// if there is already a valid network, no calculation needed
		try {
			if (board.hasValidNetwork(machine)) {
				return 100;
			} else if (board.hasValidNetwork(opponent)) {
				return -100;
			}
		} catch (InvalidNodeException e) {
			e.printStackTrace();
		}

		// score will keep track of our value
		int score = 0;
		
		// step 1. prioritize putting pieces in goal zones
		// if machine has 1 piece in start goal
		if (board.numberPiecesInGoal(machine, true) == 1) {
			score += 1.5;
		}

		// if machine has 2 or more pieces in the start goal
		if (board.numberPiecesInGoal(machine, true) > 1) {
			score -= board.numberPiecesInGoal(machine, true) * 3;
		}

		// if machine has 1 piece in end goal
		if (board.numberPiecesInGoal(machine, false) == 1) {
			score += 1.5;
		}

		// if machine has 2 or more pieces in end goal
		if (board.numberPiecesInGoal(machine, false) > 1) {
			score -= board.numberPiecesInGoal(machine, false) * 3;
		}
		
		// step 2. prefer making a move that would increase connections
		// increase the score for every connection machine has
		score += board.totalNumberConnections(machine);
		
		// step 3. prefer making a move that would block the opponent
		// decrease the score for every connection opponent has
		score -= board.totalNumberConnections(opponent);

		return score;
	}
}