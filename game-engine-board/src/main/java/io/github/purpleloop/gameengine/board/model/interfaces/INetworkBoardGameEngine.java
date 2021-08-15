package io.github.purpleloop.gameengine.board.model.interfaces;

import io.github.purpleloop.gameengine.core.interfaces.IRootGameEngine;
import io.github.purpleloop.gameengine.network.INetworkEngine;
import io.github.purpleloop.gameengine.network.message.INetMessage;
import io.github.purpleloop.gameengine.network.message.INetMessageFactory;

/** Represents a network board game engine. */
public interface INetworkBoardGameEngine extends IRootGameEngine {

	/** @return the message factory */
	INetMessageFactory getNetMessageFactory();

	/**
	 * Send a message over the network connection.
	 * 
	 * @param message the message
	 */
	void sendMessage(INetMessage message);

	/** @return the network engine. */
	INetworkEngine getNetworkEngine();

}
