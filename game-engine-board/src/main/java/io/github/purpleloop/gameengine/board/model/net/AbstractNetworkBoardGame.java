package io.github.purpleloop.gameengine.board.model.net;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.board.model.GameObserver;
import io.github.purpleloop.gameengine.board.model.interfaces.IBoardGameState;
import io.github.purpleloop.gameengine.board.model.interfaces.INetworkBoardGameEngine;
import io.github.purpleloop.gameengine.network.message.INetMessage;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** Models a game played over a network. */
public abstract class AbstractNetworkBoardGame {

	/** Class logger. */
	public static final Log LOG = LogFactory.getLog(AbstractNetworkBoardGame.class);

	/** No user message. */
	private static final String NO_MESSAGE = "...";

	/** The local game state. */
	private IBoardGameState state;

	/** Game observers. */
	private List<GameObserver> observers;

	/** Messages to be displayed to the user. */
	private List<String> userMessages;

	/** The message factory. */
	private INetMessageFactory netWorkMessageFactory;

	/** The game engine. */
	private INetworkBoardGameEngine boardGameEngine;

	/**
	 * Creates a game with a network connection.
	 * 
	 * @param boardGameEngine the game engine
	 * @param messageFactory  the message factory
	 */
	protected AbstractNetworkBoardGame(INetworkBoardGameEngine boardGameEngine, INetMessageFactory messageFactory) {
		this.boardGameEngine = boardGameEngine;
		this.netWorkMessageFactory = messageFactory;
		this.observers = new LinkedList<>();
		this.userMessages = new ArrayList<>();
	}

	/**
	 * The game state.
	 * 
	 * @param newState The new state
	 */
	public void changeState(IBoardGameState newState) {
		state = newState;
		fireGameChanged();
	}

	/**
	 * Checks if the game is in a given state.
	 * 
	 * @param testedState the state to test
	 * @return true if the game is in the given state, false otherwise
	 */
	protected boolean inState(IBoardGameState testedState) {
		return state == testedState;
	}

	/**
	 * @return the game state
	 */
	protected IBoardGameState getState() {
		return state;
	}

	/**
	 * Add an observer to the game.
	 * 
	 * @param gameObserver The new observer to add
	 */
	public void addGameObserver(GameObserver gameObserver) {
		observers.add(gameObserver);
	}

	/**
	 * Remove a game observer.
	 * 
	 * @param gameObserver The new observer to remove
	 */
	public void removeGameObserver(GameObserver gameObserver) {
		observers.clear();
	}

	/** Notifies game observers that the game has changed. */
	protected void fireGameChanged() {
		for (GameObserver obs : observers) {
			obs.gameChanged();
		}
	}

	/**
	 * Add a message to the user.
	 * 
	 * @param userMessage Message for the user
	 */
	public void addUserMessage(String userMessage) {
		userMessages.add(userMessage);
		LOG.info("New user message " + userMessage);
	}

	/**
	 * @return last message to display to the user
	 */
	public String getLastUserMessage() {
		int size = userMessages.size();
		return (size < 1) ? NO_MESSAGE : userMessages.get(size - 1);
	}

	/**
	 * Handle an incoming message.
	 * 
	 * @param message incoming message
	 */
	public abstract void receiveNetworkMessage(INetMessage message);

	/**
	 * Send a message to the other player.
	 * 
	 * @param message message to send
	 */
	protected void sendNetworkMessage(INetMessage message) {
		boardGameEngine.sendMessage(message);
	}

	/** @return the network message factory */
	public INetMessageFactory getNetworkMessageFactory() {
		return netWorkMessageFactory;
	}

}
