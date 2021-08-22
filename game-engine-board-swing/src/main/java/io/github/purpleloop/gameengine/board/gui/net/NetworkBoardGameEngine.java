package io.github.purpleloop.gameengine.board.gui.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.lang.ReflexivityTools;
import io.github.purpleloop.gameengine.board.model.interfaces.INetworkBoardGameEngine;
import io.github.purpleloop.gameengine.board.model.net.AbstractNetworkBoardGame;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;
import io.github.purpleloop.gameengine.network.INetworkEngine;
import io.github.purpleloop.gameengine.network.NetworkEngine;
import io.github.purpleloop.gameengine.network.message.INetMessage;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** The board game engine. */
public class NetworkBoardGameEngine implements INetworkBoardGameEngine {

	/** Logger of the class. */
	private static final Log LOG = LogFactory.getLog(NetworkBoardGameEngine.class);

	/** The game engine configuration. */
	private GameConfig gameConfig;

	/** Net message factory. */
	private INetMessageFactory netMessagegFactory;

	/** The current game. */
	private AbstractNetworkBoardGame currentGame;

	/** The board frame. */
	private BoardFrame boardFrame;

	/** The network engine. */
	private INetworkEngine networkEngine;

	/**
	 * Creates a new game engine.
	 * 
	 * @param dataFileProvider the dataFileProvider
	 * @param configFileName   the name of the configuration file
	 */
	public NetworkBoardGameEngine(IDataFileProvider dataFileProvider, String configFileName) throws EngineException {

		gameConfig = GameConfig.parse(dataFileProvider, configFileName);

		try {
			netMessagegFactory = ReflexivityTools.createInstance(gameConfig.getClassName(ClassRole.MESSAGE_FACTORY),
					new Class[0], new Object[0]);
		} catch (PurpleException | EngineException e) {
			LOG.error("Error while creating the message factory", e);
		}

		networkEngine = new NetworkEngine(netMessagegFactory);
	}

	/** @return the game engine configuration. */
	public GameConfig getConfig() {
		return gameConfig;
	}

	/**
	 * Create a new game.
	 * 
	 * @return the created game
	 */
	public AbstractNetworkBoardGame createGame() throws EngineException {

		instantiateGame();
		instantiateBoardFrame(currentGame);

		currentGame.addGameObserver(boardFrame);
		boardFrame.setVisible(true);

		return currentGame;
	}

	/**
	 * Instantiate the game.
	 * 
	 * @throws EngineException in case of errors
	 */
	private void instantiateGame() throws EngineException {
		try {
			String gameClass = gameConfig.getClassName(ClassRole.GAME);

			Class<?>[] paramClasses = new Class<?>[2];
			Object[] paramValues = new Object[2];
			paramClasses[0] = INetworkBoardGameEngine.class;
			paramValues[0] = this;
			paramClasses[1] = INetMessageFactory.class;
			paramValues[1] = netMessagegFactory;

			currentGame = ReflexivityTools.createInstance(gameClass, paramClasses, paramValues);
		} catch (PurpleException e) {
			throw new EngineException("Error while creating the game instance", e);
		}
	}

	/**
	 * Instantiate the board frame.
	 * 
	 * @param newGame the game to associate with the frame
	 * @throws EngineException in case of errors
	 */
	private void instantiateBoardFrame(AbstractNetworkBoardGame newGame) throws EngineException {
		try {
			String boardClass = gameConfig.getClassName(ClassRole.BOARD);

			Class<?>[] boardParamClasses = new Class<?>[1];
			Object[] boardParamValues = new Object[1];
			boardParamClasses[0] = AbstractNetworkBoardGame.class;
			boardParamValues[0] = newGame;

			boardFrame = (BoardFrame) ReflexivityTools.createInstance(boardClass, boardParamClasses, boardParamValues);
		} catch (PurpleException e) {
			throw new EngineException("Error while creating the game board frame instance", e);
		}
	}

	/** @return the current game is any */
	public AbstractNetworkBoardGame getCurrentGame() {
		return currentGame;
	}

	/** @return the board frame */
	public BoardFrame getBoardFrame() {
		return boardFrame;
	}

	@Override
	public INetMessageFactory getNetMessageFactory() {
		return netMessagegFactory;
	}

	@Override
	public INetworkEngine getNetworkEngine() {
		return networkEngine;
	}

	@Override
	public void sendMessage(INetMessage message) {
		networkEngine.sendMessage(message);
	}

}
