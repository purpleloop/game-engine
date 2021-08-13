package io.github.purpleloop.gameengine.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** This class implements a TCP client connection. */
public class TCPClientConnection extends TCPConnection {

	/** Class logger. */
	private static Log log = LogFactory.getLog(TCPClientConnection.class);

	/** The TCP communication socket used by the client. */
	private Socket clientSocket;

	/** IP address on which the client is connected. */
	private String serverIpAddress;

	/**
	 * Creates a client TCP connection to a server.
	 * 
	 * @param netObserver      The connection observer
	 * @param messageFactory   Message factory
	 * @param iserverIpAddress IP address of the server
	 * @param tcpServerPort    TCP port of the server
	 */
	public TCPClientConnection(NetConnectionObserver netObserver, INetMessageFactory messageFactory,
			String iserverIpAddress, int tcpServerPort) {
		super("Client", netObserver, messageFactory, tcpServerPort);
		this.serverIpAddress = iserverIpAddress;
	}

	@Override
	public void openConnection() throws NetException {

		String connectionName = getConnectionName();

		OutputStream outputStream = null;
		InputStream inputStream = null;

		if (getConnectionState() != NetConnectionState.DISCONNECTED) {
			terminateConnection();
		}

		log.debug(connectionName + "::connection - Creating client socket to " + serverIpAddress + ":" + getServerPort()
				+ ".");

		try {
			clientSocket = new Socket(serverIpAddress, getServerPort());
			log.debug(connectionName + "::connection - Socket is connected");

		} catch (UnknownHostException e) {
			log.error(connectionName + "::connection - Server host cannot be found", e);
			throw new NetException("Unable to find the server host");
		} catch (IOException e) {
			log.error(connectionName + "::connection - Communication error.", e);
			throw new NetException("Unable to communicate with the server");
		}

		// Here, the socket is created, we get the communication streams.
		// (E/S)
		try {
			log.debug(connectionName + "::connection - Opening communication streams...");
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();

		} catch (IOException e) {

			log.error(connectionName + "::connection - " + "Error while establishing communication streams", e);

			// Closes streams that have been established
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e1) {
					log.error(connectionName + "::connection - Failed to close the stream", e1);
				}
			}

			try {
				clientSocket.close();
			} catch (IOException e1) {
				log.error(connectionName + "::connection - Failed to close the TCP socket", e1);
			}

			changeConnectionState(NetConnectionState.DISCONNECTED);
			throw new NetException("Error while establishing a connection to the server");
		}

		setIOStreams(inputStream, outputStream);
		log.debug(connectionName + "::connection - Connection established.");
		changeConnectionState(NetConnectionState.CONNECTED);
	}

	@Override
	protected void doCloseConnectionSupport() {

		String connectionName = getConnectionName();
		try {
			log.debug(connectionName + "::terminateConnection - Closing the socket for connection " + connectionName + ".");
			clientSocket.close();
		} catch (IOException e) {
			log.error(connectionName + "::terminateConnection - " + "Error while closing the socket connection " + connectionName, e);
		}
		clientSocket = null;
	}

}
