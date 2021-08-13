package io.github.purpleloop.gameengine.network.connection;

/** Connection states. */
public enum NetConnectionState {

	/** Disconnected. */
	DISCONNECTED("disconnected"),

	/** Connecting. */
	CONNECTING("connecting"),

	/** Connection established. */
	CONNECTED("connection established"),

	/** Listening for connections. */
	LISTENING("Listening");

	/** State label. */
	private String label;

	/**
	 * Connection state constructor.
	 * 
	 * @param label The state label
	 */
	NetConnectionState(String label) {
		this.label = label;
	}

	/** @return the state label */
	public String getLabel() {
		return this.label;
	}
}
