package io.github.purpleloop.gameengine.action.model.level;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Models a predefined set of game levels predefined in an XML file. */
public class PredefinedXmlLevelSet implements ILevelManager {

	/** The name of the level set. */
	public static final String PROPERTY_LEVEL_SET_FILE_NAME = "levelSetFileName";

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(PredefinedXmlLevelSet.class);

	/** The game levels. */
	private List<XmlGameLevel> levels;

	/**
	 * Constructor of the game level set.
	 * 
	 * @param config           the game configuration
	 * @param dataFileProvider the data provider
	 * @throws EngineException in case of problems
	 */
	public PredefinedXmlLevelSet(GameConfig config, IDataFileProvider dataFileProvider) throws EngineException {
		levels = new ArrayList<XmlGameLevel>();

		LOG.debug("Loading game levels");

		String levelSetFileName = config.getProperty(PROPERTY_LEVEL_SET_FILE_NAME);

		loadFromXML(levelSetFileName, dataFileProvider, config.getClassName(ClassRole.LEVEL));
	}

	/**
	 * Load a game level set from a file.
	 * 
	 * @param levelSetFileName name of the level set file
	 * @param dfp              data file provider
	 * @param levelClassName   name of the level class
	 */
	public void loadFromXML(String levelSetFileName, IDataFileProvider dfp, String levelClassName) {

		try (InputStream is = dfp.getInputStream(levelSetFileName);) {

			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(is);

			// Get the root level
			Element root = doc.getDocumentElement();

			NodeList levelNodeList = root.getElementsByTagName("level");
			Element levelElement;
			Class<?> c = Class.forName(levelClassName);

			for (int nodeIndex = 0; nodeIndex < levelNodeList.getLength(); nodeIndex++) {

				XmlGameLevel gameLevel = (XmlGameLevel) c.getDeclaredConstructor().newInstance();

				// Warning, the number of nodes is not the number of lines

				levelElement = (Element) levelNodeList.item(nodeIndex);
				gameLevel.loadFromXml(levelElement);
				levels.add(gameLevel);
			}

		} catch (Exception e) {
			LOG.error("Error while reading XML predefined level set : " + levelSetFileName, e);
		}
		LOG.debug("Number of loaded levels : " + levels.size());
	}

	/**
	 * Provides a requested level.
	 * 
	 * @param index the level index
	 * @return the game level
	 */
	public IGameLevel getLevel(int index) {
		return levels.get(index);
	}

	@Override
	public int getSize() {
		return levels.size();
	}

}