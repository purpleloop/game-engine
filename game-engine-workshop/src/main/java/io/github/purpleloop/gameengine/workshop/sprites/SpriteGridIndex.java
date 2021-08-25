package io.github.purpleloop.gameengine.workshop.sprites;

import java.awt.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.commons.swing.sprites.SpriteSet;

/**
 * Models an index of sprites in an orthogonal 2D grid over an image. Dimensions
 * are given in pixels. Generally height and width are equal, but this is not
 * mandatory.
 */
public class SpriteGridIndex implements IndexedSpriteSet {

	private static final int OFFSET_FOR_INDEX = 8;

	/** Name of the XML element for the grid index. */
	static final String GRID_INDEX_ELEMENT = "gridIndex";

	/** Upper left corner of the grid = starting point to align the grid. */
	private Point startPoint;

	/** Width of sprites in the grid. */
	private int spriteWidth;

	/** Height of sprites in the grid. */
	private int spriteHeight;

	/** Number lines in the grid. */
	private int nbLines;

	/** Number of sprites per line in the grid. */
	private int spritesPerLine;

	/** Horizontal spacing between sprites. */
	private int paddingX;

	/** Vertical spacing between sprites. */
	private int paddingY;

	private int count = 3;

	public void setGrid(int spritesPerLines, int lines, Point point, int ugx, int ugy, int paddingX, int paddingY) {
		this.startPoint = point;
		this.spriteWidth = ugx;
		this.spriteHeight = ugy;
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		this.spritesPerLine = spritesPerLines;
		this.nbLines = lines;
	}

	/**
	 * @param spriteIndex the sprite index in the grid
	 * @return abscissa of the sprite in the image
	 */
	public int getX(int spriteIndex) {
		return (spriteWidth + paddingX) * (spriteIndex % spritesPerLine) + (int) startPoint.getX();
	}

	/**
	 * @param spriteIndex the sprite index in the grid
	 * @return ordinate of the sprite in the image
	 */
	public int getY(int spriteIndex) {
		return (spriteHeight + paddingY) * (spriteIndex / spritesPerLine) + (int) startPoint.getY();
	}

	@Override
	public void registerSprites(SpriteSet spriteSet) {
		for (int spriteNumber = 0; spriteNumber < getSpritesCount(); spriteNumber++) {
			spriteSet.addSprite(new Sprite("sprite" + spriteNumber, getX(spriteNumber), getY(spriteNumber), spriteWidth,
					spriteHeight));
		}
	}

	/**
	 * @param spritesPerLine the number of sprites per line
	 */
	public void setSpritesPerLine(int spritesPerLine) {
		this.spritesPerLine = spritesPerLine;
	}

	/** @return the number of sprites in the grid */
	public int getSpritesCount() {
		return this.spritesPerLine * this.nbLines;
	}

	@Override
	public int getIndexFor(Point p) {

		int xg = ((int) (p.getX() - startPoint.getX())) / (spriteWidth + paddingX);
		int yg = ((int) (p.getY() - startPoint.getY())) / (spriteHeight + paddingY);

		return xg + yg * spritesPerLine;
	}

	/**
	 * @param index the index
	 * @return sprite width
	 */
	public int getWidth(int index) {
		return spriteWidth;
	}

	/**
	 * Read sprite index from an XML element.
	 * 
	 * @param gridElement the XML element representing the grid
	 */
	public void readFromXmlElement(Element gridElement) {
		int sx =  Integer.parseInt(gridElement.getAttribute("sx"));
		int sy = Integer.parseInt(gridElement.getAttribute("sy"));
		this.startPoint = new Point(sx, sy);

		this.spritesPerLine = Integer.valueOf(gridElement.getAttribute("spritesPerLine"));
		this.nbLines = Integer.valueOf(gridElement.getAttribute("lines"));
		this.spriteWidth = Integer.valueOf(gridElement.getAttribute("ugx"));
		this.spriteHeight = Integer.valueOf(gridElement.getAttribute("ugy"));
		this.paddingX = Integer.valueOf(gridElement.getAttribute("paddingX"));
		this.paddingY = Integer.valueOf(gridElement.getAttribute("paddingY"));
	}

	/**
	 * Save the sprite index as an XML element.
	 * 
	 * @param doc    the XML owning document
	 * @param parent XML parent element
	 * @return the element representing the sprite index
	 */
	public Element saveToXml(Document doc, Element parent) {

		Element gridElement = doc.createElement(GRID_INDEX_ELEMENT);
		gridElement.setAttribute("sx", Integer.toString(this.startPoint.x));
		gridElement.setAttribute("sy", Integer.toString(this.startPoint.y));
		gridElement.setAttribute("spritesPerLine", Integer.toString(this.spritesPerLine));
		gridElement.setAttribute("lines", Integer.toString(this.nbLines));
		gridElement.setAttribute("ugx", Integer.toString(this.spriteWidth));
		gridElement.setAttribute("ugy", Integer.toString(this.spriteHeight));
		gridElement.setAttribute("paddingX", Integer.toString(this.paddingX));
		gridElement.setAttribute("paddingY", Integer.toString(this.paddingY));

		parent.appendChild(gridElement);
		return gridElement;

	}

	@Override
	public int getForTime(int animationIndex, double completionFraction) {
		final int indexInSequence = Math.min((int) Math.floor(completionFraction * this.count), this.count - 1);

		return animationIndex + OFFSET_FOR_INDEX * indexInSequence;
	}

	@Override
	public int getHeigth(int indexSprite) {
		return spriteHeight;
	}

	/**
	 * Translate the grid.
	 * 
	 * @param dx horizontal part of the movement
	 * @param dy vertical part of the movement
	 */
	public void translate(int dx, int dy) {
		startPoint.translate(dx, dy);
	}

}
