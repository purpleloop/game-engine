package io.github.purpleloop.gameengine.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessage;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;
import io.github.purpleloop.gameengine.network.message.MessageBox;

/** A network connection. */
public abstract class NetConnection extends Thread {

	/** Class logger. */
	private static Log log = LogFactory.getLog(NetConnection.class);

	/** Line feed character. */
	private static final int CHARACTER_LINE_FEED = 10;

	/** Carriage return character. */
	private static final int CHARACTER_CARRIAGE_RETURN = 13;

	/** Connection name. */
	private String connectionName;

	/** Connection state. */
	private NetConnectionState state;

	/** Connection observer. */
	private NetConnectionObserver connectionObserver;

	/** Output stream. */
	private OutputStream outputStream;

	/** Input stream. */
	private InputStream inputStream;

	/** The message box. */
	private MessageBox messageBox;

	/** Message factory. */
	private INetMessageFactory netMessageFactory;

	/**
	 * Constructor of the network connection.
	 * 
	 * @param connectionName     Connection name
	 * @param connectionObserver Connection observer
	 * @param messageFactory     Message factory
	 */
	protected NetConnection(String connectionName, NetConnectionObserver connectionObserver,
			INetMessageFactory messageFactory) {
		super("GameEngine-" + connectionName + "ConnectionThread");

		this.connectionName = connectionName;
		this.state = NetConnectionState.DISCONNECTED;
		this.connectionObserver = connectionObserver;
		this.messageBox = new MessageBox();
		this.netMessageFactory = messageFactory;
	}

	/**
	 * Establishes the connection.
	 * 
	 * @throws NetException In case of problem during connection.
	 */
	public abstract void openConnection() throws NetException;

	@Override
	public void run() {

		if (state == NetConnectionState.CONNECTED) {

			log.debug(connectionName + "::run - Listening to incoming messages");

			StringBuilder rawMessage = new StringBuilder();
			int readChar;

			changeConnectionState(NetConnectionState.LISTENING);

			try {

				readChar = inputStream.read();

				while ((state == NetConnectionState.LISTENING) && (readChar != -1)) {

					if (readChar == CHARACTER_CARRIAGE_RETURN) {
						log.debug(connectionName + "::run - Raw incoming message : " + rawMessage);
						String rawMessageString = rawMessage.toString();
						messageBox.receive(netMessageFactory.decodeMessage(rawMessageString));
						connectionObserver.notifyAvailableMessage(this);
						rawMessage.delete(0, rawMessage.length());

					} else if (readChar != CHARACTER_LINE_FEED) {
						rawMessage.append((char) readChar);
					}

					readChar = inputStream.read();

				} // while -- listening loop

				log.debug(connectionName + "::run - Stop listening incoming message");
				connectionObserver.endOfListening(this);

			} catch (IOException e) {
				log.error(connectionName + "::run - Error while reading from the input stream", e);
				connectionObserver.connectionError(this, e.getLocalizedMessage());
				terminateConnection();
			}

		} else {
			log.error(connectionName + "::run - Error, connection is not established");
		}
	}

	/**
	 * Set input and output streams for communication.
	 * 
	 * @param inputStream  Input stream
	 * @param outputStream Output stream
	 */
	protected void setIOStreams(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	/**
	 * Sets the connection states and notifies observers.
	 * 
	 * @param newState The new state
	 */
	protected void changeConnectionState(NetConnectionState newState) {
		log.debug("Connection state change : " + state.getLabel() + " => " + newState.getLabel());
		this.state = newState;
		this.connectionObserver.stateChanged(this, state);
	}

	/**
	 * @return the connection state
	 */
	protected NetConnectionState getConnectionState() {
		return state;
	}

	/**
	 * @return true if the connection is established, false otherwise
	 */
	public synchronized boolean isConnected() {
		return (state == NetConnectionState.CONNECTED) || (state == NetConnectionState.LISTENING);
	}

	/**
	 * @return true if the connection is in listening state, false otherwise
	 */
	public synchronized boolean isListening() {
		return state == NetConnectionState.LISTENING;
	}

	/**
	 * @return the connection name
	 */
	public String getConnectionName() {
		return connectionName;
	}

	/** Requests to stop the listening. */
	public synchronized void requestStopListening() {
		if (state == NetConnectionState.LISTENING) {
			log.debug(connectionName + "::stopListening - Stop listening requested");
			changeConnectionState(NetConnectionState.CONNECTED);
		}
	}

	/** Terminates the connection. */
	public void terminateConnection() {

		if (getConnectionState() != NetConnectionState.DISCONNECTED) {

			log.debug(connectionName + "::terminateConnection - End of connection requested");

			// Closes I/O streams

			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
					log.debug(connectionName + "::terminateConnection - Output stream closed");
				} catch (IOException e) {
					log.error(connectionName + "::terminateConnection - Unable to close the output stream", e);
				}
				outputStream = null;
			}

			if (inputStream != null) {
				try {
					inputStream.close();
					log.debug(connectionName + "::terminateConnection - Input stream closed.");
				} catch (IOException e) {
					log.error(connectionName + "::terminateConnection - Unable to close the input stream", e);
				}
				inputStream = null;
			}

			doCloseConnectionSupport();

			changeConnectionState(NetConnectionState.DISCONNECTED);
			log.debug(connectionName + "::terminateConnection - Disconnected.");
		}
	}

	/** End specific connection supports. */
	protected abstract void doCloseConnectionSupport();

	/**
	 * Sends a message on the communication channel.
	 * 
	 * @param message Message to send
	 * @return true if the message was successfully sent, false otherwise
	 */
	public synchronized boolean send(INetMessage message) {

		boolean messageSent = false;
		log.debug(connectionName + "::send - Sending message : " + message);

		if (isConnected()) {
			try {
				outputStream.write(message.getBytes());
				outputStream.write(CHARACTER_CARRIAGE_RETURN);
				messageSent = true;
				log.debug(connectionName + "::send - Message sent");
			} catch (IOException e) {
				log.error(connectionName + "::send - Communication error while sending message", e);
			}
		} else {
			log.error(connectionName + "::send - Unable to send am essage, connection is not established");
		}
		return messageSent;
	}

	/**
	 * @return optional next incoming message
	 */
	public Optional<INetMessage> getNextMessage() {
		return messageBox.getNextMessage();
	}

}
