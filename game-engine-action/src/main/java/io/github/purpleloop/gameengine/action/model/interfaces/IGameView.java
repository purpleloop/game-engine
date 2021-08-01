package io.github.purpleloop.gameengine.action.model.interfaces;

import java.awt.Dimension;
import java.awt.Graphics;

/** A view of the game in the player interface.
 * 
 * FIXME there are dependencies with Swing/AWT to resolve.
 * Possibly by adding a ISwingGameView ...
 * 
 */
public interface IGameView {

	/** Sets the game session for reference from the view.
	 * @param session the session to link to the view
	 */
	void setSession(ISession session);

	/** @return is debug information activated on this view ? */
	boolean isDebugInfo();

	/** Enables or disable the debug information display.
	 * @param active true if debug information should be displayed
	 *  */
	void setDebugInfo(boolean active);

	/** Renders the view on a AWT Graphic.
	 * @param g graphical device to use
	 */
	void paint(Graphics g);

	/** 
	 * @return preferred size of the view
	 */
	Dimension getPreferredSize();

	/** Switches the debug info for this view. */
	void switchDebugInfo();

}
