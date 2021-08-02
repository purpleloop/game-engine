package io.github.purpleloop.gameengine.action.model;

/** A timer used for action games. */
public class GameTimer {

	/** 1.000.000 nanoseconds per milliseconds. */
	private static final long NANO_PER_MILLIS = 1000000L;

	/** The delay in milliseconds. */
	private long delay;

	/** Current reference time. */
	private long start;

	/**
	 * Creates a new timer.
	 * 
	 * @param delay delay in milliseconds
	 */
	public GameTimer(long delay) {
		this.delay = delay;
		reset();
	}

	/** @return true if the timer has passed, false otherwise. */
	public boolean passed() {
		return (System.nanoTime() - start) / NANO_PER_MILLIS > delay;
	}

	/** Resets the timer. */
	public void reset() {
		this.start = System.nanoTime();
	}

}
