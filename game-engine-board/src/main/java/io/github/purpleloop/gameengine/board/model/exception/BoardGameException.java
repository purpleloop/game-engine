package io.github.purpleloop.gameengine.board.model.exception;

/** Exception of the board game engine. */
public class BoardGameException extends Exception {

	/** Serialization tag. */	
	private static final long serialVersionUID = -8575135560682960270L;

	/** Constructs an exception of the board game engine with a message. 
	 * @param message massage describing the problem
	 */
	public BoardGameException(String message) {
		super(message);
	}

	/** Constructs an exception of the board game engine with a message and a cause.
	 * @param message massage describing the problem
	 * @param cause cause of the exception
	 */
	public BoardGameException(String message, Throwable cause) {
		super(message, cause);
	}

}
