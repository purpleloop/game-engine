package io.github.purpleloop.gameengine.board.gui.net;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.gameengine.board.model.net.AbstractNetworkBoardGame;
import io.github.purpleloop.gameengine.core.config.LocalDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;
import io.github.purpleloop.gameengine.network.INetworkEngine;
import io.github.purpleloop.gameengine.network.connection.NetConnection;
import io.github.purpleloop.gameengine.network.connection.NetConnectionObserver;
import io.github.purpleloop.gameengine.network.connection.NetConnectionState;
import io.github.purpleloop.gameengine.network.connection.StarterObserver;
import io.github.purpleloop.gameengine.network.exception.NetException;
import io.github.purpleloop.gameengine.network.message.INetMessage;

/** The connection frame. */
public class ConnectionFrame extends JFrame implements StarterObserver, NetConnectionObserver {

	/** Class logger. */
	private static Log log = LogFactory.getLog(ConnectionFrame.class);

	/** Serial tag. */
	private static final long serialVersionUID = 4347683075279396487L;

	/** IP address of the local host. */
	private static final String LOCAL_HOST_IP_ADDRESS = "127.0.0.1";

	/** Default TCP port. */
	private static final String DEFAULT_TCP_PORT = "1234";

	/** New line. */
	private static final String NEW_LINE = "\r\n";

	/** Height of the communication zone. */
	private static final int COM_ZONE_WIDTH = 20;

	/** Width of the communication zone. */
	private static final int COM_ZONE_HEIGHT = 20;

	/** The network engine. */
	private INetworkEngine networkEngine;

	/** Button used to start listening as a server. */
	private JButton listen;

	/** Button used to call a server as a client. */
	private JButton call;

	/** Button used to disconnect. */
	private JButton btDisconnect;

	/** Remote server IP to reach. */
	private JTextField tfAddress;

	/** TPC communication port for the remote server to reach. */
	private JTextField tfPort;

	/** Button used to send a message to the remote peer. */
	private JButton btSend;

	/** Communication area. */
	private JTextArea communicationArea;

	/** Label indicating the connection state. */
	private JLabel labState;

	/** The game board frame. */
	private BoardFrame boardFrame;

	/** The current game it it exists. */
	private AbstractNetworkBoardGame game;

	/** The game engine. */
	private NetworkBoardGameEngine gameEngine;

	/** Action to create a server. */
	private Action createServerAction = new AbstractAction("Create server") {

		/** Serial tag. */
		private static final long serialVersionUID = -6011268905659383649L;

		@Override
		public void actionPerformed(ActionEvent e) {
			createServer();
		}
	};

	/** Action to call a server. */
	private Action callServerAction = new AbstractAction("Call a remote server") {

		/** Serial tag. */
		private static final long serialVersionUID = 5495274318360273676L;

		@Override
		public void actionPerformed(ActionEvent e) {
			callRemoteServer();
		}
	};

	/** Action to send a message. */
	private Action sendMessageAction = new AbstractAction("Send a message") {

		/** Serial tag. */
		private static final long serialVersionUID = -5612616883914203987L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage();
		}
	};

	/** Action to disconnect. */
	private Action disconnectAction = new AbstractAction("Disconnect") {

		/** Serial tag. */
		private static final long serialVersionUID = 6446447128099003690L;

		@Override
		public void actionPerformed(ActionEvent e) {
			disconnect(true);
		}
	};

	/**
	 * Constructor of the connection frame.
	 * 
	 * @param gameEngine the game engine
	 */
	public ConnectionFrame(NetworkBoardGameEngine gameEngine) {

		super("Connection");

		this.gameEngine = gameEngine;
		this.networkEngine = gameEngine.getNetworkEngine();

		game = null;
		boardFrame = null;

		JPanel mainPanel = new JPanel();
		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		communicationArea = new JTextArea(COM_ZONE_WIDTH, COM_ZONE_HEIGHT);
		mainPanel.add(communicationArea, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5, 1));

		labState = new JLabel("Disconnected");
		controlPanel.add(labState);

		listen = SwingUtils.createButton(createServerAction, controlPanel);

		JPanel serverControlPanel = new JPanel();
		serverControlPanel.setLayout(new GridLayout(2, 2));
		tfAddress = new JTextField(LOCAL_HOST_IP_ADDRESS);
		serverControlPanel.add(tfAddress);

		tfPort = new JTextField(DEFAULT_TCP_PORT);
		serverControlPanel.add(tfPort);

		call = SwingUtils.createButton(callServerAction, serverControlPanel);
		controlPanel.add(serverControlPanel);

		btSend = SwingUtils.createButton(sendMessageAction, controlPanel);
		btDisconnect = SwingUtils.createButton(disconnectAction, controlPanel);

		mainPanel.add(controlPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
	}

	/** @return The communication port. */
	private Integer getPort() {

		String portStr = tfPort.getText();

		try {
			return Integer.parseInt(portStr);

		} catch (NumberFormatException e) {
			return null;
		}

	}

	/** Create a server. */
	protected void createServer() {
		if (!networkEngine.hasConnection()) {

			Integer port = getPort();

			if (port == null) {
				communicationArea.append("Incorrect TCP port." + NEW_LINE);
				return;
			}

			networkEngine.createServerConnection(this, this, port);

			listen.setEnabled(false);
			call.setEnabled(false);
			btDisconnect.setEnabled(true);

			communicationArea.append("Server started and waiting for client connections." + NEW_LINE);
		}
	}

	/** Call a remote server. */
	protected void callRemoteServer() {
		if (!networkEngine.hasConnection()) {

			Integer port = getPort();

			if (port == null) {
				communicationArea.append("TCP port is invalid." + NEW_LINE);
				return;
			}

			try {

				networkEngine.createClientConnection(this, tfAddress.getText(), port);
				btSend.setEnabled(true);
				listen.setEnabled(false);
				call.setEnabled(false);
				btDisconnect.setEnabled(true);
			} catch (NetException e) {
				communicationArea.append("A network problem occured : " + e.getLocalizedMessage() + NEW_LINE);
			}
		}

	}

	/** Send a message. */
	protected void sendMessage() {
		if (networkEngine.isConnectionEstablished()) {
			networkEngine.sendDummyMessage();
		}
	}

	/**
	 * Disconnect from a peer, if currently connected.
	 * 
	 * @param notifyPeer true if one must notify the peer or not
	 */
	protected void disconnect(boolean notifyPeer) {
		btDisconnect.setEnabled(false);

		networkEngine.disconnect(notifyPeer);

		if ((boardFrame != null) && (boardFrame.isVisible())) {
			boardFrame.setVisible(false);
		}

	}

	@Override
	public void startResult(boolean success, NetException e) {

		if (success) {
			btSend.setEnabled(true);

		} else {
			communicationArea.append("Network error :" + e.getLocalizedMessage() + NEW_LINE);
		}
	}

	@Override
	public void notifyAvailableMessage(NetConnection thd) {
		Optional<INetMessage> optionalMessage = thd.getNextMessage();
		if (optionalMessage.isPresent()) {

			INetMessage incomingMessage = optionalMessage.get();
			if (gameEngine.getNetMessageFactory().isGoodByeMessage(incomingMessage)) {

				if (boardFrame != null) {
					if (game != null) {
						game.removeGameObserver(boardFrame);
					}
					boardFrame.setVisible(false);
					boardFrame = null;
				}
				if (game != null) {
					game = null;
				}

				communicationArea.append("Remote peer has initiated a disconnection" + NEW_LINE);
				disconnect(false);

			} else if (gameEngine.getNetMessageFactory().isDummyMessage(incomingMessage)) {

				log.info("Receiving dummy message.");

			} else {
				game.receiveNetworkMessage(incomingMessage);
				communicationArea.append(incomingMessage.toString() + NEW_LINE);
			}

		}

	}

	@Override
	public void stateChanged(NetConnection netConnection, NetConnectionState state) {
		labState.setText(state.getLabel());
		if (state == NetConnectionState.LISTENING) {

			try {
				game = gameEngine.createGame();
				gameEngine.getBoardFrame().addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent event) {
						Window windowEventSource = event.getWindow();
						log.debug("Closing window " + windowEventSource.getName());
						if (windowEventSource == boardFrame) {
							disconnect(true);
						}
					}

				});

			} catch (EngineException e) {
				log.error("Error on the game engine ", e);
				communicationArea.append("Error on the game engine " + e.getMessage());
			}

		}
	}

	@Override
	public void endOfListening(NetConnection thd) {
		listen.setEnabled(true);
		call.setEnabled(true);
		btDisconnect.setEnabled(false);
		btSend.setEnabled(false);

		if (networkEngine.hasConnection()) {
			networkEngine.terminateConnection();
			communicationArea.append("Connection terminated" + NEW_LINE);
		}

	}

	@Override
	public void connectionError(NetConnection cnx, String err) {
		communicationArea.append("Network error :" + err + NEW_LINE);

		networkEngine.clearConnectionAfterError();
		if ((boardFrame != null) && (boardFrame.isVisible())) {
			boardFrame.setVisible(false);
		}

		listen.setEnabled(true);
		call.setEnabled(true);
		btDisconnect.setEnabled(false);
		btSend.setEnabled(false);
	}

	/**
	 * Main entry point of the program.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		try {
			NetworkBoardGameEngine gameEngine = new NetworkBoardGameEngine(LocalDataFileProvider.getInstance(),
					"engine.xml");
			ConnectionFrame fra = new ConnectionFrame(gameEngine);
			fra.setVisible(true);
		} catch (EngineException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
