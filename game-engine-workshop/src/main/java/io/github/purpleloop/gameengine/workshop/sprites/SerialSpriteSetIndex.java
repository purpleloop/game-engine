package io.github.purpleloop.gameengine.workshop.sprites;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import io.github.purpleloop.commons.swing.sprites.Sprite;
import io.github.purpleloop.commons.swing.sprites.SpriteSet;

/** A sprite set organized in serial. */
public class SerialSpriteSetIndex implements IndexedSpriteSet {

	/** Offset of the serial index in the sprite names. */
	private static final int SERIAL_OFFSET_IN_NAME = 100;

	/** A serial sprite set. */
	public class SerialSpriteSet {

		/** Abscissa of the first sprite. */
		private int x;

		/** Ordinate of the first sprite. */
		private int y;

		/** Number of sprites. */
		private int count;

		/** Width of each sprite. */
		private int width;

		/** Height of each sprite. */
		private int height;

		/**
		 * Create a serial sprite set from an XML element.
		 * 
		 * @param serialElement XML element
		 */
		public SerialSpriteSet(Element serialElement) {

			this.x = Integer.parseInt(serialElement.getAttribute("x"));
			this.y = Integer.parseInt(serialElement.getAttribute("y"));
			this.width = Integer.parseInt(serialElement.getAttribute("width"));
			this.height = Integer.parseInt(serialElement.getAttribute("height"));
			this.count = Integer.parseInt(serialElement.getAttribute("count"));

		}

		/** @return number of sprites */
		public int getCount() {
			return count;
		}

	}

	/** Name of the XML element for the serial index. */
	public static final String SERIAL_INDEX_ELEMENT = "serialIndex";

	/** The serial sprite set. */
	private List<SerialSpriteSet> series;

	/** Creates an indexed serial sprite set. */
	public SerialSpriteSetIndex() {
		series = new ArrayList<>();
	}

	/**
	 * Reads a serial sprite set from an XML element.
	 * 
	 * @param serialIndexElement the XML element
	 */
	public void readFromXmlElement(Element serialIndexElement) {

		NodeList nodeList = serialIndexElement.getElementsByTagName("serial");
		for (int animationIndex = 0; animationIndex < nodeList.getLength(); animationIndex++) {
			Element serialElement = (Element) nodeList.item(animationIndex);
			series.add(new SerialSpriteSet(serialElement));
		}
	}

	@Override
	public void registerSprites(SpriteSet spriteSet) {

		int serial = 0;

		for (SerialSpriteSet spriteSerie : series) {
			for (int frame = 0; frame < spriteSerie.getCount(); frame++) {

				spriteSet.addSprite(
						new Sprite(getSpriteNameForFrame(serial, frame), spriteSerie.x + spriteSerie.width * frame,
								spriteSerie.y, spriteSerie.width, spriteSerie.height));
			}

			serial++;
		}

	}

	/**
	 * Get the sprite name for a given serial an frame indexes.
	 * 
	 * @param serial index
	 * @param frame  index
	 * @return the sprite name
	 */
	private String getSpriteNameForFrame(int serial, int frame) {
		return "sprite" + (serial * SERIAL_OFFSET_IN_NAME + frame);
	}

	@Override
	public int getIndexFor(Point point) {

		Rectangle rectangle = new Rectangle();

		int serialIndex = 0;
		for (SerialSpriteSet serialSpriteSet : series) {

			rectangle.setBounds(serialSpriteSet.x, serialSpriteSet.y, serialSpriteSet.width, serialSpriteSet.height);
			for (int frame = 0; frame < serialSpriteSet.count; frame++) {

				if (rectangle.contains(point)) {
					return serialIndex + frame;
				}
				rectangle.translate(serialSpriteSet.width, 0);

			}

			serialIndex += SERIAL_OFFSET_IN_NAME;
		}

		return SERIAL_OFFSET_IN_NAME;

	}

	@Override
	public int getForTime(int spriteIndex, double completionFraction) {

		int animationIndex = spriteIndex / SERIAL_OFFSET_IN_NAME;

		SerialSpriteSet serie = series.get(animationIndex);

		if (serie != null) {
			int indexInSequence = Math.min((int) Math.floor(completionFraction * serie.count), serie.count - 1);

			return (animationIndex) * SERIAL_OFFSET_IN_NAME + indexInSequence;

		}

		return 0;
	}

	@Override
	public int getX(int indexSprite) {

		SerialSpriteSet serie = getSerieForSpriteIndex(indexSprite);

		return serie.x + (indexSprite % SERIAL_OFFSET_IN_NAME) * serie.width;
	}

	/**
	 * Get the serial sprite set for the given index.
	 * 
	 * @param indexSprite sprite index
	 * @return serial sprite set
	 */
	private SerialSpriteSet getSerieForSpriteIndex(int indexSprite) {
		return series.get(indexSprite / SERIAL_OFFSET_IN_NAME);
	}

	@Override
	public int getY(int indexSprite) {
		return getSerieForSpriteIndex(indexSprite).y;
	}

	@Override
	public int getWidth(int indexSprite) {
		return getSerieForSpriteIndex(indexSprite).width;
	}

	@Override
	public int getHeigth(int indexSprite) {
		return getSerieForSpriteIndex(indexSprite).height;
	}

}
