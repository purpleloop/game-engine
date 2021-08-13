package io.github.purpleloop.gameengine.network;

import io.github.purpleloop.gameengine.network.connection.NetConnectionObserver;
import io.github.purpleloop.gameengine.network.connection.StarterObserver;
import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessage;

/** An interface for network engines. */
public interface INetworkEngine {

	/** @return true if there is a configured connection, false otherwise */
	boolean hasConnection();

	/** @return true if connection is established, false otherwise */
	boolean isConnectionEstablished();

	/**
	 * Creates a TCP server connection.
	 * 
	 * @param netConnectionObserver net connection observer
	 * @param starterObserver       starter observer
	 * @param tcpPort               TCP port
	 */
	void createServerConnection(NetConnectionObserver netConnectionObserver, StarterObserver starterObserver,
			Integer tcpPort);

	/**
	 * Create a TCP client connection.
	 * 
	 * @param observer  net connection observer
	 * @param ipAddress ip address
	 * @param tcpPort   tcp port
	 * @throws NetException in case of problem
	 */
	void createClientConnection(NetConnectionObserver observer, String ipAddress, int tcpPort) throws NetException;

	/** Send a dummy message. */
	void sendDummyMessage();

	/**
	 * Send a message.
	 * 
	 * @param message the message to send
	 */
	void sendMessage(INetMessage message);

	/** Clears connection after an error. */
	void clearConnectionAfterError();

	/** Terminate a connection (low level). */
	void terminateConnection();

	/**
	 * Disconnect from peer (and terminates connection).
	 * 
	 * @param notifyPeer true to send a goodbye message before disconnecting, false
	 *                   otherwise
	 */
	void disconnect(boolean notifyPeer);

}
