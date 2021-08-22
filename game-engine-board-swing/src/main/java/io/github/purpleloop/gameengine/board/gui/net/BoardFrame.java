package io.github.purpleloop.gameengine.board.gui.net;

import javax.swing.JFrame;

import io.github.purpleloop.gameengine.board.model.GameObserver;

/** Base of Swing frames to display boards. */
public abstract class BoardFrame extends JFrame implements GameObserver {

	/** Serial tag. */
	private static final long serialVersionUID = 600997337441190529L;

	/**
	 * Create the frame.
	 * 
	 * @param title frame title
	 */
	protected BoardFrame(String title) {
		super(title);
	}

}
