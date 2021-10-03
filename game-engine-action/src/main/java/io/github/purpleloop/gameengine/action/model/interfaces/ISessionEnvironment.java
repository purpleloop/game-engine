package io.github.purpleloop.gameengine.action.model.interfaces;

import io.github.purpleloop.gameengine.core.util.EngineException;

/** Base interface for modeling the game environment.
 * 
 * Agents and other objects, events ...
 * 
 * An environment can typically represent a game level.
 */
public interface ISessionEnvironment {

	/** Dumps to log an snapshot of all objects in the current environment if it exists. */
	void dumpEnvironmentObjects();

	/** Updates the environment, by one unit of time. 
	 * @throws EngineException in case of problem
	 */
	void update() throws EngineException;

	/** Adds an observer to the environment.
	 * @param obs the observer to add
	 */
	void addObserver(IEnvironmentObserver obs);

	/** Remove an observer from the environment.
	 * @param obs observer to remove
	 * 
	 */
	void removeObserver(IEnvironmentObserver obs);

    /**
     * Sets the controller for controlled agents.
     * 
     * @param controller the controller to use
     */
	void setController(IController controller);

    /**
     * Removes the controller.
     * 
     * @param controller the controller to remove
     */
    void removeController(IController controller);

    /** Do a specific cleanup of the environment, if necessary. */
    void specificCleanUp();

}
