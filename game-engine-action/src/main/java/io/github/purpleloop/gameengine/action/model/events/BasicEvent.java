package io.github.purpleloop.gameengine.action.model.events;

/** A basic game event. */
public class BasicEvent extends BaseGameEvent {

	/** This event typically occurs when the environment has been updated. */
	public static final int ENVIRONNEMENT_UPDATED = 0;

	/**
	 * Creates a basic event.
	 * 
	 * @param code the event code
	 */
	public BasicEvent(int code) {
		super(code);
	}

}
