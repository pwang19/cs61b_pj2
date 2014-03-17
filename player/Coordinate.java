package player;

public class Coordinate {

	/**
	* The Coordinate class holds information about each space
	* on the Grid.
	**/
	
	protected static final int EMPTY = 0;
	protected static final int COMPUTER = 1;
	protected static final int OPPONENT = 2;
	
	protected int piece;
	protected boolean valid;

	public Coordinate() {
		piece = 0;
		valid = true;
	}


	protected void addPiece(int piece) {
		this.piece = piece;
		valid = false;
	}

	protected void removePiece() {
		piece = 0;
		valid = true;
	}

	protected void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

}