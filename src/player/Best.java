package player;

/**
 * The Best class holds information about the best move a player can play. The
 * fields are directly manipulated by the MachinePlayer class.
 **/
public class Best {

	protected Move move; // The proposed move
	protected int score; // The assigned score to this move

	/**
	 * Creates a new Best object that has a null move and has a score of 0.
	 */
	public Best() {
		move = null;
		score = 0;
	}

}
