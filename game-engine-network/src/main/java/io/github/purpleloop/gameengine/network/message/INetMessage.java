package io.github.purpleloop.gameengine.network.message;

/** Message sent over network. */
public interface INetMessage {

	/**
	 * @return buffer containing the message bytes
	 */
	byte[] getBytes();

}
