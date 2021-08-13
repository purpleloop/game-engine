package io.github.purpleloop.gameengine.network.connection;

import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** Network connection on TCP protocol. */
public abstract class TCPConnection extends NetConnection {

	/** TCP server port. */
	private int tcpServerPort;

	/**
	 * Creates a client TCP connection to a game server.
	 * 
	 * @param connectionName     Connection name
	 * @param connectionObserver Connection observer
	 * @param messageFactory     Message factory to use
	 * @param tcpServerPort      TCP server port
	 */
	protected TCPConnection(String connectionName, NetConnectionObserver connectionObserver,
			INetMessageFactory messageFactory, int tcpServerPort) {
		super(connectionName, connectionObserver, messageFactory);
		this.tcpServerPort = tcpServerPort;
	}

	/** @return TCP server port. */
	protected int getServerPort() {
		return tcpServerPort;
	}
}
