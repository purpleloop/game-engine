package io.github.purpleloop.gameengine.action.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import io.github.purpleloop.gameengine.action.model.interfaces.IGameView;

/** An extension of the Swing JPanel class used to display a game view. */
public final class GamePanel extends JPanel {

	/** Serial tag. */
	private static final long serialVersionUID = 3469377462597987507L;

	/** The game view. */
	private IGameView gameView;

	/** The thread used to refresh the view. */
	private RefreshThread gamePanelRefreshThread;

	/**
	 * Associates a view of the game to this panel.
	 * 
	 * @param view The game view to display
	 */
	public void setView(IGameView view) {
		this.gameView = view;
		setPreferredSize(view.getPreferredSize());

		if (gamePanelRefreshThread == null) {
			startViewRefresh();
		}
	}

	/** Starts the automatic refresh of the view. */
	private void startViewRefresh() {
		gamePanelRefreshThread = new RefreshThread(this);
		gamePanelRefreshThread.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (gameView != null) {
			gameView.paint(g);
		}
	}

	/** Stops the automating refreshing of the panel. */
	public void stopRefresh() {
		if (gamePanelRefreshThread != null) {
			gamePanelRefreshThread.terminate();
		}
	}

}
