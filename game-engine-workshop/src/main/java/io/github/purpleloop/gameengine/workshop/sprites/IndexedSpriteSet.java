package io.github.purpleloop.gameengine.workshop.sprites;

import java.awt.Point;

import io.github.purpleloop.commons.swing.sprites.SpriteSet;

/** Interface for an indexed sprite set. */
public interface IndexedSpriteSet {

	/**
	 * Register the sprites.
	 * 
	 * @param spriteSet the sprite set
	 */
	void registerSprites(SpriteSet spriteSet);

	/**
	 * Get the index for the sprite at a given point.
	 * 
	 * @param point point to test
	 * @return the index of the sprite at the given point
	 */
	int getIndexFor(Point point);

	/**
	 * Get the index of the sprite in the sprite set.
	 * 
	 * @param animationIndex for the animation index
	 * @param progression    the progression of animation
	 * @return the index
	 */
	int getForTime(int animationIndex, double progression);

	/**
	 * @param indexSprite the sprite index
	 * @return abscissa of the first sprite
	 */
	int getX(int indexSprite);

	/**
	 * @param indexSprite the sprite index
	 * @return ordinate of the first sprite
	 */
	int getY(int indexSprite);

	/**
	 * @param indexSprite the sprite index
	 * @return width of the sprites
	 */
	int getWidth(int indexSprite);

	/**
	 * @param indexSprite the sprite index
	 * @return height of the sprites
	 */
	int getHeigth(int indexSprite);

}
