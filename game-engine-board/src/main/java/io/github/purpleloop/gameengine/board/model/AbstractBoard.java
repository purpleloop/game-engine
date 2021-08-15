package io.github.purpleloop.gameengine.board.model;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import io.github.purpleloop.gameengine.board.model.interfaces.BoardObserver;

/** Abstract class that represents a game board. */
public abstract class AbstractBoard {

	/** The set of board observers. */
	private Set<BoardObserver> boardObservers;

	/** Constructor of the game board. */
	protected AbstractBoard() {
		super();
		boardObservers = new HashSet<>();
	}

	/** Notifies observers all that the board has changed. */
	protected void fireBoardChanged() {
		for (BoardObserver boardObserver : boardObservers) {
			boardObserver.boardChanged();
		}
	}

	/** Notifies observers all that the board has been initialized. */
	protected void fireBoardInitialized() {
		for (BoardObserver boardObserver : boardObservers) {
			boardObserver.boardInitialized();
		}
	}

	/**
	 * Adds an observer for this board.
	 * 
	 * @param boardObserver The board observer to add
	 */
	public void addObserver(BoardObserver boardObserver) {
		if (!boardObservers.contains(boardObserver)) {
			boardObservers.add(boardObserver);
		}
	}

	/**
	 * Tests if the board corresponds to a termination state.
	 * 
	 * @return true if the board corresponds to a final state, false otherwise
	 */
	public abstract boolean isTerminated();

	/**
	 * Gives information about who won the game.
	 * 
	 * @return describes the winner if the game is terminated, null otherwise
	 */
	public abstract String getWinningStatus();

	/**
	 * Initializes the game board. This method must be called at the beginning of a
	 * new game.
	 * 
	 * @param game the new game for which the initialization is done.
	 */
	public abstract void initialize(AbstractBoardGame game);

	/**
	 * @return the current score
	 */
	public abstract String getScore();

	/**
	 * Loads the game board state from a file.
	 * 
	 * @param file file from where to load the board state
	 */
	public void load(File file) {
	}

	/**
	 * Saves the board state to a file.
	 * 
	 * @param file the file where to save the board state
	 */
	public void save(File file) {
	}

}
