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

	// influence might make it easier to keep track of validity
	// each piece casts 1 influence on its surrounding coordinates
	// each piece has 2 influence on its own coordinate
	// if a coordinate has 2 or more influence, it is invalid
	protected int influence; // if 0 or 1, this coordinate is valid
							 // if 2 or more, this coordinate is invalid

	public Coordinate() {
		piece = EMPTY;
		influence = 0;
	}


	protected void addPiece(int piece) {
		this.piece = piece;
		influence += 2;
	}

	protected void removePiece() {
		piece = EMPTY;
		influence = 0;
	}

	protected void increaseInfluence() {
		influence++;
	}

	protected void decreaseInfluence() {
		influence--;
	}

	/**
	* isValid() checks if this coordinate is valid
	* @return if influence >= 2, it means that either a piece is
	* 			on that coordinate, or that a piece placed next to it
	*			would break the rules.
	**/
	public boolean isValid() {
		return (influence < 2);
	}

	/**
	* toString() gives a String representation of the coordinate.
	* 
	* @return there are 2 characters:
	* 				(1) piece representation
	*					0 = empty
	*					1 = computer
	*					2 = enemy
	*				(2) valid/invalid
	*					o = valid
	*					x = invalid
	**/
	public String toString() {
		String str = "" + piece;
		if(isValid()) {
			str += "o";
		} else {
			str += "x";
		}
		return str;
	}

}