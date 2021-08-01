package io.github.purpleloop.gameengine.action.model.events;

/** A base class for game events, typically, when someone do something. */
public abstract class BaseGameEvent implements IGameEvent {

	/** Event code. */
	private int code;

	/**
	 * Creates a game event.
	 * 
	 * @param code event code
	 */
	protected BaseGameEvent(int code) {
		this.code = code;
	}

	/** @return the event code */
	public int getCode() {
		return this.code;
	}
}
