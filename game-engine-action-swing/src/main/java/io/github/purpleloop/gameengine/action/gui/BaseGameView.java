package io.github.purpleloop.gameengine.action.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameView;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;

/** Abstract base implementation of the game view. */
public abstract class BaseGameView implements IGameView {

	/** Class logger. */
	public static final Log LOG = LogFactory.getLog(BaseGameView.class);

	/** The sprite engine used to provide sprites. */
	private SpriteEngine spriteEngine;

	/** Should we display debug information ? */
	private boolean debugInfo = false;

	/** The configuration of the game. */
	private GameConfig config;

	/** The main panel used to render the game view. */
	private GamePanel ownerPanel;

	/** The default font used to display text. */
	private Font normalFont;

	/** Information of the current game session. */
	private ISession currentSession;

	/**
	 * Creates a view for the game.
	 * 
	 * @param owner the GamePanel associated to this view
	 * @param conf  the game configuration
	 */
	public BaseGameView(GamePanel owner, GameConfig conf) {
		this.ownerPanel = owner;
		this.config = conf;
		this.spriteEngine = new SpriteEngineImpl();
	}

	/**
	 * Loads the game sprites.
	 * 
	 * @param conf the game configuration
	 * @param dfp  the data file provider
	 */
	protected final void loadSpritesSource(GameConfig conf, IDataFileProvider dfp) {
		spriteEngine.loadSpritesSource(conf, dfp);
	}

	/** {@inheritDoc} */
	public final boolean isDebugInfo() {
		return debugInfo;
	}

	/** {@inheritDoc} */
	public final void setDebugInfo(boolean active) {
		debugInfo = active;
	}

	/** {@inheritDoc} */
	public final void switchDebugInfo() {
		this.debugInfo = !this.debugInfo;

		LOG.debug("DebugInfo is : " + this.debugInfo);
	}

	/**
	 * Displays a list of the available commands on the view.
	 * 
	 * @param g graphic context where to display the list
	 */
	public void listControls(Graphics g) {

		// Static key mapping controls
		g.drawString("<ENTER> to start.", 50, 50);
		g.drawString("<ESCAPE> to quit", 50, 70);
		g.drawString("<I> to show debug info", 50, 90);
		g.drawString("<P> for pause/unpause", 50, 110);
		g.drawString("<S> for sound on/off", 50, 130);

		// Dynamic key mapping controls
		int x = 50;
		int y = 150;
		for (Map.Entry<String, String> keyMapEntry : config.getKeyMap().entrySet()) {

			g.drawString("<" + keyMapEntry.getKey().toUpperCase() + "> for " + keyMapEntry.getValue(), x, y);
			y += 20;
		}

	}

	@Override
	public final void paint(Graphics g) {

		if (normalFont == null) {
			normalFont = ownerPanel.getFont();
			registerFonts(normalFont);
		}

		paintView(g);
	}

	/**
	 * Registers extra fonts.
	 * 
	 * @param normalFont the normal font to derive from
	 */
	protected void registerFonts(Font normalFont) {
	}

	/** Register a sprite in the sprite engine.
	 * @param spriteDescriptor the sprite description
	 */
	protected void registerSprite(Sprite spriteDescriptor) {
		spriteEngine.registerSprite(spriteDescriptor);
	}

	/** Register a sprite in the sprite engine.
	 * 
	 * @param name         name of the sprite
	 * @param xOrigin      Horizontal location of the sprite in the tile set
	 * @param yOrigin      Vertical location of the sprite in the tile set
	 * @param spriteWidth  sprite width
	 * @param spriteHeight sprite height
	 */
	protected void registerSprite(String name, int xOrigin, int yOrigin, int spriteWidth, int spriteHeight) {
		spriteEngine.registerSprite(new Sprite(name, xOrigin, yOrigin, spriteWidth, spriteHeight));
	}

	/** Put a sprite on the graphic context.
	 * @param g the graphic context
	 * @param name the sprite name
	 * @param xl abscissa
	 * @param yl ordinate
	 */
	protected void putSprite(Graphics g, String name, int xl, int yl) {
		spriteEngine.putSprite(g, ownerPanel, name, xl, yl);
	}

	/**
	 * Paints the view on the graphic context.
	 * 
	 * @param g the graphic context
	 */
	protected abstract void paintView(Graphics g);

	/** Refresh the view. */
	protected void repaint() {
		
		// Propagates to the panel
		ownerPanel.repaint();
	}

	@Override
	public void setSession(ISession session) {
		currentSession = session;
		repaint();
	}

	/** @return the current game session */
	protected ISession getCurrentSession() {
		return currentSession;
	}

	/** @return The default of the view. */
	protected Font getNormalFont() {
		return normalFont;
	}
}
