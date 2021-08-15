package io.github.purpleloop.gameengine.board.model.interfaces;

import io.github.purpleloop.commons.swing.SwingUtils;

/** User interface for board games. */
public interface IBoardGameUI {

	/**
	 * Display a message to the user.
	 * 
	 * @param message the massage to display
	 */
	void displayMessage(String message);

	/**
	 * Display a message to the user with a message type.
	 * 
	 * @param messageType the message type
	 * @param message     the massage to display
	 */
	void displayMessage(SwingUtils.MessageType messageType, String message);

	/**
	 * Highlight the game board at given location to get the user attention.
	 * 
	 * @param x abscissa
	 * @param y ordinate
	 */
	void putFocusOnCell(int x, int y);

	/**
	 * Update the player status.
	 * 
	 * @param status the new status
	 */
	void setStatus(String status);

	/**
	 * Display information about the player score.
	 * 
	 * @param score scoring information
	 */
	void displayScore(String score);

	/**
	 * Displays information about current selection.
	 * 
	 * @param selectionInfo current selection informations
	 */
	void setSelectionInfo(String selectionInfo);

}
