package io.github.purpleloop.gameengine.board.model;

import io.github.purpleloop.gameengine.board.model.interfaces.IBoardGameState;

/** The game states. */
public enum BoardGameState implements IBoardGameState {

	/** Board game is idle, not playing. */
	IDLE("idle"),

	/** Game is active. */
	INGAME("active"),

	/** Game is terminating. */
	TERMINATING("terminating"),

	/** Game is terminated. */
	TERMINATED("terminated");

	/** Game status name. */
	private String name;

	/**
	 * Constructor of the status.
	 * 
	 * @param name status name
	 */
	BoardGameState(String name) {
		this.name = name;
	}

	/** @return the status name */
	String getName() {
		return this.name;
	}

}
