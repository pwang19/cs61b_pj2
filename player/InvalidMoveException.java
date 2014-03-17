package player;

public class InvalidMoveException extends Exception {
	public InvalidMoveException() {
		super("Invalid Move!");
	}

	public InvalidMoveException(int x, int y) {
		super("Tried to place piece on " +
		"coordinates: (" + x + ", " + y + ")");
	}
}