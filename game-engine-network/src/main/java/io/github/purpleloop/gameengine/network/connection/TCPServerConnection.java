package io.github.purpleloop.gameengine.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** This class implements a TCP server connection. */
public class TCPServerConnection extends TCPConnection {

	/** Class logger. */
	private static Log log = LogFactory.getLog(TCPServerConnection.class);

	/** The server socket. */
	private ServerSocket serverSocket;

	/**
	 * Creates a TCP server.
	 * 
	 * @param netObserver    the connection observer
	 * @param messageFactory The message factory
	 * @param tcpServerPort  The TCP port on which the server will listen to
	 *                       incoming client connections
	 */
	public TCPServerConnection(NetConnectionObserver netObserver, INetMessageFactory messageFactory,
			int tcpServerPort) {
		super("Server", netObserver, messageFactory, tcpServerPort);
	}

	@Override
	public void openConnection() throws NetException {

		OutputStream outputStream = null;
		InputStream inputStream = null;
		String connectionName = getConnectionName();

		// Terminates a connection if is exists
		if (getConnectionState() != NetConnectionState.DISCONNECTED) {
			terminateConnection();
		}

		try {
			log.debug(
					connectionName + "::connection - Creation of a TCP server socket on port " + getServerPort() + ".");
			serverSocket = new ServerSocket(getServerPort());
			log.debug(connectionName + "::connection - TCP server socket created");
			changeConnectionState(NetConnectionState.CONNECTING);

		} catch (IOException e) {
			log.error(connectionName + "::connection - Unable to create the server socket", e);
			serverSocket = null;
			throw new NetException("Failed to start the server.");
		}

		try {
			log.debug(connectionName + "::connection - Waiting for remote clients");
			Socket client = serverSocket.accept();
			log.debug(connectionName + "::connection - Incoming connection from IP : "
					+ client.getInetAddress().toString());

			log.debug(connectionName + "::connection - Opening communication streams ...");
			outputStream = client.getOutputStream();
			inputStream = client.getInputStream();
			setIOStreams(inputStream, outputStream);
			log.debug(connectionName + "::connection - Connection successfully established.");

			changeConnectionState(NetConnectionState.CONNECTED);

		} catch (IOException e) {

			log.error(connectionName + "::connection - Error while establishing the connection with the remote client",
					e);

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
					log.error(connectionName + "::connection - Error while closing output stream", e1);
				}
			}

			// inputStream should not be null here if opening was successful

			try {
				serverSocket.close();
			} catch (IOException e1) {
				log.error(connectionName + "::connection - Unable to close the server socket", e1);
			}
			changeConnectionState(NetConnectionState.DISCONNECTED);

			throw new NetException(connectionName + "::connection - Unable to communicate with the client.");
		}
	}

	@Override
	protected void doCloseConnectionSupport() {
		String connectionName = getConnectionName();

		try {
			log.debug(connectionName + "::terminateConnection - Closing the server socket " + connectionName + ".");
			serverSocket.close();
		} catch (IOException e) {
			log.error(connectionName + "::terminateConnection - Unable to close the server socket",
					e);
		}
		serverSocket = null;
	}

}
