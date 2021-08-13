package io.github.purpleloop.gameengine.network.connection;

import io.github.purpleloop.gameengine.network.exception.NetException;

/** Thread handling server connection start. */
public class TCPServerConnectionStarter extends Thread {

	/** Server connection to start. */
	private TCPServerConnection server;

	/** Connection starter observer. */
	private StarterObserver starterObserver;

	/**
	 * Constructor of the connection starter thread.
	 * 
	 * @param starterObserver  starter observer
	 * @param serverConnection The connection server to start
	 */
	public TCPServerConnectionStarter(StarterObserver starterObserver, TCPServerConnection serverConnection) {
		this.server = serverConnection;
		this.starterObserver = starterObserver;
	}

	@Override
	public void run() {
		try {
			server.openConnection();
			server.start();
		} catch (NetException e) {
			starterObserver.startResult(false, e);
		}
		starterObserver.startResult(true, null);
	}

}
