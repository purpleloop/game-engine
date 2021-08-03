package io.github.purpleloop.gameengine.action.gui;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;

/** The sprite engine interface. */
public interface SpriteEngine {

	/**
	 * Loads the game sprites.
	 * 
	 * @param gameConfig       Game configuration
	 * @param dataFileProvider provider of data files
	 */
	void loadSpritesSource(GameConfig gameConfig, IDataFileProvider dataFileProvider);

	/**
	 * Registers a sprite in the sprite engine.
	 * 
	 * @param name sprite name
	 * @param x    abscissa of the sprite
	 * @param y    ordinate of the sprite
	 * @param w    width of the sprite
	 * @param h    height of the sprite
	 */
	void registerSprite(String name, int x, int y, int w, int h);

	/**
	 * Puts a specific sprite on the given graphics, at given coordinates.
	 * 
	 * @param g        the graphic context where to put the sprite
	 * @param observer image observer
	 * @param name     name of the sprite to use
	 * @param xl       abscissa
	 * @param yl       ordinate
	 */
	void putSprite(Graphics g, ImageObserver observer, String name, int xl, int yl);

	/**
	 * Registers a sprite from it's descriptor.
	 * 
	 * @param desc the sprite descriptor
	 */
	void registerSprite(Sprite desc);

}
