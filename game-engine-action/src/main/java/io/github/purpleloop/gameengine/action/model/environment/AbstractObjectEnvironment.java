package io.github.purpleloop.gameengine.action.model.environment;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.action.model.events.BasicEvent;
import io.github.purpleloop.gameengine.action.model.interfaces.IAgent;
import io.github.purpleloop.gameengine.action.model.interfaces.IControllableAgent;
import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironment;
import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironmentObjet;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.level.IGameLevel;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * This class describes an abstract game environment containing objects,
 * including agents.
 * 
 * A particular agent is controlled and put in the 'controlled' cache.
 * 
 * This environment class does not handle the geometric structure and let this
 * characteristic to subclasses.
 */
public abstract class AbstractObjectEnvironment extends CommonAbstractEnvironment
        implements IEnvironment {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(AbstractObjectEnvironment.class);

    /** Max agents for environment property. */
    private static final String ENVIRONMENT_MAX_AGENTS = "environment.maxAgents";

    /** Maximal number of agents living in this environment. */
    protected int maxAgents;

    /** Environment objects. */
    private List<IEnvironmentObjet> envObjects;

    /** Objects removed. */
    private List<IEnvironmentObjet> removeList;

    /** Objects added. */
    private List<IEnvironmentObjet> addedList;

    /**
     * The game level that serves as reference for the static structure of the
     * environment.
     */
    private IGameLevel level;

    /**
     * Creates an abstract object environment.
     * 
     * @param session session in which the environment evolves
     * @param level the game level
     * @throws EngineException in case of problems during environment creation
     */
    protected AbstractObjectEnvironment(ISession session, IGameLevel level) throws EngineException {
        super(session);
        this.envObjects = new LinkedList<>();
        this.removeList = new LinkedList<>();
        this.addedList = new LinkedList<>();
        this.level = level;

        GameConfig config = session.getGameEngine().getConfig();
        maxAgents = config.getIntProperty(ENVIRONMENT_MAX_AGENTS);
    }

    /**
     * Initializes the environment from the static structure. Advice : Should be
     * called during environment creation, after environment metrics are set.
     */
    protected abstract void initFromGameLevel();

    /**
     * Updates the environment.
     * 
     * @throws EngineException in case of error
     */
    public final synchronized void update() throws EngineException {

        doEvolveObjects();
        doRemoveObjects();
        doAddObjects();
        specificEvolve();
        fireEnvironmentChanged(new BasicEvent(BasicEvent.ENVIRONNEMENT_UPDATED));
    }

    /**
     * Makes each object of the environment evolve according to the laws like
     * the physics of the environment and of the game. Before evolution, the
     * agents are allowed to act according to the external controls or their
     * internal behavior.
     * 
     * @throws EngineException in case of errors
     */
    protected void doEvolveObjects() throws EngineException {

        for (IEnvironmentObjet envObject : getObjects()) {

            if (envObject instanceof IAgent) {
                ((IAgent) envObject).behave();
            }

            envObject.evolve();
        }
    }

    /** Do the specific evolutions of the environment. */
    protected void specificEvolve() throws EngineException {
    }

    /**
     * Add an object to the environment.
     * 
     * @param obj the added object
     * @throws EngineException in case of error
     */
    protected final void addObject(IEnvironmentObjet obj) throws EngineException {

        LOG.debug("Add object " + obj);

        envObjects.add(obj);

        if (obj instanceof IControllableAgent) {
            setControlledElement((IControllableAgent) obj);
        }
    }

    /**
     * Prepare the addition of an object to the environment.
     * 
     * @param obj the pre-added object
     * 
     *            The object will be added later by
     *            {@link AbstractObjectEnvironment#doAddObjects()}.
     * 
     */
    public final void preAddObject(IEnvironmentObjet obj) {
        addedList.add(obj);
    }

    /** @return all objects of the environment */
    public final List<IEnvironmentObjet> getObjects() {
        return envObjects;
    }

    /** Removes from the environment all objects 'marked' for removal. */
    protected final synchronized void doRemoveObjects() {
        for (IEnvironmentObjet obj : removeList) {

            LOG.debug("Remove object " + obj);

            envObjects.remove(obj);
        }
        removeList.clear();
    }

    /**
     * Adds to the environment all objects 'marked' for addition during a
     * pre-addition.
     */
    protected final synchronized void doAddObjects() {
        for (IEnvironmentObjet obj : addedList) {
            envObjects.add(obj);
        }
        addedList.clear();
    }

    /**
     * Marks an object of the environment for a removal.
     * 
     * @param obj the object to remove The object will be removed later by
     *            {@link AbstractObjectEnvironment#doRemoveObjects()}.
     */
    public void markObjectForRemoval(IEnvironmentObjet obj) {
        LOG.debug("Object will be removed " + obj);
        removeList.add(obj);
    }

    /** {@inheritDoc} */
    public final IGameEngine getGameEngine() {
        return this.session.getGameEngine();
    }

    /**
     * @return The game level that serves as reference for the static structure
     *         of the environment
     */
    public IGameLevel getLevel() {
        return level;
    }

    @Override
    public void dumpEnvironmentObjects() {
        dumpObjects();
    }

    /** Dumps to logs all objects of the environment. */
    private void dumpObjects() {
        LOG.debug("Object dump");
        for (IEnvironmentObjet objet : getObjects()) {
            LOG.debug("- " + objet.toString());
        }
    }

}
