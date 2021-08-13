package io.github.purpleloop.gameengine.network;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.network.connection.NetConnection;
import io.github.purpleloop.gameengine.network.connection.NetConnectionObserver;
import io.github.purpleloop.gameengine.network.connection.StarterObserver;
import io.github.purpleloop.gameengine.network.connection.TCPClientConnection;
import io.github.purpleloop.gameengine.network.connection.TCPServerConnection;
import io.github.purpleloop.gameengine.network.connection.TCPServerConnectionStarter;
import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessage;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** The network engine for Peer to Peer Client-Server over TCP. */
public class NetworkEngine implements INetworkEngine {

	/** Logger of the class. */
	private static final Log LOG = LogFactory.getLog(NetworkEngine.class);

	/** The network connection. */
	private NetConnection netConnection;

	/** The net message factory. */
	private INetMessageFactory netMessageFactory;
	
	/**
	 * Create a network engine.
	 * 
	 * @param netMessagegFactory the message factory
	 */
	public NetworkEngine(INetMessageFactory netMessagegFactory) {
		this.netMessageFactory = netMessagegFactory;
	}

	@Override
	public void createServerConnection(NetConnectionObserver netConnectionObserver, StarterObserver starterObserver,
			Integer tcpPort) {
	
		LOG.info("Creating a server connection on port : " + tcpPort);
		
		TCPServerConnection server = new TCPServerConnection(netConnectionObserver, netMessageFactory, tcpPort);
		netConnection = server;
		
		// Asynchronous start (the starter observer is notified when server is listening).
		TCPServerConnectionStarter starter = new TCPServerConnectionStarter(starterObserver, server);
		starter.start();
	}

	@Override
	public void createClientConnection(NetConnectionObserver observer, String ipAddress, int tcpPort)
			throws NetException {

		LOG.info("Creating a client connection to " + ipAddress + ":" + tcpPort);

		netConnection = new TCPClientConnection(observer, netMessageFactory, ipAddress,
				tcpPort);
		netConnection.openConnection();
		netConnection.start();
	}

	@Override
	public boolean hasConnection() {
		return netConnection != null;
	}

	@Override
	public boolean isConnectionEstablished() {
		return hasConnection() && netConnection.isConnected();
	}

	@Override
	public void sendDummyMessage() {
		sendMessage(netMessageFactory.getDummyMessage());
	}

	@Override
	public void sendMessage(INetMessage message) {
		netConnection.send(message);		
	}

	@Override
	public void clearConnectionAfterError() {
		netConnection = null;
	}

	@Override
	public void terminateConnection() {
		netConnection.terminateConnection();
		netConnection = null;
	}

	@Override
	public void disconnect(boolean notifyPeer) {
	
		if (hasConnection()) {
	
			if (isConnectionEstablished()) {
				if (notifyPeer) {		
					netConnection.send(netMessageFactory.getGoodbyeMessage());					
				}
				netConnection.requestStopListening();
			}
			terminateConnection();		
		}
		
	}

}
