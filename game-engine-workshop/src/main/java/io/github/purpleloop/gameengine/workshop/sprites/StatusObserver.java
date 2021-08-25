package io.github.purpleloop.gameengine.workshop.sprites;

/** An observer of status. */
public interface StatusObserver {

	/** Handle status.
	 * @param category the category
	 * @param value the value
	 */
    void setStatus(String category, String value);

}
