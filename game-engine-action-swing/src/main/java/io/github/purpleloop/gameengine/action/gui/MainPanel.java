package io.github.purpleloop.gameengine.action.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Main panel for the game engine. */
public class MainPanel extends JPanel implements GameEngineUI {

	/** Serial tag. */
	private static final long serialVersionUID = -4161776733138285824L;

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(MainPanel.class);

	/** The game panel that displays the game view. */
	private GamePanel gamePanel;

	/** The game engine. */
	private ActionGameEngine gameEngine;

	/**
	 * Creates the main panel.
	 * 
	 * @param df           data file provider
	 * @param confFileName configuration file name
	 */
	public MainPanel(IDataFileProvider df, String confFileName) {
		super();

		LOG.debug("Creation of the main panel");
		setLayout(new BorderLayout());

		gamePanel = new GamePanel();
		try {
			gameEngine = new ActionGameEngine(df, confFileName, this);
		} catch (EngineException e) {
			LOG.error("Failed to create the action game engine.", e);
			System.exit(1);
		}

		// Associates the game view with the game panel
		gamePanel.setView(gameEngine.getView());

		add(gamePanel, BorderLayout.CENTER);

		gameEngine.startEngine();

	}

	@Override
	public GamePanel getGamePanel() {
		return gamePanel;
	}

	/** @return the game engine running in this panel */
	public IGameEngine getGameEngine() {
		return gameEngine;
	}

	/** Stops the refresh of the panel. */
	public void stopRefresh() {
		gamePanel.stopRefresh();
	}

}
