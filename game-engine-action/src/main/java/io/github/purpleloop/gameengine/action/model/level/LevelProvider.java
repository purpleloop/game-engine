package io.github.purpleloop.gameengine.action.model.level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.lang.ReflexivityTools;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** A provider of game levels, based on the game configuration. */
public class LevelProvider {

	/** Indicates there is currently no current level. */
    private static final int NO_LEVEL = -1;

	/** Class logger. */
    private static final Log LOG = LogFactory.getLog(LevelProvider.class);
    
    /** The game engine holding the level manager and providing level resources. */
    private IGameEngine gameEngine;

    /** Current level index. */
    protected int currentLevelIndex = NO_LEVEL;

    /** Constructor of the level provider.
     * @param gameEngine the game engine
     */
    public LevelProvider(IGameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /**
     * Initializes the environment with the next level.
     * 
     * Level are loaded by reflection from the game configuration.
     * 
     * 
     * @param session the current game session
     * @return the initialized environment
     * 
     * @throws EngineException in case of problem during initialization
     */
    public ISessionEnvironment getNextLevel(ISession session) throws EngineException {

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

        ISessionEnvironment currentEnvironment = ReflexivityTools.createInstance(
                gameEngine.getConfig().getClassName(ClassRole.ENVIRONMENT), paramClasses, paramValues);

        return currentEnvironment;
    }

}
