package io.github.purpleloop.gameengine.network.message;

/** The message factory interface. */
public interface INetMessageFactory {

	/**
	 * Decodes a message from a string.
	 * 
	 * @param sourceString the source string
	 * @return Plain message
	 */
	INetMessage decodeMessage(String sourceString);

	/** @return a goodbye message */
	INetMessage getGoodbyeMessage();

	/** @return a dummy message */
	INetMessage getDummyMessage();

	/**
	 * @param testMessage the message
	 * @return true if this is a goodbye message, false otherwise
	 */
	boolean isGoodByeMessage(INetMessage testMessage);

	/**
	 * @param testMessage the message
	 * @return true if this is a dummy message, false otherwise
	 */
	boolean isDummyMessage(INetMessage testMessage);
}
