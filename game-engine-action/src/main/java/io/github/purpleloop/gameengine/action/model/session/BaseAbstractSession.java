package io.github.purpleloop.gameengine.action.model.session;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.action.model.environment.EnvironmentProvider;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.IPlayer;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;
import io.github.purpleloop.gameengine.action.model.level.ILevelManager;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * Models a game session.
 * 
 * This is all the time the user is "in game", from "game start" to "game over".
 * 
 * A session spreads across several levels. For sake of simplicity, the current
 * level is the active environment and is unique at a given time.
 */
public abstract class BaseAbstractSession implements ISession {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(BaseAbstractSession.class);

    /** The game engine where the session runs. */
    protected IGameEngine gameEngine;

    /** The level manager. */
    protected ILevelManager levelManager;

    /** The environment provider. */
    private EnvironmentProvider environmentProvider;

    /** The current environment. */
    protected ISessionEnvironment currentEnvironment;

    /** The current level id. */
    protected String currentLevelId;

    /** Should the level change on next update ? */
    private boolean changeLevelOnNextupdate;

    /**
     * The target level id, if a level change occur. May be a specific level
     * change class can be used here later ?
     */
    private String targetLevelId;

    /** The players. */
    protected List<IPlayer> players;

    /**
     * Creates a game session.
     * 
     * @param gameEngine The game engine where the session occurs
     * @throws EngineException in case of problem
     */
    protected BaseAbstractSession(IGameEngine gameEngine) throws EngineException {
        LOG.debug("Creating the game session");
        this.gameEngine = gameEngine;

        this.players = new ArrayList<>();

        // Initialize the game levels
        levelManager = gameEngine.getLevelManager();

        initSession(levelManager.getStartLevelId());

        environmentProvider = new EnvironmentProvider(gameEngine);

        setupLevel();
    }

    /** Common tasks to be performed at the beginning of session.
     * @param levelId id of the level to initialize
     */
    protected final void initSession(String levelId) {
        this.targetLevelId = levelId;

        initSessionSpecific();
    }

    /** Game specific tasks to be performed at the beginning of session. */
    protected void initSessionSpecific() {
    }

    /** Cleans the current environment if it exists. */
    private void cleanupCurrentEnvironment() {

        if (currentEnvironment != null) {
            LOG.debug("Cleaning the current environment");
            currentEnvironment.removeController(gameEngine.getController());
            currentEnvironment.removeObserver(this);
            currentEnvironment = null;
        }
    }

    @Override
    public final ISessionEnvironment getCurrentEnvironment() {
        return currentEnvironment;
    }

    @Override
    public final synchronized void update() throws EngineException {

        if (changeLevelOnNextupdate) {
            setupLevel();
            changeLevelOnNextupdate = false;
        }

        updateSpecific();
    }

    /**
     * Specific updates of the session.
     * 
     * @throws EngineException in case of problem
     */
    protected void updateSpecific() throws EngineException {
        if (currentEnvironment != null) {
            currentEnvironment.update();
        }
    }

    @Override
    public final IGameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Prepare the environment for the given target level.
     * 
     * @throws EngineException in case of problem
     */
    private void setupLevel() throws EngineException {

        cleanupCurrentEnvironment();

        LOG.info("Setup for target level " + targetLevelId);
        currentEnvironment = environmentProvider.getEnvironmentForLevel(this);
        currentLevelId = targetLevelId;
        currentEnvironment.setController(gameEngine.getController());
        currentEnvironment.addObserver(this);
    }

    /** @return the current level id */
    public String getCurrentLevelId() {
        return currentLevelId;
    }

    /** @param currentLevelId the current level id */
    public void setCurrentLevelId(String currentLevelId) {
        this.currentLevelId = currentLevelId;
    }

    /** @param targetLevelId target level id */
    public void setTargetLevelId(String targetLevelId) {
        this.targetLevelId = targetLevelId;
    }

    /** @return the target level id */
    public String getTargetLevelId() {
        return targetLevelId;
    }

    @Override
    public void cleanup() {
        cleanupCurrentEnvironment();
    }

    /**
     * Asks for a change of level for the next update of the session.
     */
    protected final void prepareLevelChange() {

        LOG.debug("Prepare for level change ...");
        changeLevelOnNextupdate = true;
    }

    /**
     * Add a player to the session.
     * 
     * @param player the player to add
     */
    protected void addPlayer(IPlayer player) {
        LOG.debug("Adding player " + player);
        players.add(player);
    }

    @Override
    public List<IPlayer> getPlayers() {
        return players;
    }
}
