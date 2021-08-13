package io.github.purpleloop.gameengine.network.exception;

/** Exception class for networking problems in the networking engine. */
public class NetException extends Exception {

	/** Serial tag. */
	private static final long serialVersionUID = 1343885788406579422L;

	/**
	 * Constructor of the exception.
	 * 
	 * @param message Error message
	 */
	public NetException(String message) {
		super(message);
	}

}
