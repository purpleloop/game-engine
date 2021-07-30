package io.github.purpleloop.gameengine.action.model.environment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.commons.lang.Byte2DMatrix;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.objects.Agent;
import io.github.purpleloop.gameengine.action.model.objects.GridMobile;

/** Abstract extension environment class that can store objects. */
public abstract class AbstractEnvironment extends CommonAbstractEnvironment {

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(AbstractEnvironment.class);

	/** Storage for objects of the environment. */
	private Byte2DMatrix storage;

	/** Mask for objects. */
	private static final int OBJECT_MASK_VALUE = 255;

	/**
	 * Creates an abstract environment.
	 * 
	 * @param session the session in which the environment occurs
	 */
	public AbstractEnvironment(ISession session) {
		super(session);
	}

	/**
	 * Adds an agent to the environment.
	 * 
	 * FIXME : addAgent is not yet implemented, see if it is still interesting.
	 * 
	 * @param agent agent to add
	 */
	@Deprecated
	protected void addAgent(Agent agent) {
		LOG.warn("addAgent is not yet implemented");
	}

	/**
	 * Tests if the given location contains a specific object.
	 * 
	 * @param location   the location to test
	 * @param objectCode Code of the object to test
	 * @return true if the object is stored at this location
	 */
	public boolean containsObject(GridMobile location, byte objectCode) {
		return (storage.get(location.getXloc(), location.getYloc()) & objectCode) > 0;
	}

	/**
	 * Tests if the given location does not contain a specific object.
	 * 
	 * @param x          abscissa
	 * @param y          ordinate
	 * @param objectCode Code of the object to test
	 * @return true if the object is NOT stored at this location
	 */
	public boolean containsNotObject(int x, int y, byte objectCode) {
		return (storage.get(x, y) & objectCode) == 0;
	}

	// --- Changes ----------------------------------------------------

	/**
	 * Add an object to the current location.
	 * 
	 * @param location   the location where to store the object
	 * @param objectCode Code of the object to add
	 */
	public void addObjet(GridMobile location, byte objectCode) {
		storage.or(location.getXloc(), location.getYloc(), objectCode);
	}

	/**
	 * Remove an object from the given location.
	 * 
	 * @param loc        the location where to remove the object
	 * @param objectCode Code of the object to remove
	 */
	public void removeObject(GridMobile loc, byte code) {
		storage.and(loc.getXloc(), loc.getYloc(), OBJECT_MASK_VALUE - code);
	}

	/** Revive the player agent. */
	public abstract void resurrect();

	/** Updates the environment. */
	public abstract void update();

	/**
	 * Tests if a given action can be made by the player from the given location.
	 * 
	 * @param loc    location from where to act
	 * @param action action to test
	 * @return true if the action is possible, false otherwise
	 */
	public abstract boolean isActionPossibleFromLocation(GridMobile loc, int action);

	/**
	 * Initialize the contents of objects from an array
	 * 
	 * @param width          width of the environment
	 * @param height         height of the environment
	 * @param initialObjects initial objects to put in the environment
	 */
	protected void initStorage(int width, int height, byte[][] initialObjects) {
		storage = new Byte2DMatrix(width, height, initialObjects);
	}

	/** {@inheritDoc} */
	public void dumpEnvironmentObjects() {
		LOG.debug("Objects Dump :\n" + storage.hexDump());
	}

	/**
	 * Get the orientation for an action.
	 * 
	 * @param atemptedAction action to try
	 * @return direction
	 */
	public abstract Direction getOrientationForAction(int atemptedAction);
}
