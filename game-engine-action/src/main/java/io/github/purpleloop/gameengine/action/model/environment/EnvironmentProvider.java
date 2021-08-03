package io.github.purpleloop.gameengine.action.model.environment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.lang.ReflexivityTools;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;
import io.github.purpleloop.gameengine.action.model.level.IGameLevel;
import io.github.purpleloop.gameengine.action.model.level.ILevelManager;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** A provider of game environments, based on the game configuration. */
public class EnvironmentProvider {

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(EnvironmentProvider.class);

	/** Indicates there is currently no current level. */
	private static final int NO_LEVEL = -1;

	/** The game engine holding the level manager and providing level resources. */
	private IGameEngine gameEngine;

	/** Current level index. */
	protected int currentLevelIndex = NO_LEVEL;

	/**
	 * Constructor of the environment provider.
	 * 
	 * @param gameEngine the game engine
	 */
	public EnvironmentProvider(IGameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	/**
	 * Initializes the environment for the next level.
	 * 
	 * Environment are created by reflection from the class name specified in the
	 * game configuration.
	 * 
	 * @param session the current game session
	 * @return the initialized environment
	 * 
	 * @throws EngineException in case of problem during initialization
	 */
	public ISessionEnvironment getEnvironmentForNextLevel(ISession session) throws EngineException {

		// FIME : may be a feature envy, it's more the level manager that knows what the
		// next level will be ...
		// Possible improvement here : make levels/maps links less linear, a graph
		// defined in level set ?
		ILevelManager levelManager = gameEngine.getLevelManager();
		if (currentLevelIndex < levelManager.getSize() - 1) {
			currentLevelIndex++;
		}

		LOG.debug("Initialization of game for the level : " + currentLevelIndex);

		Class<?>[] paramClasses = new Class<?>[2];
		paramClasses[0] = ISession.class;
		paramClasses[1] = IGameLevel.class;

		IGameLevel level = levelManager.getLevel(currentLevelIndex);

		Object[] paramValues = new Object[2];
		paramValues[0] = session;
		paramValues[1] = level;

		try {
			return ReflexivityTools.createInstance(
					gameEngine.getConfig().getClassName(ClassRole.ENVIRONMENT), paramClasses, paramValues);
		} catch (PurpleException e) {
			throw new EngineException("Error while creating the environment", e);
		}

	}

}
