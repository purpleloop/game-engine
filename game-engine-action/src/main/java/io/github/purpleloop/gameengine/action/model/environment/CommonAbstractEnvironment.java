package io.github.purpleloop.gameengine.action.model.environment;

import java.util.ArrayList;
import java.util.List;

import io.github.purpleloop.gameengine.action.model.events.IGameEvent;
import io.github.purpleloop.gameengine.action.model.interfaces.IControllable;
import io.github.purpleloop.gameengine.action.model.interfaces.IController;
import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironmentObserver;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.action.model.interfaces.ISessionEnvironment;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * This class defines an abstract base from game environments.
 * 
 * An environment can typically represent the 'local' game state. Local in time,
 * local in space. It can be used for a given game level and during a game
 * session.
 * 
 * Current limit : An environment has a single controlled element. 
 * 
 * TODO more than one controllable agents, towards multi-player ?
 * 
 */
public abstract class CommonAbstractEnvironment implements ISessionEnvironment {

	/** The 'do nothing' action. */
	public static final int ACTION_NONE = -1;

	/** Environment observers. */
	private List<IEnvironmentObserver> environmentObservers;

	/** The game session that serves as context for the environment. */
	protected ISession session;

	/** The controlled element of the environment. */
	private IControllable controlled;

	/**
	 * Base constructor.
	 * 
	 * @param session session in which environment takes place
	 */
	protected CommonAbstractEnvironment(ISession session) {
		this.session = session;
		this.environmentObservers = new ArrayList<>();
	}

	/**
	 * Add an observer to the environment
	 * 
	 * @param observer observer to add
	 */
	public final void addObserver(IEnvironmentObserver observer) {

		if (!environmentObservers.contains(observer)) {
			environmentObservers.add(observer);
		}
	}

	/**
	 * Removes an observer from the environment.
	 * 
	 * @param observer observer to remove
	 */
	public final void removeObserver(IEnvironmentObserver observer) {
		environmentObservers.remove(observer);
	}

	/**
	 * Fires a notification of a change in the environment for all observers
	 * 
	 * @param gameEvent the event causing the change
	 */
	protected final void fireEnvironmentChanged(IGameEvent gameEvent) {
		for (IEnvironmentObserver observer : environmentObservers) {
			observer.environmentChanged(gameEvent);
		}
	}

	/**
	 * Sets the controlled object of the environment. Currently, only a single
	 * controllable is supported. The controlled object will receive actions
	 * incoming from the controller.
	 * 
	 * @param controlled the controlled object
	 * @throws EngineException in case of problem
	 * 
	 */
	protected void setControlledElement(IControllable controlled) throws EngineException {
		if (this.controlled != null) {

			throw new EngineException("Controlled object is already set " + ", cannot change to " + controlled);
		}

		this.controlled = controlled;
	}

	@Override
	public final void setController(IController controller) {

		registerControlListener(controller);
		controller.registerControlListener(controlled);
	}

	@Override
	public final void removeController(IController controller) {
		unRegisterControlListener(controller);
		controller.unRegisterControlListener(controlled);
	}

	/**
	 * Do operations while registering a control listener.
	 * 
	 * @param controller the controller
	 */
	protected void registerControlListener(IController controller) {
	}

	/**
	 * Unregister a control listener.
	 * 
	 * @param controller the controller
	 */
	protected void unRegisterControlListener(IController controller) {
	}

	/** @return the current controllable */
	public IControllable getControllable() {
		return this.controlled;
	}

}
