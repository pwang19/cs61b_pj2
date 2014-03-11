public class InvalidMoveException extends Exception {
	public InvalidMoveException() {
		super("Invalid Move!");
	}

	public InvalidMoveException(String err) {
		super("Invalid Move: " + err);
	}
}