package io.github.purpleloop.gameengine.workshop.sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.swing.sprites.SpriteSet;
import io.github.purpleloop.commons.xml.XMLTools;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * Describes a sprite model.
 * 
 * This is the combination of a source image with various indexes used to define
 * how sprites are organized an can be obtained from the source image.
 * 
 */
public class SpriteModel {

	/** Class logger. */
	public static final Log LOG = LogFactory.getLog(SpriteModel.class);

	/** The persistent part of the model (file). */
	private File modelFile;

	/** Path of the source image. */
	private String sourceImagePath;

	/** The indexed sprite set. */
	private IndexedSpriteSet index;

	/** The sprite set. */
	private SpriteSet spriteSet;

	/** Rectangles. */
	private ArrayList<Rectangle2D> rectangles;

	/**
	 * Constructor of the sprite model.
	 * 
	 * @param fileName the file name
	 * @throws EngineException in case of problem
	 */
	public SpriteModel(String fileName) throws EngineException {
		rectangles = new ArrayList<>();

		if (fileName.endsWith(".xml")) {

			loadSpriteModelFromXMLFile(new File(fileName));

		} else {

			// Creation of the sprite model with a single image
			try {
				spriteSet = new SpriteSet(fileName);
				index = new SerialSpriteSetIndex();

			} catch (PurpleException e) {
				LOG.error("Error while creating spriteset", e);
			}

		}

	}

	/**
	 * Loads the sprite model from an XML file.
	 * 
	 * @param file the sprite model description
	 * @throws EngineException in case of problem
	 */
	private void loadSpriteModelFromXMLFile(File file) throws EngineException {

		this.modelFile = file;

		try {
			Document doc = XMLTools.getDocument(file);
			Element spriteDescriptorElement = doc.getDocumentElement();

			Optional<Element> sourceImageElementOptional = XMLTools.getUniqueChildElement(spriteDescriptorElement, "sourceImage");
			if (sourceImageElementOptional.isPresent()) {
				Element sourceImageElement = sourceImageElementOptional.get();
				this.sourceImagePath = sourceImageElement.getAttribute("path");

				LOG.info("Loading sprites from the source image " + sourceImagePath);
				this.spriteSet = new SpriteSet(sourceImagePath);

			}

			Optional<Element> indexesElementOptional = XMLTools.getUniqueChildElement(spriteDescriptorElement,
					"indexes");

			if (indexesElementOptional.isPresent()) {
				Element indexesElement = indexesElementOptional.get();

				for (Element indexElement : XMLTools.getChildElements(indexesElement)) {

					String indexType = indexElement.getTagName();

					if (indexType.equals(SpriteGridIndex.GRID_INDEX_ELEMENT)) {
						index = readGridSpriteSetIndex(indexElement);

					} else if (indexType.equals(SerialSpriteSetIndex.SERIAL_INDEX_ELEMENT)) {
						index = readSerialSpriteSet(indexElement);
					}
				}

			}

		} catch (PurpleException e) {
			LOG.error("Error while loading sprite model from file", e);
			throw new EngineException("Error while reading the sprite model.", e);
		}

		index.registerSprites(spriteSet);
	}

	/**
	 * Read a serial sprite set from an XML element.
	 * 
	 * @param serialIndexElement the XML element
	 * @return the serial sprite set index
	 */
	private SerialSpriteSetIndex readSerialSpriteSet(Element serialIndexElement) {

		SerialSpriteSetIndex serialIndex = new SerialSpriteSetIndex();
		serialIndex.readFromXmlElement(serialIndexElement);
		return serialIndex;
	}

	/**
	 * Read a grid sprite set from an XML element.
	 * 
	 * @param gridElement the XML element
	 * @return the grid sprite set index
	 */
	private SpriteGridIndex readGridSpriteSetIndex(Element gridElement) {

		SpriteGridIndex gridIndex = new SpriteGridIndex();
		gridIndex.readFromXmlElement(gridElement);
		return gridIndex;
	}

	/** @return the image used for the sprite model */
	public Image getImage() {
		return spriteSet.getSourceImage();
	}

	/**
	 * @return the index used by the model
	 */
	public IndexedSpriteSet getIndex() {
		return index;
	}

	/**
	 * @return the name of the file used in the model
	 */
	public String getFileName() {
		return modelFile.getAbsolutePath();
	}

	/**
	 * Adds a rectangle to the model.
	 * 
	 * @param rect the rectangle to add
	 */
	public void addRectangle(Rectangle2D rect) {
		LOG.debug("Adding the rectangle " + rect);
		rectangles.add(rect);
	}

	/** @return the rectangles of the model */
	public List<Rectangle2D> rectangles() {
		return rectangles;
	}

	/**
	 * Put the sprite at the given location.
	 * 
	 * @param canvas     the graphics on which to paint
	 * @param iob        the image observer
	 * @param spriteName the name of the sprite to paint
	 * @param x          abscissa
	 * @param y          ordinate
	 */
	public void putSprite(Graphics canvas, ImageObserver iob, String spriteName, int x, int y) {
		this.spriteSet.putSprite(canvas, iob, spriteName, x, y);
	}

	/**
	 * Put the sprite for the animation time.
	 * 
	 * @param canvas                the graphics on which to paint
	 * @param iob                   the image observer
	 * @param currentAnimationIndex the animation index
	 * @param progression           the progression of the animation
	 * @param x                     abscissa
	 * @param y                     ordinate
	 */
	public void putSpriteForTime(Graphics canvas, ImageObserver iob, int currentAnimationIndex, double progression,
			int x, int y) {
		int finalIndex = getIndex().getForTime(currentAnimationIndex, progression);
		putSprite(canvas, iob, "sprite" + finalIndex, x, y);
	}

}
