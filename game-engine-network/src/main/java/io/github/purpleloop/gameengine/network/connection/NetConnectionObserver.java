package io.github.purpleloop.gameengine.network.connection;

/** Network connection observer. */
public interface NetConnectionObserver {

	/**
	 * Handles a state change on a connection.
	 * 
	 * @param connection Connection that changed of state
	 * @param state      the new state of the connection
	 */
	void stateChanged(NetConnection connection, NetConnectionState state);

	/**
	 * Handles a connection error.
	 * 
	 * @param connection The connection on which the error occurred
	 * @param message    Error message
	 */
	void connectionError(NetConnection connection, String message);

	/**
	 * Handles the arrival of a new message on the connection.
	 * 
	 * @param connection Connection on which the message has just arrived
	 */
	void notifyAvailableMessage(NetConnection connection);

	/**
	 * Handles a end of listening event.
	 * 
	 * @param connection Connection that just ended
	 */
	void endOfListening(NetConnection connection);
}
