package io.github.purpleloop.gameengine.core.config;

import java.awt.Image;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.xml.XMLTools;
import io.github.purpleloop.gameengine.core.sound.interfaces.ISoundFileResolver;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * Models the configuration of the game engine for a given game.
 * 
 * This is the base class used to configure the game engine, provided by a
 * configuration file.
 */
public class GameConfig implements ISoundFileResolver {

	/** Class logger. */
	private static Log log = LogFactory.getLog(GameConfig.class);

	/** Path to images files. */
	private static final String IMAGE_PATH = "media/images/";

	/** Path to sound files. */
	private static final String SOUND_PATH = "media/sounds/";

	/** Map of the image files. */
	private Map<String, String> imageFiles;

	/** Map of the sound files. */
	private Map<String, String> soundFiles;

	/** Map of keyboard actions. */
	private KeyBoardActionMap keyMap;

	/** Class names per role. */
	private Map<ClassRole, String> classes;

	/** Additional configuration properties. */
	private Properties properties;

	/** Constructor for the game engine configuration. */
	public GameConfig() {
		super();

		log.info("Creating a new game engine configuration");

		soundFiles = new HashMap<>();
		imageFiles = new HashMap<>();

		keyMap = new KeyBoardActionMap();

		classes = new EnumMap<>(ClassRole.class);
		properties = new Properties();
	}

	/**
	 * Reads and builds the configuration of the game engine from a file.
	 * 
	 * @param dfp            data file provider
	 * @param configFileName name of the game engine configuration file
	 * @return game engine configuration
	 * @throws EngineException in case of problem
	 */
	public static synchronized GameConfig parse(IDataFileProvider dfp, String configFileName) throws EngineException {

		log.info("Reading the configuration file : " + configFileName);

		InputStream is = dfp.getInputStream(configFileName);

		GameConfig createdGameConfig = new GameConfig();

		Document doc;
		try {
			doc = XMLTools.getDocument(is);
		} catch (PurpleException e1) {
			log.error("An error occured while reading the game engine configuration file " + configFileName, e1);
			throw new EngineException("The reading of the game engine configuration fil has failed.");
		}

		Element root = doc.getDocumentElement();

		parseProperties(createdGameConfig, root);

		parseClasses(createdGameConfig, root);

		parseKeymap(createdGameConfig, root);

		for (Element elementImage : XMLTools.getChildElements(root, "image")) {
			String name = elementImage.getAttribute("name");
			String location = elementImage.getAttribute("location");
			createdGameConfig.addImage(name, location);
		}

		for (Element elementSound : XMLTools.getChildElements(root, "sound")) {
			String name = elementSound.getAttribute("name");
			String location = elementSound.getAttribute("location");
			createdGameConfig.addSound(name, location);
		}

		return createdGameConfig;
	}

	/**
	 * Parse specific game properties.
	 * 
	 * @param gameConfig the game configuration
	 * @param element    XML element where to read properties
	 * @throws EngineException in case of errors
	 */
	private static void parseProperties(GameConfig gameConfig, Element element) throws EngineException {

		try {
			Optional<Element> propertiesElementOptional = XMLTools.getUniqueElement(element, "properties");

			if (propertiesElementOptional.isPresent()) {

				Element propertiesElement = propertiesElementOptional.get();

				log.debug("Configuring specific properties");

				for (Element elementClasse : XMLTools.getChildElements(propertiesElement, "property")) {
					String propertyName = elementClasse.getAttribute("name");

					if (propertyName == null) {
						throw new EngineException("Missing property name in configuration");
					}

					String propertyValue = elementClasse.getAttribute("value");

					if (StringUtils.isEmpty(propertyValue)) {
						throw new EngineException("Missing value for property " + propertyName + ".");
					}

					log.debug("Property " + propertyName + " -> " + propertyValue);

					gameConfig.setProperty(propertyName, propertyValue);
				}
			}

		} catch (PurpleException e) {
			log.error("XML error while reading game properties", e);
			throw new EngineException(e.getMessage(), e);
		}

	}

	/**
	 * Parse game classes.
	 * 
	 * @param gameConfig the game configuration
	 * @param element    XML element where to read properties
	 * @throws EngineException in case of errors
	 */
	private static void parseClasses(GameConfig gameConfig, Element element) throws EngineException {
		for (Element elementClasse : XMLTools.getChildElements(element, "class")) {
			String role = elementClasse.getAttribute("role");

			if (StringUtils.isEmpty(role)) {
				throw new EngineException("The name of the role is missing.");
			}

			String name = elementClasse.getAttribute("classname");

			if (StringUtils.isEmpty(name)) {
				throw new EngineException("The name of the class for the role " + role + " is missing.");
			}

			gameConfig.addClass(ClassRole.valueOf(StringUtils.upperCase(role)), name);
		}
	}

	/**
	 * Parse game keyboard map.
	 * 
	 * @param gameConfig the game configuration
	 * @param element    XML element where to read properties
	 */
	private static void parseKeymap(GameConfig gameConfig, Element element) {
		gameConfig.keyMap.fillFromElement(XMLTools.getChildElements(element, "keymap"));
	}

	/**
	 * Sets a property.
	 * 
	 * @param propertyName  property name
	 * @param propertyValue property value
	 */
	private void setProperty(String propertyName, String propertyValue) {
		this.properties.setProperty(propertyName, propertyValue);
	}

	/**
	 * Registers a class name for a given role in the game engine.
	 * 
	 * @param role role name
	 * @param name class name
	 */
	private void addClass(ClassRole role, String name) {
		classes.put(role, name);
	}

	/**
	 * Add an image to the engine.
	 * 
	 * @param name     the image name
	 * @param location the image file location (relative to
	 *                 {@link GameConfig#IMAGE_PATH}).
	 */
	public void addImage(String name, String location) {
		imageFiles.put(name, IMAGE_PATH + location);
	}

	/**
	 * Add an sound to the engine.
	 * 
	 * @param name     the sound name
	 * @param location the sound file location (relative to
	 *                 {@link GameConfig#SOUND_PATH}).
	 */
	public void addSound(String name, String location) {
		soundFiles.put(name, SOUND_PATH + location);
	}

	/**
	 * Obtain the image for a given image name.
	 * 
	 * @param dataFileProvider the data file provider
	 * @param imageName        the image name
	 * @return the requested image
	 * @throws EngineException in case of problems
	 */
	public Image getImage(IDataFileProvider dataFileProvider, String imageName) throws EngineException {
		return dataFileProvider.getImage(imageFiles.get(imageName));
	}

	@Override
	public String getSoundFileName(String soundName) {
		return soundFiles.get(soundName);
	}

	/**
	 * Get the class name for a given role.
	 * 
	 * @param role class role
	 * @return class name
	 * @throws EngineException in case of error
	 */
	public String getClassName(ClassRole role) throws EngineException {

		String roleName = classes.get(role);

		if (StringUtils.isEmpty(roleName)) {
			throw new EngineException("The name of the class to load for the role " + role
					+ " is missing in the game engine configuration.");
		}

		return classes.get(role);
	}

	/**
	 * Get an integer property.
	 * 
	 * @param propertyName the name of the property
	 * @return the integer property value
	 * @throws EngineException in case of error on the property
	 */
	public int getIntProperty(String propertyName) throws EngineException {

		String strValue = getProperty(propertyName);

		if (strValue == null) {
			throw new EngineException("The value of the property " + propertyName + " must be an integer.");
		}

		return Integer.parseInt(strValue);
	}

	/**
	 * Get an property value.
	 * 
	 * @param propertyName the name of the property
	 * @return the property value
	 */
	public String getProperty(String propertyName) {
		return this.properties.getProperty(propertyName);
	}

	/** @return the keyboard mappings */
	public KeyBoardActionMap getKeyMap() {
		return keyMap;
	}

}
